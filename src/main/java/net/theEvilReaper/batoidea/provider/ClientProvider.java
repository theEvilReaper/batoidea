package net.theevilreaper.batoidea.provider;

import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.theevilreaper.bot.api.provider.IClientProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientProvider implements IClientProvider {

    private final LocalTeamspeakClientSocket socket;
    private final Map<Integer, Client> clientMap;
    private final Cache<Integer, Client> clientCache;
    private final Cache<String, Client> clientIdentifierCache;
    private final Lock lock;

    public ClientProvider(@NotNull LocalTeamspeakClientSocket socket) {
        this.socket = socket;
        this.clientMap = new HashMap<>();
        this.lock = new ReentrantLock();
        this.clientCache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build();
        this.clientIdentifierCache = CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public void add(@NotNull Client client) {
        if (Objects.requireNonNull(this.clientMap.putIfAbsent(client.getId(), client)).getDatabaseId() == client.getDatabaseId()) {
            Logger.info("Add client with name= {} and id={}", client.getNickname(), client.getDatabaseId());
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
    public Client getClientByUniqueIdentifier(@NotNull String identifier) {
        try {
            return clientIdentifierCache.get(identifier, () -> {
                lock.lock();
                try {
                    for (Client client : clientMap.values()) {
                        if (client.getUniqueIdentifier() == null || client.getUniqueIdentifier().isEmpty()) {
                            client = getClientById(client.getId());
                        }

                        Logger.info("{} => {} ?", client.getNickname(), client.getUniqueIdentifier(), identifier);

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

    public Client findClient(@NotNull Client client) {
        return clientMap.get(client.getId());
    }

    @Override
    public Map<Integer, Client> getClients() {
        return clientMap;
    }
}