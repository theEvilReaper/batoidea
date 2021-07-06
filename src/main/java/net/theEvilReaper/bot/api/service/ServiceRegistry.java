package net.theEvilReaper.bot.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @since 1.0.0
 * @version 1.0.0
 */

public interface ServiceRegistry {

    /**
     * Add a new service to the registry.
     * @param serviceName the name of the service
     * @param IService the service class which must implement the {@link IService}
     */

    void addService(@NotNull String serviceName, @NotNull IService IService);

    /**
     * Remove a service from the registry.
     * @param serviceName The name of the service to remove
     */

    void removeService(@NotNull String serviceName);

    /**
     * Get a {@link IService} from name.
     * @param serviceName The name of the service.
     * @return the fetched service. If no service matches with the given string it returns null
     */

    @Nullable
    IService getService(@NotNull String serviceName);
}
