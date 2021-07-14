package net.theEvilReaper.batoideas.user;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.bot.api.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * The {@link TeamSpeakUser} is the implementation from the {@link User}.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */

public class TeamSpeakUser implements User {

    private transient final Client client;
    private transient Locale locale;
    //TODO: Array....
    private transient int groupID;
    private boolean verified;

    /**
     * Creates a new instance from the {@link TeamSpeakUser}.
     * @param client The client to create the user
     */

    public TeamSpeakUser(@NotNull Client client) {
        this.client = client;
        this.locale = Locale.ENGLISH;
    }

    /**
     * Creates a new instance from the {@link TeamSpeakUser}.
     * @param client The client to create the user
     * @return the created instance of the {@link TeamSpeakUser}
     */

    public static TeamSpeakUser of(@NotNull Client client) {
        return new TeamSpeakUser(client);
    }

    /**
     * Set the group id from the user.
     * @param groupID The id to set
     */

    @Override
    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    /**
     * Change the verified status. Standard value is false
     * @param verified If the player is verified as boolean
     */

    @Override
    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    /**
     * Returns the group id from the user.
     * @return the group id from the user
     */

    @Override
    public int getGroupID() {
        return groupID;
    }

    /**
     * Returns if the user is verified.
     * @return true when verified otherwise false
     */

    @Override
    public boolean isVerified() {
        return verified;
    }

    /**
     * Returns the language from the user as {@link Locale}.
     * @return the underlying locale
     */

    @Override
    public Locale getLocale() {
        return null;
    }

    /**
     * Returns the {@link Client} from the user.
     * @return the given client
     */

    @Override
    public Client getClient() {
        return client;
    }
}