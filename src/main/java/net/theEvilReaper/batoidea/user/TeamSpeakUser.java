package net.theevilreaper.batoidea.user;

import com.github.manevolent.ts3j.api.Client;
import net.theevilreaper.bot.api.interaction.UserInteraction;
import net.theevilreaper.bot.api.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * The {@link TeamSpeakUser} is the implementation from the {@link User}.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */

public class TeamSpeakUser implements User {

    private transient final UserInteraction userInteraction;
    private transient int[] groups;
    private transient int channelID;

    private transient Client client;
    private transient Locale locale;

    private int mainGroup;
    private boolean verified;

    /**
     * Creates a new instance from the {@link TeamSpeakUser}.
     * @param client The client to create the user
     */

    public TeamSpeakUser(@NotNull Client client, @NotNull UserInteraction userInteraction) {
        this.client = client;
        this.userInteraction = userInteraction;
        this.locale = Locale.ENGLISH;
        this.channelID = client.getChannelId();
        this.groups = client.getServerGroups();
    }

    /**
     * Creates a new instance from the {@link TeamSpeakUser}.
     * @param client The client to create the user
     * @return the created instance of the {@link TeamSpeakUser}
     */

    public static TeamSpeakUser of(@NotNull Client client, @NotNull UserInteraction userInteraction) {
        return new TeamSpeakUser(client, userInteraction);
    }

    @Override
    public void sendMessage(@NotNull String message) {
        this.userInteraction.sendPrivateMessage(client, message);
    }

    /**
     * Set the group ids from the user.
     * @param groups The id to set
     */

    @Override
    public void setGroups(int... groups) {
        this.groups = groups;
    }

    /**
     * Refresh some underlying data.
     * @param client The object to update
     */

    @Override
    public void refresh(@NotNull Client client) {
        this.client = client;
        this.channelID = client.getChannelId();
        this.groups = client.getServerGroups();;
    }

    /**
     * Set the current channel from the client.
     * @param channelID The new channel id
     */

    @Override
    public void setCurrentChannel(int channelID) {
        this.channelID = channelID;
    }

    /**
     * Change the verified status. Standard value is false.
     * @param verified If the player is verified as boolean
     */

    @Override
    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    /**
     * Set's a locale to the user.
     * @param locale The locale to set
     */

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale == null ? Locale.ENGLISH : locale;
    }

    @Override
    public int getID() {
        return 0;
    }

    /**
     * Set the main group for the user.
     * @param mainGroup The id to set
     */

    @Override
    public void setMainGroup(int mainGroup) {
        this.mainGroup = mainGroup;
    }

    /**
     * Returns the main teamspeak group of the user.
     * @return the current main group as id
     */

    @Override
    public int getMainGroup() {
        return mainGroup;
    }

    /**
     * Returns the group ids from the user.
     * @return the group ids from the user
     */

    @Override
    public int[] getGroups() {
        return groups;
    }

    /**
     * Returns the current channel id from the client.
     * @return the given channel id
     */

    @Override
    public int getChannelID() {
        return channelID;
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
        return locale;
    }

    /**
     * Returns the {@link Client} from the user.
     * @return the given client
     */

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public String getName() {
        return client.getNickname();
    }
}