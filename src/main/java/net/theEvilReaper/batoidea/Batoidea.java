package net.theEvilReaper.batoidea;

import com.github.manevolent.ts3j.api.Channel;
import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.protocol.TS3DNS;
import com.github.manevolent.ts3j.protocol.client.ClientConnectionState;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import io.javalin.Javalin;
import net.theEvilReaper.batoidea.command.CommandManagerImpl;
import net.theEvilReaper.batoidea.command.commands.ExitCommand;
import net.theEvilReaper.batoidea.command.commands.HelpCommand;
import net.theEvilReaper.batoidea.command.commands.PongCommand;
import net.theEvilReaper.batoidea.config.BotConfigImpl;
import net.theEvilReaper.batoidea.terminal.Terminal;
import net.theEvilReaper.batoidea.identity.BatoideaIdentity;
import net.theEvilReaper.batoidea.interaction.BatoideaInteraction;
import net.theEvilReaper.batoidea.interaction.InteractionFactory;
import net.theEvilReaper.batoidea.listener.TeamSpeakListener;
import net.theEvilReaper.batoidea.property.PropertyEventDispatcher;
import net.theEvilReaper.batoidea.provider.ChannelProvider;
import net.theEvilReaper.batoidea.provider.ClientProvider;
import net.theEvilReaper.batoidea.service.ServerRegistryImpl;
import net.theEvilReaper.batoidea.listener.ClientListener;
import net.theEvilReaper.batoidea.user.UserService;
import net.theEvilReaper.bot.api.BotState;
import net.theEvilReaper.bot.api.IBot;
import net.theEvilReaper.bot.api.command.CommandManager;
import net.theEvilReaper.bot.api.database.IRedisEventManager;
import net.theEvilReaper.bot.api.identity.Identity;
import net.theEvilReaper.bot.api.interaction.AbstractInteractionFactory;
import net.theEvilReaper.bot.api.interaction.BotInteraction;
import net.theEvilReaper.bot.api.property.PropertyEventCall;
import net.theEvilReaper.bot.api.provider.IChannelProvider;
import net.theEvilReaper.bot.api.provider.IClientProvider;
import net.theEvilReaper.bot.api.service.ServiceRegistry;
import net.theEvilReaper.bot.api.user.IUserService;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class Batoidea implements IBot {

    private static final int BOT_GROUP = 1149;
    private static final int BOT_MUSIC_GROUP = 1144;

    private int botID;

    public static LocalTeamspeakClientSocket teamspeakClient;

    private volatile boolean stopping;

    private final Object stateLock = new Object();
    private final Logger logger;
    private final ServiceRegistry serviceRegistry;
    private final IChannelProvider channelProvider;
    private final IUserService userService;
    private final Identity identity;
    private final BotConfigImpl fileConfig;
    private final PropertyEventCall propertyEventCall;
    private final CommandManagerImpl commandManager;
    private final Terminal terminal;


    private AbstractInteractionFactory interactionFactory;

    private BotState state = BotState.STOPPED;
    private Date started;

    private BotInteraction botInteraction;
    private IClientProvider clientProvider;

    private IRedisEventManager iRedisEventManager;

    public Batoidea(Logger logger) {
        this.logger = logger;
        this.fileConfig = new BotConfigImpl(Paths.get(System.getProperty("user.dir")));
        this.fileConfig.load();
        this.identity = new BatoideaIdentity(25);
        this.serviceRegistry = new ServerRegistryImpl();
        //TODO: FIX NPE
        this.userService = new UserService(null);
        this.channelProvider = new ChannelProvider();
        this.propertyEventCall = new PropertyEventDispatcher(this);
        this.commandManager = new CommandManagerImpl();
        this.terminal = new Terminal(this.commandManager);
        this.terminal.startTerminal();
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
                    logger.log(Level.WARNING, "There is an error: ", throwable));

            setState(BotState.CONNECTING);

            try {
                var lookup = TS3DNS.lookup("trainingsoase.net");
                teamspeakClient.connect(lookup.get(0).getHostName(), 5000L);
                var address = new InetSocketAddress(InetAddress.getByName(this.fileConfig.getServer()), 9987);
                teamspeakClient.connect(address, "", this.fileConfig.getConnectionTimeout());
                logger.info("Waiting for the connected state");
                teamspeakClient.waitForState(ClientConnectionState.CONNECTED, 5000L);
            } catch (IOException | TimeoutException | InterruptedException e) {
                logger.info("Can't connect to the given host. Check IP");
                //System.exit(0);
            }

            logger.info("Successfully connected to the server");

            try {
                teamspeakClient.subscribeAll();
            } catch (IOException | CommandException | TimeoutException | InterruptedException e) {
                logger.info("Unable to subscribe the channels. " +
                        "Please check the permissions of the group which the bot currently owns");
            }

            try {
                teamspeakClient.setDescription("I am only a bot. Don't trust me. Best regards your bot <3");
            } catch (CommandException | IOException | ExecutionException | InterruptedException | TimeoutException e) {
                logger.info("Unable to set the description of the bot");
            }

            try {
                teamspeakClient.joinChannel(this.fileConfig.getDefaultChannel(),"");
            } catch (CommandException e) {
                logger.info("There is no channel that matches with the channel id. Joining base channel");
            } catch (IOException | InterruptedException | TimeoutException e) {
                logger.info("Unable to join default channel. Check the given id in the config file");
            }


            botID = teamspeakClient.getClientId();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                stopping = true;
                terminal.stopTerminal();
            }));

            setState(BotState.RUNNING);

            this.clientProvider = new ClientProvider(logger, teamspeakClient);
            this.interactionFactory = new InteractionFactory(teamspeakClient);

            this.botInteraction = new BatoideaInteraction(teamspeakClient, botID);
            onLoad();
            teamspeakClient.addListener(new TeamSpeakListener(this, commandManager, userService));
            teamspeakClient.addListener(new ClientListener(clientProvider, userService, logger, botID));
            registerCommands();
        }

        Javalin.createStandalone().get("/", context -> {});

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
        /*if (!getTeamspeakClient().isConnected()) {
            disconnect();
            return;
        }*/

        try {
            for (Client client : teamspeakClient.listClients()) {
                var current = teamspeakClient.getClientInfo(client.getId());
                if (current.isInServerGroup(BOT_GROUP)) {
                    logger.info("Ignoring client: " + client.getNickname() + " because it is a bot");
                    continue;
                }

                if (current.isInServerGroup(BOT_MUSIC_GROUP)) {
                    logger.info("Ignoring client: " + client.getNickname() + " because it is a bot");
                    continue;
                }

                clientProvider.add(client);
            }

            for (Channel channel : teamspeakClient.listChannels()) {
                channelProvider.add(channel);
            }
        } catch (IOException | TimeoutException | InterruptedException | CommandException e) {
            logger.warning("Unable to fetch updates");
        }

        logger.info("Added " + channelProvider.getChannels().size() + " Channels");
        logger.info("Added " + clientProvider.getSize() + " Clients");
    }

    @Override
    public void disconnect() {
        synchronized (stateLock) {
            if (getState() != BotState.RUNNING) throw new IllegalStateException(state.name());

            logger.info("Shutting down...");

            setState(BotState.STOPPING);

            if (teamspeakClient.isConnected()) {
                try {
                    teamspeakClient.disconnect("Requested disconnect via console! (no)");
                } catch (IOException | TimeoutException | ExecutionException | InterruptedException exception) {
                    exception.printStackTrace();
                }
            }

            setState(BotState.STOPPED);
            logger.info("Shutdown complete");
        }
        System.exit(0);
    }

    @Override
    public boolean isConnected() {
        return teamspeakClient.isConnected();
    }

    public boolean isStopping() {
        return stopping;
    }

    @Override
    public void setState(@NotNull BotState state) {
        if (state == this.state) return;
        synchronized (this.stateLock) {
            logger.info("Change state " + this.state + " -> " + state);
            if (state == BotState.RUNNING) {
                this.started = new Date(System.currentTimeMillis());
            }
            this.state = state;
            this.stateLock.notifyAll();
        }
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
    public BotInteraction getBotInteraction() {
        return botInteraction;
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
        return null;
    }

    @Override
    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    public Date getStarted() {
        return started;
    }

    @Override
    public AbstractInteractionFactory getInteractionFactory() {
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
    public PropertyEventCall getPropertyEventCall() {
        return propertyEventCall;
    }

    /**
     * Returns the implementation from the {@link CommandManager}.
     * @return the given instance
     */

    @Override
    public CommandManagerImpl getCommandManager() {
        return commandManager;
    }
}