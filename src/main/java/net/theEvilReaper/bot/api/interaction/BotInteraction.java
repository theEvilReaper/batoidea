package net.theEvilReaper.bot.api.interaction;

import com.github.manevolent.ts3j.api.Channel;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public interface BotInteraction extends Interaction {

    void sendChannelMessage(@NotNull String message);

    void poke(@NotNull String message, int clientID);

    default void joinChannel(Channel targetChannel) {
        joinChannel(targetChannel.getId());
    }

    void joinChannel(int channelId);

}
