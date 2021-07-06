package net.theEvilReaper.bot.impl.database;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import net.theEvilReaper.bot.api.Connectable;
import net.theEvilReaper.bot.api.config.Config;
import net.theEvilReaper.bot.api.database.broker.Publish;
import net.theEvilReaper.bot.api.database.broker.Subscribe;
import net.theEvilReaper.bot.api.exception.ValueException;
import net.theEvilReaper.bot.api.service.Service;
import net.theEvilReaper.bot.impl.database.exception.RabbitExceptionHandler;
import org.jetbrains.annotations.NotNull;

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

    private final Logger logger = Logger.getLogger("BotLogger");

    private final Config config;

    private Connection publishConnection;
    private Connection subscribeConnection;

    private Channel subscribeChannel;
    private Channel publishChannel;

    private Properties properties;

    public RabbitService(Config config) throws ValueException {
        super("RabbitService", -1);
        this.config = config;

        if (config.getString("host") == null) {
            throw new ValueException("host", "The given host can not be null");
        }

        if (config.getString("virtualhost") == null) {
            throw new ValueException("virtualhost", "The given virtualhost can not be null");
        }

        if (config.getString("username") == null) {
            throw new ValueException("username", "The given username can not be null");
        }
    }

    @Override
    public void connect()  {
        var factory = new ConnectionFactory();

        factory.setUsername(config.getString("username"));
        factory.setPassword(config.getString("password"));
        factory.setVirtualHost(config.getString("virtualhost"));
        factory.setHost(config.getString("host"));
        factory.setPort(config.getInt("port"));
        factory.setAutomaticRecoveryEnabled(true);
        factory.setExceptionHandler(new RabbitExceptionHandler(logger));
        factory.setNetworkRecoveryInterval(2);

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
    public void subscribe(@NotNull String exchange, Consumer<byte[]> consumer) {
        if (exchange.trim().isEmpty()) {
            throw new IllegalArgumentException("The given channel can not be empty");
        }

        try {

            if (subscribeChannel == null) {
                logger.log(Level.INFO, "Creating new channel for the incoming messages over rabbit");
                subscribeChannel = subscribeConnection.createChannel();
            }
            subscribeChannel.basicQos(100);
            subscribeChannel.exchangeDeclare(exchange, "fanout");

            var queueName = subscribeChannel.queueDeclare().getQueue();

            subscribeChannel.queueBind(queueName, exchange, "");
            subscribeChannel.addShutdownListener(exception -> {
                logger.warning("Reason: " + exception.getReason());
                logger.warning("Error: " + exception.getMessage());
            });

            subscribeChannel.close();
        } catch (IOException exception) {
            logger.log(Level.WARNING, "There is an error occurred when handling subscribe logic (Rabbit)");
        } catch (TimeoutException e) {
            logger.warning("Error when closing channel. Client has an timeout");
        }
    }

    @Override
    public void publish(@NotNull String exchange, byte[] data) {
        try {
            if(publishChannel == null) {
                publishChannel = publishConnection.createChannel();
            }
            publishChannel.basicQos(100);
            publishChannel.exchangeDeclare(exchange, "fanout");
            publishChannel.basicPublish(exchange, "", null, data);
            publishChannel.addShutdownListener(cause -> {
                System.out.println("Reason:" + cause.getReason());
                System.out.println("ErrorMessage:" + cause.getMessage());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
