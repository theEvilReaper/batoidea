package net.theEvilReaper.batoideas.interaction;

import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.api.Permission;
import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
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

public class ClientInteraction implements UserInteraction {

    private final LocalTeamspeakClientSocket teamspeakClient;

    public ClientInteraction(LocalTeamspeakClientSocket teamspeakClient) {
        this.teamspeakClient = teamspeakClient;
    }

    @Override
    public void sendPrivateMessage(@NotNull Client client, @NotNull String message) {
        checkString(message);
        try {
            teamspeakClient.sendPrivateMessage(client.getId(), message);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void moveToChannel(int channelId, @NotNull Client client, @Nullable String password) {
        if (password != null && password.trim().isEmpty()) {
            throw new IllegalArgumentException("The password can not be empty");
        }
        try {
            teamspeakClient.clientMove(client.getId(), channelId, password);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void kick(@NotNull Client client, @NotNull String message) {
        checkString(message);
        try {
            teamspeakClient.kick(Collections.singletonList(client.getId()), message);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void pokeClient(int clientID, @NotNull String message) {
        checkString(message);
        try {
            teamspeakClient.clientPoke(clientID, message);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void addPermission(int clientDatabaseID, @Nullable Permission... permissions) {
        try {
            teamspeakClient.clientAddPermission(clientDatabaseID, permissions);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void removePermission(int clientDatabaseID, @Nullable Permission... permissions) {
        try {
            teamspeakClient.clientDeletePermission(clientDatabaseID, permissions);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }

    public void checkString(@NotNull String string) {
        if (string.trim().isEmpty()) {
            throw new IllegalArgumentException("The message can not be empty");
        }
    }
}
