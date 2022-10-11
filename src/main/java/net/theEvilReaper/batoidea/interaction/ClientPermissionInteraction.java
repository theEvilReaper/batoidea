package net.theevilreaper.batoidea.interaction;

import com.github.manevolent.ts3j.api.Permission;
import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theevilreaper.bot.api.interaction.PermissionInteraction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public record ClientPermissionInteraction(@NotNull LocalTeamspeakClientSocket client) implements PermissionInteraction {

    @Override
    public void addPermission(int clientDatabaseID, @Nullable Permission... permissions) {
        try {
            client.clientAddPermission(clientDatabaseID, permissions);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void removePermission(int clientDatabaseID, @Nullable Permission... permissions) {
        try {
            client.clientDeletePermission(clientDatabaseID, permissions);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
