package net.theEvilReaper.batoidea.service;

import net.theevilreaper.bot.api.service.IService;
import net.theevilreaper.bot.api.service.ServiceRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.TreeMap;

/**
 * The {@link ServerRegistryImpl} is the implementation from the {@link ServiceRegistry}.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class ServerRegistryImpl implements ServiceRegistry {

    private final TreeMap<String, IService> serviceTreeMap;

    /**
     * Creates a new instance from the {@link ServerRegistryImpl}.
     */

    public ServerRegistryImpl() {
        this.serviceTreeMap = new TreeMap<>();
    }

    /**
     * Add a new service to the registry.
     * @param serviceName the name of the service
     * @param IService the service class which must implement the {@link IService}
     */

    @Override
    public void addService(@NotNull String serviceName, @NotNull IService IService) {
        serviceTreeMap.putIfAbsent(serviceName, IService);
    }

    /**
     * Remove a service from the registry.
     * @param serviceName The name of the service to remove
     */

    @Override
    public void removeService(@NotNull String serviceName) {
        this.serviceTreeMap.remove(serviceName);
    }

    /**
     * Get a {@link IService} from name.
     * @param serviceName The name of the service.
     * @return the fetched service. If no service matches with the given string it returns null
     */

    @Override
    @Nullable
    public IService getService(@NotNull String serviceName) {
        return this.serviceTreeMap.get(serviceName);
    }
}