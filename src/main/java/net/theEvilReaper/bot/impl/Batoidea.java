package net.theEvilReaper.bot.impl;

import com.github.manevolent.ts3j.api.Channel;
import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.identity.LocalIdentity;
import com.github.manevolent.ts3j.protocol.TS3DNS;
import com.github.manevolent.ts3j.protocol.client.ClientConnectionState;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import com.github.manevolent.ts3j.util.Ts3Debugging;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.theEvilReaper.bot.api.BotState;
import net.theEvilReaper.bot.api.IBot;
import net.theEvilReaper.bot.api.provider.IChannelProvider;
import net.theEvilReaper.bot.api.provider.IClientProvider;
import net.theEvilReaper.bot.api.service.ServiceRegistry;
import net.theEvilReaper.bot.api.user.IUserIService;
import net.theEvilReaper.bot.impl.console.BotConsoleService;
import net.theEvilReaper.bot.impl.interaction.BotInteraction;
import net.theEvilReaper.bot.impl.interaction.ClientInteraction;
import net.theEvilReaper.bot.listener.TeamSpeakListener;
import net.theEvilReaper.bot.logging.BotLogger;
import net.theEvilReaper.bot.service.FollowService;
import net.theEvilReaper.bot.service.SupportService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class Batoidea implements IBot {

    public static final Pattern SPLIT_PATTERN = Pattern.compile(" ");

    static BotLogger botLogger;

    private int botID;

    public static LocalTeamspeakClientSocket teamspeakClient;

    private final Cache<Integer, Client> clientCache;
    private final Cache<String, Client> clientIdentifierCache;

    private final Map<Integer, Client> clientMap;
    private final Map<Integer, Channel> channels;

    private final FollowService followService;
    private final Lock updateLock;

    private volatile boolean stopping;

    private final Object stateLock = new Object();
    private BotState state = BotState.STOPPED;
    private Date started;

    private BotInteraction botInteraction;

    private SupportService supportService;

    public Batoidea() {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$td.%1$tm.%1$ty %1$tH:%1$tM:%1$tS] %4$s: %5$s%n");
        System.out.println("Running Enviroment: " + System.getProperty("user.dir"));
        botLogger = new BotLogger();

        botLogger.info("\n" +
                "  ____                 ____        _   \n" +
                " / __ \\               |  _ \\      | |  \n" +
                "| |  | | __ _ ___  ___| |_) | ___ | |_ \n" +
                "| |  | |/ _` / __|/ _ \\  _ < / _ \\| __|\n" +
                "| |__| | (_| \\__ \\  __/ |_) | (_) | |_ \n" +
                " \\____/ \\__,_|___/\\___|____/ \\___/ \\__|\n" +
                "                                       ");
        botLogger.info("I am only an test bot. I have bugs lol");
        clientCache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .build();
        clientIdentifierCache = CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build();
        clientMap = new HashMap<>();
        channels = new HashMap<>();
        updateLock = new ReentrantLock();

        this.followService = new FollowService();
        Ts3Debugging.setEnabled(false);
        connect();
    }

    @Override
    public void connect() throws IllegalAccessError {
        this.supportService = new SupportService(null, 8420);
        if (teamspeakClient != null) return;
        synchronized (stateLock) {
            if (getState() != BotState.STOPPED) throw new IllegalStateException(state.name());

            setState(BotState.STARTING);

            this.teamspeakClient = new LocalTeamspeakClientSocket();

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

            this.teamspeakClient.setNickname(identity);
            this.teamspeakClient.setIdentity(localIdentity);
            this.teamspeakClient.setExceptionHandler(throwable -> {
                System.out.println("There is an error: " + throwable);
            });

            try {
                List<InetSocketAddress> lookup = TS3DNS.lookup("trainingsoase.net");
                for (InetSocketAddress inetSocketAddress : lookup) {
                    System.out.println(inetSocketAddress.getHostString());
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }


            try {
                teamspeakClient.connect(new InetSocketAddress("ts3.ht-hosting.de", 9999), "", 5000L);
                //teamspeakClient.waitForState(ClientConnectionState.CONNECTED, 5000L);
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }

            try {
                teamspeakClient.subscribeAll();
            } catch (IOException | CommandException | TimeoutException | InterruptedException e) {
                e.printStackTrace();
            }

            this.teamspeakClient.addListener(new TeamSpeakListener(this, followService));

            try {
                teamspeakClient.setDescription("I am only a bot. Don't trust me. Best regards your bot <3");
            } catch (CommandException | IOException | ExecutionException | InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }

            try {
                teamspeakClient.joinChannel(8420,"");
            } catch (CommandException e) {
                botLogger.info("There is no channel that matches with the channelid. Joining base channel");
            } catch (IOException | InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }

            new BotConsoleService(botLogger, this, teamspeakClient);

            botID = teamspeakClient.getClientId();
            update();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                stopping = true;
            }));
            setState(BotState.RUNNING);

            this.botInteraction = new BotInteraction(teamspeakClient, botID);

            this.supportService.setSocket(teamspeakClient);
            this.supportService.setUserInteraction(new ClientInteraction(teamspeakClient));
        }

        while (!stopping) {}
    }

    protected void update() {
        if (!getTeamspeakClient().isConnected()) {
            disconnect();
            return;
        }

        try {
            updateLock.lock();

            clientMap.clear();



            Client clientss = null;

            for (Client client : teamspeakClient.listClients()) {
                clientMap.put(client.getId(), client);
            }

            channels.clear();
            for (Channel channel : teamspeakClient.listChannels()) {
                channels.put(channel.getId(), channel);
            }
        }catch (IOException | TimeoutException | InterruptedException | CommandException e) {
            System.out.println("Unable to fetch updates");
        } finally {
            updateLock.unlock();
        }
    }

    public static BotLogger getBotLogger() {
        return botLogger;
    }

    public Client getClient(Client client) {
        return getClientById(client.getId());
    }

    @Nullable
    public Client getClientById(int clientId) {
        try {
            return clientCache.get(clientId, () -> teamspeakClient.getClientInfo(clientId));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void disconnect() {
        synchronized (stateLock) {
            if (getState() != BotState.RUNNING) throw new IllegalStateException(state.name());

            botLogger.info("Shutting down...");

            setState(BotState.STOPPING);

            if (teamspeakClient.isConnected()) {
                try {
                    teamspeakClient.disconnect("Requested disconnect via console! (no)");
                } catch (IOException | TimeoutException | ExecutionException | InterruptedException exception) {
                    exception.printStackTrace();
                }
            }

            setState(BotState.STOPPED);
            botLogger.info("Shutdown complete");
        }
        System.exit(0);
    }

    @Override
    public boolean isConnected() {
        return teamspeakClient.isConnected();
    }

    @Nullable
    public Client getClientByUniqueIdentifier(String identifier) {
        try {
            return clientIdentifierCache.get(identifier, () -> {
                updateLock.lock();
                try {
                    for (Client client : getClientMap().values()) {
                        if (client.getUniqueIdentifier() == null || client.getUniqueIdentifier().isEmpty()) {
                            client = getClientById(client.getId());
                        }
                        if (client.getUniqueIdentifier().equals(identifier)) {
                            return client;
                        }
                    }
                } finally {
                    updateLock.unlock();
                }
                return null;
            });
        } catch (ExecutionException executionException) {
            executionException.printStackTrace();
        }
        return null;
    }

    public Channel recognizeChannel(@NotNull Channel channel) {
        var teamSpeakChannel = findChannelID(channel.getId());

        if (teamSpeakChannel == null) {
            channels.put(channel.getId(), channel);
        }

        return teamSpeakChannel;
    }

    public Client recognizeClient(@NotNull Client client) {
        var teamspeakClient = findClient(client);

        if (teamspeakClient == null) {
            this.clientMap.put(client.getId(), client);
        }

        return teamspeakClient;
    }

    @Override
    public void setBotInteraction(@NotNull net.theEvilReaper.bot.api.interaction.BotInteraction botInteraction) {
        this.botInteraction = (BotInteraction) botInteraction;
    }

    public boolean isStopping() {
        return stopping;
    }

    private void setState(BotState state) {
        synchronized (this.stateLock) {
            if (this.state != state) {
                botLogger.info("Change state " + this.state + " -> " + state);
                if (state == BotState.RUNNING) {
                    this.started = new Date(System.currentTimeMillis());
                }
                this.state = state;
                this.stateLock.notifyAll();
            }
        }
    }

    @Override
    public BotState getState() {
        return state;
    }

    public Client findClient(Client client) {
        return clientMap.get(client.getId());
    }

    public Channel findChannelID(int id) {
        return channels.get(id);
    }


    @Override
    public int getBotID() {
        return botID;
    }

    public Lock getUpdateLock() {
        return updateLock;
    }

    public Map<Integer, Channel> getChannels() {
        return channels;
    }

    public Map<Integer, Client> getClientMap() {
        return clientMap;
    }

    public LocalTeamspeakClientSocket getTeamspeakClient() {
        return teamspeakClient;
    }

    @Override
    public BotInteraction getBotInteraction() {
        return botInteraction;
    }

    @Override
    public IUserIService getUserService() {
        return null;
    }

    @Override
    public IClientProvider getClientProvider() {
        return null;
    }

    @Override
    public IChannelProvider getChannelProvider() {
        return null;
    }

    @Override
    public ServiceRegistry getServiceRegistry() {
        return null;
    }

    public Date getStarted() {
        return started;
    }

    public SupportService getSupportService() {
        return supportService;
    }
}
