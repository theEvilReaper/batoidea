package net.theEvilReaper.batoidea.service;

import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.theEvilReaper.bot.api.provider.IClientProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class ClientProvider implements IClientProvider {

    private final Logger logger;
    private final LocalTeamspeakClientSocket socket;
    private final Map<Integer, Client> clientMap;
    private final Cache<Integer, Client> clientCache;
    private final Cache<String, Client> clientIdentifierCache;
    private final Lock lock;

    public ClientProvider(Logger logger, LocalTeamspeakClientSocket socket) {
        this.logger = logger;
        this.socket = socket;
        this.clientMap = new HashMap<>();
        this.lock = new ReentrantLock();
        this.clientCache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .build();
        this.clientIdentifierCache = CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public void add(@NotNull Client client) {
        if (this.clientMap.putIfAbsent(client.getId(), client) == client) {
            System.out.println("Added Client " + client.getNickname() + ",ID=" + client.getDatabaseId());
        }
    }

    @Override
    public void remove(int clientID) {
        try {
            lock.lock();
            this.clientMap.remove(clientID);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getSize() {
        return clientMap.size();
    }

    @Override
    @Nullable
    public Client getClientById(int clientId) {
        try {
            return this.clientCache.get(clientId, () -> socket.getClientInfo(clientId));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Client recognizeClient(@NotNull Client client) {
        try {
            lock.lock();
            var teamspeakClient = findClient(client);

            if (teamspeakClient == null) {
                this.clientMap.put(client.getId(), client);
            }
            return teamspeakClient;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Client getClientByUniqueIdentifier(@NotNull String identifier) {
        try {
            return clientIdentifierCache.get(identifier, () -> {
                lock.lock();
                try {
                    for (Client client : clientMap.values()) {
                        if (client.getUniqueIdentifier() == null || client.getUniqueIdentifier().isEmpty()) {
                            client = getClientById(client.getId());
                        }

                        logger.fine(client.getNickname() + " => " + client.getUniqueIdentifier() + " ? " + identifier);

                        if (client.getUniqueIdentifier().equals(identifier)) {
                            return client;
                        }
                    }
                } finally {
                    lock.unlock();
                }
                return null;
            });
        } catch (ExecutionException executionException) {
            executionException.printStackTrace();
        }
        return null;
    }

    public Client findClient(Client client) {
        return clientMap.get(client.getId());
    }

    @Override
    public Map<Integer, Client> getClients() {
        return clientMap;
    }
}