package net.theEvilReaper.bot.api.interaction;

import com.github.manevolent.ts3j.api.Channel;
import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.api.Permission;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public interface UserInteraction extends Interaction {

    void sendPrivateMessage(@NotNull Client client, @NotNull String message);

    void pokeClient(int clientID, @NotNull String message);

    void addPermission(int clientDatabaseID, @Nullable Permission... permissions);

    void removePermission(int clientDatabaseID, @Nullable Permission... permissions);

    default void moveToChannel(@NotNull Client client, Channel channel) {
        this.moveToChannel(channel.getId(), client, null);
    }

    default void moveToChannel(@NotNull Client client, int channelId) {
        moveToChannel(channelId, client, null);
    }

    void moveToChannel(int channelId, @NotNull Client client, @Nullable String password);

    void kick(@NotNull Client client, @NotNull String message);
}
