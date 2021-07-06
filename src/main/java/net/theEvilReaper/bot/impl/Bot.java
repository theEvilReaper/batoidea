package net.theEvilReaper.bot.impl;

import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theEvilReaper.bot.api.BotState;
import net.theEvilReaper.bot.api.IBot;
import net.theEvilReaper.bot.api.interaction.BotInteraction;
import net.theEvilReaper.bot.api.provider.IChannelProvider;
import net.theEvilReaper.bot.api.provider.IClientProvider;
import net.theEvilReaper.bot.api.service.ServiceRegistry;
import net.theEvilReaper.bot.api.user.IUserIService;
import net.theEvilReaper.bot.config.BotConfig;
import net.theEvilReaper.bot.impl.user.UserIService;
import net.theEvilReaper.bot.service.ChannelProvider;
import net.theEvilReaper.bot.service.ClientProvider;
import net.theEvilReaper.bot.service.ServerRegistryImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class Bot implements IBot {

    private final Logger logger = Logger.getLogger("BotLogger");
    private LocalTeamspeakClientSocket socket;
    private final BotConfig botConfig;
    private final ServiceRegistry serviceRegistry;
    private final IChannelProvider channelProvider;
    private final IClientProvider clientProvider;
    private final IUserIService userService;

    private BotInteraction botInteraction;

    private volatile boolean stopping;

    private final Object stateLock = new Object();
    private BotState state = BotState.STOPPED;

    protected int botID;

    public Bot(@NotNull BotConfig botConfig) {
        this.botConfig = botConfig;
        this.serviceRegistry = new ServerRegistryImpl();
        this.channelProvider = new ChannelProvider();
        this.clientProvider = new ClientProvider();
        this.userService = new UserIService();
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
                e.printStackTrace();
            }
        }
    }

    protected void setState(BotState state) {
        synchronized (this.stateLock) {
            if (this.state != state) {
                logger.info("Change state " + this.state + " 1-> " + state);
                if (state == BotState.RUNNING) {
                    //this.started = new Date(System.currentTimeMillis());
                }
                this.state = state;
                this.stateLock.notifyAll();
            }
        }
    }

    @Override
    public void setBotInteraction(@NotNull BotInteraction botInteraction) {
        this.botInteraction = botInteraction;
    }

    @Override
    public boolean isConnected() {
        return socket != null && socket.isConnected();
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
    public IUserIService getUserService() {
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
}
