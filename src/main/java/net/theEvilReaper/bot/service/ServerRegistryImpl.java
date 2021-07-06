package net.theEvilReaper.bot.service;

import net.theEvilReaper.bot.api.service.IService;
import net.theEvilReaper.bot.api.service.ServiceRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.TreeMap;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class ServerRegistryImpl implements ServiceRegistry {

    private final TreeMap<String, IService> serviceTreeMap;

    public ServerRegistryImpl() {
        this.serviceTreeMap = new TreeMap<>();
    }

    @Override
    public void addService(@NotNull String serviceName, @NotNull IService IService) {
        serviceTreeMap.putIfAbsent(serviceName, IService);
    }

    @Override
    public void removeService(@NotNull String serviceName) {
        this.serviceTreeMap.remove(serviceName);
    }

    @Override
    @Nullable
    public IService getService(@NotNull String serviceName) {
        return this.serviceTreeMap.get(serviceName);
    }
}