package net.theEvilReaper.batoidea.interaction;

import com.github.manevolent.ts3j.api.Permission;
import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theEvilReaper.bot.api.interaction.ChannelInteraction;
import net.theEvilReaper.bot.api.util.Conditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public record ServerChannelInteraction(LocalTeamspeakClientSocket socket) implements ChannelInteraction {

    @Override
    public void sendChannelMessage(int channelID, @NotNull String message) {
        Conditions.checkForEmpty(message);
        try {
            socket.sendChannelMessage(channelID, message);
        } catch (IOException | CommandException | InterruptedException | TimeoutException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void joinChannel(int channelID, @Nullable String password) {
        try {
            socket.joinChannel(channelID, password);
        } catch (IOException | CommandException | InterruptedException | TimeoutException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void addChannelPermission(int channelID, @NotNull Permission... permissions) {
        try {
            socket.channelAddPermission(channelID, permissions);
        } catch (IOException | CommandException | InterruptedException | TimeoutException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void addChannelClientPermission(int channelID, int clientDatabaseID, @NotNull Permission... permissions) {
        try {
            socket.channelClientAddPermission(channelID, clientDatabaseID, permissions);
        } catch (IOException | CommandException | InterruptedException | TimeoutException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void removeChannelClientPermission(int channelID, int clientDatabaseID, @NotNull Permission... permissions) {
        try {
            socket.channelClientDeletePermission(channelID, clientDatabaseID, permissions);
        } catch (IOException | CommandException | InterruptedException | TimeoutException exception) {
            exception.printStackTrace();
        }
    }
}
