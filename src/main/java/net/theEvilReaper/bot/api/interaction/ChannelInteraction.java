package net.theEvilReaper.bot.api.interaction;

import com.github.manevolent.ts3j.api.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public interface ChannelInteraction extends Interaction {

    void sendChannelMessage(int channelID, @NotNull String message);

    default void joinChannel(int channelID) {
        joinChannel(channelID, null);
    }

    void joinChannel(int channeldID, @Nullable String password);

    void addChannelPermission(int channelID, @Nullable Permission... permissions);

    void addChannelClientPermission(int channelID, int clientDatabaseID,  @Nullable Permission... permissions);

    void removeChannelClientPermission(int channelID, int clientDatabaseID, @Nullable Permission... permissions);
}
