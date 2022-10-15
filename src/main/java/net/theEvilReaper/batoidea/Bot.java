package net.theevilreaper.batoidea;

import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theevilreaper.batoidea.command.CommandManagerImpl;
import net.theevilreaper.batoidea.config.BotConfigImpl;
import net.theevilreaper.batoidea.interaction.InteractionFactory;
import net.theevilreaper.batoidea.property.PropertyEventDispatcher;
import net.theevilreaper.batoidea.provider.ChannelProvider;
import net.theevilreaper.batoidea.provider.ClientProvider;
import net.theevilreaper.batoidea.service.ServerRegistryImpl;
import net.theevilreaper.batoidea.user.TeamSpeakUser;
import net.theevilreaper.batoidea.user.UserService;
import net.theevilreaper.bot.api.BotState;
import net.theevilreaper.bot.api.IBot;
import net.theevilreaper.bot.api.command.CommandManager;
import net.theevilreaper.bot.api.config.BotConfig;
import net.theevilreaper.bot.api.database.IRedisEventManager;
import net.theevilreaper.bot.api.interaction.AbstractInteractionFactory;
import net.theevilreaper.bot.api.interaction.BotInteraction;
import net.theevilreaper.bot.api.property.PropertyEventCall;
import net.theevilreaper.bot.api.provider.IChannelProvider;
import net.theevilreaper.bot.api.provider.IClientProvider;
import net.theevilreaper.bot.api.service.ServiceRegistry;
import net.theevilreaper.bot.api.user.IUserService;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Bot implements IBot {

    private final UUID uuid = UUID.randomUUID();
    private LocalTeamspeakClientSocket socket;
    private final BotConfigImpl botConfig;
    private final ServiceRegistry serviceRegistry;
    private final IChannelProvider channelProvider;
    private final IClientProvider clientProvider;
    private final IUserService<TeamSpeakUser> userService;
    private final AbstractInteractionFactory interactionFactory;
    private final PropertyEventCall propertyEventCall;
    private final CommandManager commandManager;
    private BotInteraction botInteraction;
    private volatile boolean stopping;
    private final Object stateLock = new Object();
    private BotState state = BotState.STOPPED;
    protected int botID;
    private Date started;

    public Bot(@NotNull BotConfigImpl botConfig) {
        this.botConfig = botConfig;
        this.serviceRegistry = new ServerRegistryImpl();
        this.channelProvider = new ChannelProvider();
        this.clientProvider = new ClientProvider(socket);
        this.interactionFactory = new InteractionFactory(socket);
        this.userService = new UserService<>();
        this.propertyEventCall = new PropertyEventDispatcher(this);
        this.commandManager = new CommandManagerImpl();
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
                Logger.info("An error occurred when disconnecting from the server");
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void setState(@NotNull BotState state) {
        synchronized (this.stateLock) {
            if (this.state != state) {
                Logger.info("Change state {} -> {}", this.state, state);
                if (state == BotState.RUNNING) {
                   this.started = new Date(System.currentTimeMillis());
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
    public @NotNull BotState getState() {
        return state;
    }

    @Override
    public int getBotID() {
        return botID;
    }

    @Override
    public @NotNull IUserService getUserService() {
        return userService;
    }

    @Override
    public @NotNull IClientProvider getClientProvider() {
        return clientProvider;
    }

    @Override
    public @NotNull IChannelProvider getChannelProvider() {
        return channelProvider;
    }

    @Override
    public @NotNull ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    @Override
    public @NotNull BotInteraction getBotInteraction() {
        return botInteraction;
    }

    @Override
    public @NotNull AbstractInteractionFactory getInteractionFactory() {
        return interactionFactory;
    }

    @Override
    public @NotNull PropertyEventCall getPropertyEventCall() {
        return propertyEventCall;
    }

    @Override
    public IRedisEventManager getEventManager() {
        return null;
    }

    @Override
    public @NotNull CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public @NotNull BotConfig getConfig() {
        return botConfig;
    }

    @Override
    public @NotNull UUID getUUID() {
        return uuid;
    }
}