package net.theevilreaper.batoidea;

import com.github.manevolent.ts3j.api.Channel;
import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.protocol.TS3DNS;
import com.github.manevolent.ts3j.protocol.client.ClientConnectionState;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theevilreaper.batoidea.command.CommandManagerImpl;
import net.theevilreaper.batoidea.command.commands.ExitCommand;
import net.theevilreaper.batoidea.command.commands.HelpCommand;
import net.theevilreaper.batoidea.command.commands.PongCommand;
import net.theevilreaper.batoidea.config.BotConfigImpl;
import net.theevilreaper.batoidea.config.ConfigurationProvider;
import net.theevilreaper.batoidea.terminal.Terminal;
import net.theevilreaper.batoidea.identity.BatoideaIdentity;
import net.theevilreaper.batoidea.interaction.BatoideaInteraction;
import net.theevilreaper.batoidea.interaction.InteractionFactory;
import net.theevilreaper.batoidea.listener.TeamSpeakListener;
import net.theevilreaper.batoidea.property.PropertyEventDispatcher;
import net.theevilreaper.batoidea.provider.ChannelProvider;
import net.theevilreaper.batoidea.provider.ClientProvider;
import net.theevilreaper.batoidea.service.ServerRegistryImpl;
import net.theevilreaper.batoidea.listener.ClientListener;
import net.theevilreaper.batoidea.user.TeamSpeakUser;
import net.theevilreaper.batoidea.user.UserService;
import net.theevilreaper.bot.api.BotState;
import net.theevilreaper.bot.api.IBot;
import net.theevilreaper.bot.api.command.CommandManager;
import net.theevilreaper.bot.api.config.BotConfig;
import net.theevilreaper.bot.api.database.IRedisEventManager;
import net.theevilreaper.bot.api.identity.Identity;
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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class Batoidea implements IBot {

    private static final int BOT_GROUP = 1149;
    private static final int BOT_MUSIC_GROUP = 1144;

    public static LocalTeamspeakClientSocket teamspeakClient;
    private int botID;
    private volatile boolean stopping;
    private final Object stateLock = new Object();
    private final ServiceRegistry serviceRegistry;
    private final IChannelProvider channelProvider;
    private final IUserService<TeamSpeakUser> userService;
    private final Identity identity;
    private final BotConfigImpl fileConfig;
    private final PropertyEventCall propertyEventCall;
    private final CommandManagerImpl commandManager;
    private final UUID uuid = UUID.randomUUID();
    private final Terminal terminal;
    private AbstractInteractionFactory interactionFactory;
    private BotState state = BotState.STOPPED;
    private Date started;
    private BotInteraction botInteraction;
    private IClientProvider clientProvider;
    private BotConfig config;

    public Batoidea() {
        this.fileConfig = new BotConfigImpl(Paths.get(System.getProperty("user.dir")));
        this.fileConfig.load();
        this.identity = new BatoideaIdentity(25);
        this.serviceRegistry = new ServerRegistryImpl();
        this.userService = new UserService<>();
        this.channelProvider = new ChannelProvider();
        this.propertyEventCall = new PropertyEventDispatcher(this);
        this.commandManager = new CommandManagerImpl();
        this.terminal = new Terminal(this.commandManager);
        this.terminal.startTerminal();
        this.config = new ConfigurationProvider().getBotConfig();
        connect();
    }

    @Override
    public void connect() throws IllegalAccessError {
        if (teamspeakClient != null) return;
        synchronized (stateLock) {
            if (getState() != BotState.STOPPED) throw new IllegalStateException(state.name());

            setState(BotState.STARTING);

            teamspeakClient = new LocalTeamspeakClientSocket();
            teamspeakClient.setNickname(this.fileConfig.getName());
            teamspeakClient.setIdentity(this.identity.getIdentity());
            teamspeakClient.setHWID("Windows");
            teamspeakClient.setExceptionHandler(throwable ->
                    Logger.warn("There is an error: {}", throwable));

            setState(BotState.CONNECTING);

            try {
                var lookup = TS3DNS.lookup("trainingsoase.net");
                teamspeakClient.connect(lookup.get(0).getHostName(), 5000L);
                var address = new InetSocketAddress(InetAddress.getByName(this.fileConfig.getServer()), 9987);
                teamspeakClient.connect(address, "", this.fileConfig.getConnectionTimeout());
                Logger.info("Waiting for the connected state");
                teamspeakClient.waitForState(ClientConnectionState.CONNECTED, 5000L);
            } catch (IOException | TimeoutException | InterruptedException e) {
                Logger.info("Can't connect to the given host. Check IP");
                Thread.currentThread().interrupt();
            }

            Logger.info("Successfully connected to the server");

            try {
                teamspeakClient.subscribeAll();
            } catch (IOException | CommandException | TimeoutException | InterruptedException e) {
                Logger.info("Unable to subscribe the channels. " +
                        "Please check the permissions of the group which the bot currently owns");
                Thread.currentThread().interrupt();
            }

            try {
                teamspeakClient.setDescription("I am only a bot. Don't trust me. Best regards your bot <3");
            } catch (CommandException | IOException | ExecutionException | InterruptedException | TimeoutException e) {
                Logger.info("Unable to set the description of the bot");
                Thread.currentThread().interrupt();
            }

            try {
                teamspeakClient.joinChannel(this.fileConfig.getDefaultChannel(),"");
            } catch (CommandException e) {
                Logger.info("There is no channel that matches with the channel id. Joining base channel");
            } catch (IOException | InterruptedException | TimeoutException e) {
                Logger.info("Unable to join default channel. Check the given id in the config file");
                Thread.currentThread().interrupt();
            }


            botID = teamspeakClient.getClientId();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                stopping = true;
                terminal.stopTerminal();
            }));

            setState(BotState.RUNNING);

            this.clientProvider = new ClientProvider(teamspeakClient);
            this.interactionFactory = new InteractionFactory(teamspeakClient);

            this.botInteraction = new BatoideaInteraction(teamspeakClient, botID);
            onLoad();
            teamspeakClient.addListener(new TeamSpeakListener(this, commandManager, userService));
            teamspeakClient.addListener(new ClientListener(clientProvider, userService, botID));
            registerCommands();
        }

        while (!stopping) {
            Thread.onSpinWait();
        }
    }

    private void registerCommands() {
        commandManager.register(new PongCommand());
        commandManager.register(new ExitCommand(this));
        commandManager.register(new HelpCommand());
    }

    protected void onLoad() {
        if (!teamspeakClient.isConnected()) {
            disconnect();
            return;
        }

        try {
            for (Client client : teamspeakClient.listClients()) {
                var current = teamspeakClient.getClientInfo(client.getId());
                if (current.isInServerGroup(BOT_GROUP) || current.isInServerGroup(BOT_MUSIC_GROUP)) {
                    Logger.info("Ignoring client: {} because it is a bot", client.getNickname());
                    continue;
                }
                clientProvider.add(client);
            }

            for (Channel channel : teamspeakClient.listChannels()) {
                channelProvider.add(channel);
            }
        } catch (IOException | TimeoutException | InterruptedException | CommandException e) {
            Logger.warn("Unable to fetch updates");
            Thread.currentThread().interrupt();
        }

        Logger.info("Added {} channels", channelProvider.getChannels().size());
        Logger.info("Added {} Clients", clientProvider.getClients().size());
    }

    @Override
    public void disconnect() {
        synchronized (stateLock) {
            if (getState() != BotState.RUNNING) throw new IllegalStateException(state.name());

            Logger.info("Shutting down...");

            setState(BotState.STOPPING);

            if (teamspeakClient.isConnected()) {
                try {
                    teamspeakClient.disconnect("Requested disconnect via console! (no)");
                } catch (IOException | TimeoutException | ExecutionException | InterruptedException exception) {
                    exception.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }

            setState(BotState.STOPPED);
            Logger.info("Shutdown complete");
        }
        System.exit(0);
    }

    @Override
    public boolean isConnected() {
        return teamspeakClient.isConnected();
    }

    @Override
    public boolean isStopping() {
        return stopping;
    }

    @Override
    public void setState(@NotNull BotState state) {
        if (state == this.state) return;
        synchronized (this.stateLock) {
            Logger.info("Change state {} -> {}", this.state, state);
            if (state == BotState.RUNNING) {
                this.started = new Date(System.currentTimeMillis());
            }
            this.state = state;
            this.stateLock.notifyAll();
        }
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
    public @NotNull BotInteraction getBotInteraction() {
        return botInteraction;
    }

    @Override
    public @NotNull IUserService<TeamSpeakUser> getUserService() {
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

    public Date getStarted() {
        return started;
    }

    @Override
    public @NotNull AbstractInteractionFactory getInteractionFactory() {
        return interactionFactory;
    }

    @Override
    public IRedisEventManager getEventManager() {
        return null;
    }

    /**
     * Returns the implementation for the {@link PropertyEventCall}.
     * @return the given instance
     */
    @Override
    public @NotNull PropertyEventCall getPropertyEventCall() {
        return propertyEventCall;
    }

    /**
     * Returns the implementation from the {@link CommandManager}.
     * @return the given instance
     */
    @Override
    public @NotNull CommandManagerImpl getCommandManager() {
        return commandManager;
    }

    @Override
    public @NotNull BotConfig getConfig() {
        return config;
    }

    @Override
    public @NotNull UUID getUUID() {
        return uuid;
    }
}