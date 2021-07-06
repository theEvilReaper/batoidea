package net.theEvilReaper.bot.api.user;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.bot.api.service.IService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The {@link IUserIService} defines some methods that are used for the management of users.
 * @author theEvilReaper
 * @since 1.0.0
 * @version 1.0.0
 */
public interface IUserIService extends IService {

    /**
     * Add an user to the service.
     * @param client The {@link Client} to create the user
     */

    void addUser(@NotNull Client client);

    /**
     * Remove an user to the service.
     * @param clientID The client id from the user
     */

    void removeUser(int clientID);

    /**
     * Get an user with the given id.
     * @param clientID The id form the client to determine the {@link User}
     * @return the determined client. When no client found it returns a null reference
     */

    @Nullable
    User getUser(int clientID);
}