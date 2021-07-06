package net.theEvilReaper.bot.api.interaction;

import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public interface GroupInteraction extends Interaction {

    void addServerGroup(@NotNull Client client, int groupId);

    void removeServerGroup(@NotNull Client client, int groupId);

    default boolean isInServerGroup(@NotNull Client client, int groupId) {
        if (client.getServerGroups().length != 0) {
            for (int i = 0; i < client.getServerGroups().length; i++) {
                if (client.getServerGroups()[i] == groupId)
                    return true;
            }
        }
        return false;
    }
}
