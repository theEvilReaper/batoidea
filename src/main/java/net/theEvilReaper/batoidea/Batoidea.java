package net.theEvilReaper.batoidea;

import com.github.manevolent.ts3j.api.Channel;
import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.identity.LocalIdentity;
import com.github.manevolent.ts3j.protocol.TS3DNS;
import com.github.manevolent.ts3j.protocol.client.ClientConnectionState;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theEvilReaper.batoidea.console.BotConsoleService;
import net.theEvilReaper.batoidea.database.RabbitService;
import net.theEvilReaper.batoidea.interaction.BatoideaInteraction;
import net.theEvilReaper.batoidea.interaction.ClientInteraction;
import net.theEvilReaper.batoidea.interaction.InteractionFactory;
import net.theEvilReaper.batoidea.listener.TeamSpeakListener;
import net.theEvilReaper.batoidea.logging.BotLogger;
import net.theEvilReaper.batoidea.service.ChannelProvider;
import net.theEvilReaper.batoidea.service.ClientProvider;
import net.theEvilReaper.batoidea.service.ServerRegistryImpl;
import net.theEvilReaper.batoidea.service.SupportService;
import net.theEvilReaper.batoidea.service.listener.ClientListener;
import net.theEvilReaper.batoidea.user.UserService;
import net.theEvilReaper.bot.api.BotState;
import net.theEvilReaper.bot.api.IBot;
import net.theEvilReaper.bot.api.interaction.AbstractInteractionFactory;
import net.theEvilReaper.bot.api.interaction.BotInteraction;
import net.theEvilReaper.bot.api.provider.IChannelProvider;
import net.theEvilReaper.bot.api.provider.IClientProvider;
import net.theEvilReaper.bot.api.service.ServiceRegistry;
import net.theEvilReaper.bot.api.user.IUserService;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class Batoidea implements IBot {

    private static final int BOT_GROUP = 1149;
    private static final int BOT_MUSIC_GROUP = 1144;

    public static final Pattern SPLIT_PATTERN = Pattern.compile(" ");


    private int botID;

    public static LocalTeamspeakClientSocket teamspeakClient;

    private volatile boolean stopping;

    private final Object stateLock = new Object();
    private final Logger logger;
    private final ServiceRegistry serviceRegistry;
    private final IChannelProvider channelProvider;
    private final IUserService userService;
    private AbstractInteractionFactory interactionFactory;

    private BotState state = BotState.STOPPED;
    private Date started;

    private BotInteraction botInteraction;
    private IClientProvider clientProvider;
    private SupportService supportService;

    public Batoidea() {
        var botLogger = new BotLogger();
        this.logger = Logger.getLogger("BotLogger");
        this.serviceRegistry = new ServerRegistryImpl();
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$td.%1$tm.%1$ty %1$tH:%1$tM:%1$tS] %4$s: %5$s%n");

        logger.info("\n" +
                "  ____                 ____        _   \n" +
                " / __ \\               |  _ \\      | |  \n" +
                "| |  | | __ _ ___  ___| |_) | ___ | |_ \n" +
                "| |  | |/ _` / __|/ _ \\  _ < / _ \\| __|\n" +
                "| |__| | (_| \\__ \\  __/ |_) | (_) | |_ \n" +
                " \\____/ \\__,_|___/\\___|____/ \\___/ \\__|\n" +
                "                                       ");
        logger.info("I am only an test bot. I have bugs lol");
        this.userService = new UserService();
        this.channelProvider = new ChannelProvider();
        connect();
        new RabbitService(null);
    }

    @Override
    public void connect() throws IllegalAccessError {
        this.supportService = new SupportService(null, 8420);
        if (teamspeakClient != null) return;
        synchronized (stateLock) {
            if (getState() != BotState.STOPPED) throw new IllegalStateException(state.name());

            setState(BotState.STARTING);

            teamspeakClient = new LocalTeamspeakClientSocket();

            String identity = "ReaperBot";
            int identityLevel = 15;

            LocalIdentity localIdentity = null;

            File file = new File("./config.txt");

            if (!file.exists()) {
                try {
                    localIdentity = LocalIdentity.generateNew(identityLevel);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }

                try {
                    localIdentity.save(new File("./config.txt"));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

            try {
                localIdentity = LocalIdentity.read(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            teamspeakClient.setNickname(identity);
            teamspeakClient.setIdentity(localIdentity);
            teamspeakClient.setHWID("Windows");
            teamspeakClient.setExceptionHandler(throwable ->
                    logger.log(Level.WARNING, "There is an error: ", throwable));

            List<InetSocketAddress> lookup = null;

            try {
                lookup = TS3DNS.lookup("trainingsoase.net");
                if (lookup.isEmpty()) {
                    logger.info("There is no valid ts dns for the given ip. Please check the config file");
                    System.exit(-1);
                    return;
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            setState(BotState.CONNECTING);

            try {
                var address = lookup.get(0);
                logger.info("Trying to connect to the host: " + address.toString());
                teamspeakClient.connect(address, "", 5000L);
                logger.info("Waiting for the connected state");
                teamspeakClient.waitForState(ClientConnectionState.CONNECTED, 5000L);
            } catch (IOException | TimeoutException | InterruptedException e) {
                e.printStackTrace();
            }

            logger.info("Successfully connected to the server");

            try {
                teamspeakClient.subscribeAll();
            } catch (IOException | CommandException | TimeoutException | InterruptedException e) {
                e.printStackTrace();
            }

            try {
                teamspeakClient.setDescription("I am only a bot. Don't trust me. Best regards your bot <3");
            } catch (CommandException | IOException | ExecutionException | InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }

            try {
                teamspeakClient.joinChannel(8420,"");
            } catch (CommandException e) {
                logger.info("There is no channel that matches with the channel id. Joining base channel");
            } catch (IOException | InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }


            botID = teamspeakClient.getClientId();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> stopping = true));
            setState(BotState.RUNNING);

            this.interactionFactory = new InteractionFactory(teamspeakClient);
            this.clientProvider = new ClientProvider(logger, teamspeakClient);


            this.botInteraction = new BatoideaInteraction(teamspeakClient, botID);
            this.supportService.setSocket(teamspeakClient);
            this.supportService.setUserInteraction(new ClientInteraction(teamspeakClient));
            onLoad();
            teamspeakClient.addListener(new TeamSpeakListener(this));
            teamspeakClient.addListener(new ClientListener(clientProvider, logger, botID));
            new BotConsoleService(logger, this);
        }

        while (!stopping) {
            Thread.onSpinWait();
        }
    }

    protected void onLoad() {
        if (!getTeamspeakClient().isConnected()) {
            disconnect();
            return;
        }

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

    private void setState(BotState state) {
        synchronized (this.stateLock) {
            if (this.state != state) {
                logger.info("Change state " + this.state + " -> " + state);
                if (state == BotState.RUNNING) {
                    this.started = new Date(System.currentTimeMillis());
                }
                this.state = state;
                this.stateLock.notifyAll();
            }
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

    public LocalTeamspeakClientSocket getTeamspeakClient() {
        return teamspeakClient;
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

    public SupportService getSupportService() {
        return supportService;
    }

    @Override
    public AbstractInteractionFactory getInteractionFactory() {
        return interactionFactory;
    }
}