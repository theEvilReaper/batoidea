package net.theEvilReaper.batoidea.interaction;

import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theEvilReaper.bot.api.interaction.GlobalInteraction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class ServerInteraction implements GlobalInteraction {

    private final LocalTeamspeakClientSocket teamspeakClient;

    public ServerInteraction(LocalTeamspeakClientSocket teamspeakClient) {
        this.teamspeakClient = teamspeakClient;
    }

    @Override
    public void broadcastMessage(@NotNull String message) {
        if (message.isEmpty()) {
            throw new IllegalArgumentException("The message can not be empty");
        }
        try {
            this.teamspeakClient.sendServerMessage(message);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }
}
