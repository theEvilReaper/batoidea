package net.theEvilReaper.batoidea.database;

import net.theEvilReaper.bot.api.database.RedisConnector;
import net.theEvilReaper.bot.api.database.RedisProcessor;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public abstract class AbstractRedisProcessor<K, V> implements RedisProcessor<K, V> {

    protected final RedisConnector redisConnector;

    protected AbstractRedisProcessor(RedisConnector redisConnector) {
        this.redisConnector = redisConnector;
    }
}
