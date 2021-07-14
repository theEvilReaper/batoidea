package net.theEvilReaper.batoidea.interaction;

import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class BotInteraction implements net.theEvilReaper.bot.api.interaction.BotInteraction {

    private final LocalTeamspeakClientSocket socket;
    private final int botID;

    public BotInteraction(LocalTeamspeakClientSocket socket, int botID) {
        this.socket = socket;
        this.botID = botID;
    }

    @Override
    public void sendChannelMessage(@NotNull String message) {
        try {
            socket.sendChannelMessage(socket.getClientInfo(botID).getChannelId(), message);
        } catch (IOException | CommandException | InterruptedException | TimeoutException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void poke(int clientID, @NotNull String message) {
        try {
            socket.clientPoke(clientID, message);
        } catch (IOException | CommandException | InterruptedException | TimeoutException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void joinChannel(int channelId) {
        try {
            if (socket.getClientInfo(botID).getChannelId() != channelId)
                socket.joinChannel(channelId, null);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }
}
