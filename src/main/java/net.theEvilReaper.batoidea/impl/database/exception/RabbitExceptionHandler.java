package net.theEvilReaper.batoidea.impl.database.exception;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.ExceptionHandler;
import com.rabbitmq.client.TopologyRecoveryException;

import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class RabbitExceptionHandler implements ExceptionHandler {

    private final Logger logger;

    public RabbitExceptionHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handleUnexpectedConnectionDriverException(Connection conn, Throwable exception) {

    }

    @Override
    public void handleReturnListenerException(Channel channel, Throwable exception) {

    }

    @Override
    public void handleConfirmListenerException(Channel channel, Throwable exception) {

    }

    @Override
    public void handleBlockedListenerException(Connection connection, Throwable exception) {

    }

    @Override
    public void handleConsumerException(Channel channel, Throwable exception, Consumer consumer, String consumerTag,
                                        String methodName) {
        System.out.println(" ");
        System.out.println(" -------------------------------------- ");
        System.out.println(channel.getChannelNumber());
        exception.printStackTrace();
        System.out.println(consumerTag);
        System.out.println(methodName);
        System.out.println(" ");
        System.out.println(" -------------------------------------- ");
    }

    @Override
    public void handleConnectionRecoveryException(Connection conn, Throwable exception) {

    }

    @Override
    public void handleChannelRecoveryException(Channel ch, Throwable exception) {

    }

    @Override
    public void handleTopologyRecoveryException(Connection conn, Channel ch, TopologyRecoveryException exception) {

    }
}
