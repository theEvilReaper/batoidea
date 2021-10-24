package net.theEvilReaper.batoidea.interaction;

import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
import net.theEvilReaper.bot.api.util.Conditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public record ClientInteraction(@NotNull LocalTeamspeakClientSocket teamspeakClient) implements UserInteraction {

    @Override
    public void sendPrivateMessage(@NotNull Client client, @NotNull String message) {
        Conditions.checkForEmpty(message);
        try {
            teamspeakClient.sendPrivateMessage(client.getId(), message);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void moveToChannel(int channelId, @NotNull Client client, @Nullable String password) {
        if (password != null) {
            Conditions.checkForEmpty(password);
        }

        try {
            teamspeakClient.clientMove(client.getId(), channelId, password);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void kick(@NotNull Client client, @NotNull String message) {
        Conditions.checkForEmpty(message);
        try {
            teamspeakClient.kick(Collections.singletonList(client.getId()), message);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void pokeClient(int clientID, @NotNull String message) {
        Conditions.checkForEmpty(message);
        try {
            teamspeakClient.clientPoke(clientID, message);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }
}
