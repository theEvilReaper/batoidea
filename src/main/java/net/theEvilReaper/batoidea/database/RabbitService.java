package net.theEvilReaper.batoideas.database;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import net.theEvilReaper.batoideas.database.exception.RabbitExceptionHandler;
import net.theEvilReaper.batoideas.database.pojo.ServiceUpdate;
import net.theEvilReaper.bot.api.Connectable;
import net.theEvilReaper.bot.api.config.Config;
import net.theEvilReaper.bot.api.database.broker.Publish;
import net.theEvilReaper.bot.api.database.broker.Subscribe;
import net.theEvilReaper.bot.api.service.Service;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class RabbitService extends Service implements Connectable, Subscribe, Publish {

    private static final String SYSTEM_EXCHANGE = "system";

    private final Logger logger = Logger.getLogger("BotLogger");

    private final Config config;

    private Connection publishConnection;
    private Connection subscribeConnection;

    private Channel subscribeChannel;
    private Channel publishChannel;

    private Properties properties;

    public RabbitService(Config config) {
        super("RabbitService", -1);
        this.config = config;

       /* if (config.getString("host") == null) {
            throw new ValueException("host", "The given host can not be null");
        }

        if (config.getString("virtualhost") == null) {
            throw new ValueException("virtualhost", "The given virtualhost can not be null");
        }

        if (config.getString("username") == null) {
            throw new ValueException("username", "The given username can not be null");
        }*/
    }

    @Override
    public void connect()  {
       /* var factory = new ConnectionFactory();

        factory.setUsername(config.getString("username"));
        factory.setPassword(config.getString("password"));
        factory.setVirtualHost(config.getString("virtualhost"));
        factory.setHost(config.getString("host"));
        factory.setPort(config.getInt("port"));
        factory.setAutomaticRecoveryEnabled(true);
        factory.setExceptionHandler(new RabbitExceptionHandler(logger));
        factory.setNetworkRecoveryInterval(2);*/

        ConnectionFactory factory = new ConnectionFactory();

        factory.setUsername("api");
        factory.setPassword("ud6Hh2Q4B9rUP0OG");
        factory.setVirtualHost("messaging");
        factory.setHost("10.0.0.11");
        factory.setAutomaticRecoveryEnabled(true);
        factory.setExceptionHandler(new RabbitExceptionHandler(logger));
        factory.setNetworkRecoveryInterval(2);
        factory.setPort(5672);

        reconnect:
        try {
            publishConnection = factory.newConnection();
            subscribeConnection = factory.newConnection();
            logger.info("[RabbitMQ] Verbindung hergestellt");
        } catch (IOException | TimeoutException exception) {
            exception.printStackTrace();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break reconnect;
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (subscribeConnection != null) {
            subscribe(bytes -> {
                var update = ServiceUpdate.deserialize(bytes);

                logger.info(update.getUpdateAction() + ", " + update.getObject().toString());
            });
        }

        if (publishConnection != null) {
            publish(ServiceUpdate.ofSupport(Integer.toString(12)).serialize());
        }
    }

    @Override
    public void disconnect() {
        try {
            if (publishConnection.isOpen()) {
                logger.info("Closing publish channel / connection");
                publishConnection.close();
                publishChannel.close();
            }

            if (subscribeChannel.isOpen()) {
                logger.info("Closing subscribe channel / connection");
                subscribeChannel.close();
                subscribeConnection.close();
            }
        } catch (Exception exception) {
            logger.warning("Error when disconnecting from redis");
        }
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    protected void update() {

    }

    @Override
    public void subscribe(Consumer<byte[]> consumer) {
        try {
            if (subscribeChannel == null) {
                logger.log(Level.INFO, "Creating new channel for the incoming messages over rabbit");
                subscribeChannel = subscribeConnection.createChannel();
            }
            subscribeChannel.basicQos(100);
            subscribeChannel.exchangeDeclare(SYSTEM_EXCHANGE, "fanout");

            var queueName = subscribeChannel.queueDeclare().getQueue();

            subscribeChannel.queueBind(queueName, SYSTEM_EXCHANGE, "");
            subscribeChannel.addShutdownListener(exception -> {
                logger.warning("Reason: " + exception.getReason());
                logger.warning("Error: " + exception.getMessage());
            });

            DeliverCallback deliverCallback = ((consumerTag, delivery) -> consumer.accept(delivery.getBody()));

            subscribeChannel.basicConsume(queueName, true, deliverCallback, t -> {});
        } catch (IOException exception) {
            logger.log(Level.WARNING, "An error occurred when handling subscribe logic (Rabbit)");
        }
    }

    @Override
    public void publish(byte[] data) {
        try {
            if(publishChannel == null) {
                publishChannel = publishConnection.createChannel();
            }
            publishChannel.basicQos(100);
            publishChannel.exchangeDeclare(SYSTEM_EXCHANGE, "fanout");
            publishChannel.basicPublish(SYSTEM_EXCHANGE, "", null, data);
            publishChannel.addShutdownListener(cause -> {
                System.out.println("Reason:" + cause.getReason());
                System.out.println("ErrorMessage:" + cause.getMessage());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
