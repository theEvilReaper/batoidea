package net.theEvilReaper.batoidea.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.theevilreaper.bot.api.database.IRedisEventManager;
import net.theevilreaper.bot.api.database.REvent;
import net.theevilreaper.bot.api.database.RedisConnector;
import net.theevilreaper.bot.api.util.Conditions;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RFuture;
import org.redisson.api.RTopic;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class RedisEventManager implements IRedisEventManager {

    private static final TypeReference<Map<String, Object>> MAP_TOKEN = new TypeReference<>(){};
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Logger logger;
    private final RedisConnector redis;
    private final Map<Class<? extends REvent>, List<Consumer<REvent>>> listeners = new HashMap<>();
    private final Map<Class<? extends REvent>, RTopic> topics = new HashMap<>();
    private final RTopic mainTopic;

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ReentrantLock topicLock;
    private final ReentrantLock listenerLock;

    public RedisEventManager(@NotNull RedisConnector redis) {
        this.logger = Logger.getLogger("BotLogger");
        this.redis = redis;
        this.topicLock = new ReentrantLock();
        this.listenerLock = new ReentrantLock();
        this.mainTopic = redis.getConnection().getTopic("MainTopic");
    }

    public RedisEventManager(@NotNull RedisConnector redis, @NotNull String topic) {
        Conditions.checkForEmpty(topic);
        this.logger = Logger.getLogger("BotLogger");
        this.redis = redis;
        this.topicLock = new ReentrantLock();
        this.listenerLock = new ReentrantLock();
        this.mainTopic = redis.getConnection().getTopic(topic);
    }

    public <T extends REvent> void registerListener(Class<T> eventType, Consumer<T> consumer) {
        listenerLock.lock();
        try {
            List<Consumer<REvent>> listener = listeners.computeIfAbsent(eventType, k -> new ArrayList<>(5));
            listener.add((Consumer<REvent>) consumer);
        } finally {
            listenerLock.unlock();
        }

        subscribeEvent(eventType);
    }

    @Override
    public void unregisterListener(Consumer<? extends REvent> consumer) {
        listenerLock.lock();

        Class<? extends REvent> toUnsubscribe = null;
        try {
            for (Map.Entry<Class<? extends REvent>, List<Consumer<REvent>>> entry : listeners.entrySet()) {
                if (entry.getValue().removeIf(rEventConsumer -> rEventConsumer == consumer)) {
                    if (entry.getValue().isEmpty()) {
                        //We should unsubscribe the event
                        toUnsubscribe = entry.getKey();
                    }
                    break;
                }
            }

            if (toUnsubscribe != null) {
                listeners.remove(toUnsubscribe);
            }
        } finally {
            listenerLock.unlock();
        }

        if (toUnsubscribe == null)
            return;

        //Unsubscribe RTopic
        topicLock.lock();
        try {
            RTopic rTopic = topics.get(toUnsubscribe);
            rTopic.removeAllListeners();
            topics.remove(toUnsubscribe);
        } finally {
            topicLock.unlock();
        }
    }

    @Override
    public long callEvent(@NotNull REvent rEvent) {
        var publish = redis.getConnection().getTopic(rEvent.getTopic()).publish(rEvent);
        callEventOnMainChannel(rEvent);

        return publish;
    }

    @Override
    public RFuture<Long> callAsync(@NotNull REvent rEvent) {
        var longRFuture = redis.getConnection().getTopic(rEvent.getTopic()).publishAsync(rEvent);
        callEventOnMainChannel(rEvent);

        return longRFuture;
    }

    private void callEventOnMainChannel(REvent event) {
        executorService.execute(() -> {
            var map = OBJECT_MAPPER.convertValue(event, MAP_TOKEN);
            map.put("_topicName", event.getTopic());
            map.put("_date", System.currentTimeMillis());
            mainTopic.publish(map);
        });
    }

    private void subscribeEvent(Class<? extends REvent> eventType) {
        if (topics.containsKey(eventType))
            return;

        try {
            REvent rEvent = eventType.getConstructor().newInstance();

            RTopic topic = redis.getConnection().getTopic(rEvent.getTopic());
            topics.put(eventType, topic);

            topic.addListener(eventType, (channel, msg) -> {
                topicLock.lock();
                try {
                    List<Consumer<REvent>> listener = listeners.get(eventType);
                    if (listener != null && !listener.isEmpty()) {
                        for (Consumer<REvent> consumer : listener)
                            consumer.accept(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    topicLock.unlock();
                }
            });
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            //TODO: Check result in the log
            logger.warning(exception.getLocalizedMessage());
        }
    }
}
