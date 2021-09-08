package net.theEvilReaper.batoidea.interaction;

import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theEvilReaper.bot.api.interaction.GlobalInteraction;
import net.theEvilReaper.bot.api.util.Conditions;
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
        Conditions.checkForEmpty(message);

        try {
            this.teamspeakClient.sendServerMessage(message);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }
}
