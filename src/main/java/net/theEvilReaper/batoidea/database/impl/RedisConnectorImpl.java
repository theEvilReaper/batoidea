package net.theEvilReaper.batoidea.database.impl;

import net.theEvilReaper.bot.api.database.RedisConnector;
import org.apache.commons.lang.SystemUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;

/**
 * The implementation for the {@link RedisConnector}.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class RedisConnectorImpl implements RedisConnector {

    private RedissonClient redissonClient;

    private final String server;
    private final String password;
    private final int database;
    private final boolean epoll;

    /**
     * Creates a new instance from the {@link RedisConnector} with the given values.
     * @param server The host for the server
     * @param password The password from the server
     * @param database The database where the client should be use
     * @param epoll Determines if the client use the epoll network socket or not
     */

    public RedisConnectorImpl(@NotNull String server, @Nullable String password, int database, boolean epoll) {
        this.server = server;
        this.password = password;
        this.database = database;
        this.epoll = epoll;
        this.connect();
    }

    /**
     * Creates a new instance from the {@link RedisConnector} with the given values.
     * @param server The host for the server
     * @param password The password from the server
     * @param database The database where the client should be use
     */

    public RedisConnectorImpl(@NotNull String server, @Nullable String password, int database) {
        this.server = server;
        this.password = password;
        this.database = database;
        this.epoll = SystemUtils.IS_OS_UNIX;
        this.connect();
    }

    /**
     * Connects to the Redis server with the given data.
     */

    @Override
    public void connect() {
        if (isConnected()) {
            throw new IllegalArgumentException("Can't connect because there is an valid connection");
        }

        var config = new Config();
        config.useSingleServer()
                .setAddress(server)
                .setDatabase(database)
                .setConnectionMinimumIdleSize(5)
                .setConnectionPoolSize(6)
                .setClientName("OaseAPI");
        config.setCodec(new JsonJacksonCodec());
        config.setNettyThreads(4);

        config.setTransportMode(epoll ? TransportMode.EPOLL : TransportMode.NIO);

        if (password != null && !password.trim().isEmpty()) {
            config.useSingleServer().setPassword(password);
        }

        redissonClient = Redisson.create(config);

        if (redissonClient.getBucket("test") == null) {
            throw new NullPointerException("Could not connect to Redis");
        }
    }

    /**
     * Disconnects the client from the server.
     */

    @Override
    public void disconnect() {
        if (!isConnected()) {
            throw new IllegalStateException("Can not close connection because it's null");
        }
        redissonClient.shutdown();
    }

    /**
     * Returns a boolean if the connector has an valid connection.
     * @return True when there is a connector otherwise false
     */

    @Override
    public boolean isConnected() {
        return redissonClient != null;
    }

    /**
     * Returns the given instance from the {@link RedissonClient}.
     * @return the underlying instance
     */

    @Override
    @NotNull
    public RedissonClient getConnection() {
        return redissonClient;
    }
}
