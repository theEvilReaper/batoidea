package net.theevilreaper.batoidea.database;

import net.theevilreaper.bot.api.database.RedisConnector;
import net.theevilreaper.bot.api.database.RedisProcessor;
import net.theevilreaper.bot.api.database.model.RedisModel;
import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public abstract class AbstractRedisProcessor<K, V extends RedisModel> implements RedisProcessor<K, V> {

    protected final RedisConnector redisConnector;

    protected AbstractRedisProcessor(@NotNull RedisConnector redisConnector) {
        this.redisConnector = redisConnector;
    }
}
