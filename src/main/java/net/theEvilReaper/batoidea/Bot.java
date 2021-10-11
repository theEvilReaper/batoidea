package net.theEvilReaper.batoidea;

import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theEvilReaper.batoidea.config.FileConfig;
import net.theEvilReaper.batoidea.interaction.InteractionFactory;
import net.theEvilReaper.batoidea.property.PropertyEventDispatcher;
import net.theEvilReaper.batoidea.service.ChannelProvider;
import net.theEvilReaper.batoidea.service.ClientProvider;
import net.theEvilReaper.batoidea.service.ServerRegistryImpl;
import net.theEvilReaper.batoidea.user.UserService;
import net.theEvilReaper.bot.api.BotState;
import net.theEvilReaper.bot.api.IBot;
import net.theEvilReaper.bot.api.database.IRedisEventManager;
import net.theEvilReaper.bot.api.interaction.AbstractInteractionFactory;
import net.theEvilReaper.bot.api.interaction.BotInteraction;
import net.theEvilReaper.bot.api.interaction.InteractionType;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
import net.theEvilReaper.bot.api.property.PropertyEventCall;
import net.theEvilReaper.bot.api.provider.IChannelProvider;
import net.theEvilReaper.bot.api.provider.IClientProvider;
import net.theEvilReaper.bot.api.service.ServiceRegistry;
import net.theEvilReaper.bot.api.user.IUserService;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class Bot implements IBot {

    private final Logger logger = Logger.getLogger("BotLogger");
    private LocalTeamspeakClientSocket socket;
    private final FileConfig botConfig;
    private final ServiceRegistry serviceRegistry;
    private final IChannelProvider channelProvider;
    private final IClientProvider clientProvider;
    private final IUserService userService;
    private final AbstractInteractionFactory interactionFactory;
    private final PropertyEventCall propertyEventCall;

    private BotInteraction botInteraction;

    private volatile boolean stopping;

    private final Object stateLock = new Object();
    private BotState state = BotState.STOPPED;

    protected int botID;

    public Bot(@NotNull FileConfig botConfig) {
        this.botConfig = botConfig;
        this.serviceRegistry = new ServerRegistryImpl();
        this.channelProvider = new ChannelProvider();
        this.clientProvider = new ClientProvider(logger, null);

        //TODO: Fix npe
        this.interactionFactory = new InteractionFactory(socket);
        this.userService = new UserService(interactionFactory.getInteraction(InteractionType.CLIENT, UserInteraction.class));
        this.propertyEventCall = new PropertyEventDispatcher(this);
    }

    @Override
    public void connect() {
        setState(BotState.STARTING);




        setState(BotState.RUNNING);
    }

    @Override
    public void disconnect() {
        if (socket.isConnected()) {
            try {
                socket.disconnect();
            } catch (IOException | TimeoutException | ExecutionException | InterruptedException e) {
                logger.info("An error occurred when disconnecting from the server");
            }
        }
    }

    @Override
    public void setState(@NotNull BotState state) {
        synchronized (this.stateLock) {
            if (this.state != state) {
                logger.info("Change state " + this.state + " -> " + state);
                if (state == BotState.RUNNING) {
                   // this.started = new Date(System.currentTimeMillis());
                }
                this.state = state;
                this.stateLock.notifyAll();
            }
        }
    }

    @Override
    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public BotState getState() {
        return state;
    }

    @Override
    public int getBotID() {
        return botID;
    }

    @Override
    public IUserService getUserService() {
        return userService;
    }

    @Override
    public IClientProvider getClientProvider() {
        return clientProvider;
    }

    @Override
    public IChannelProvider getChannelProvider() {
        return channelProvider;
    }

    @Override
    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    @Override
    public BotInteraction getBotInteraction() {
        return botInteraction;
    }

    @Override
    public AbstractInteractionFactory getInteractionFactory() {
        return interactionFactory;
    }

    @Override
    public PropertyEventCall getPropertyEventCall() {
        return propertyEventCall;
    }

    @Override
    public IRedisEventManager getEventManager() {
        return null;
    }

    public FileConfig getBotConfig() {
        return botConfig;
    }
}