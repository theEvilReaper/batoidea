package net.theEvilReaper.batoidea.interaction;

import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theevilreaper.bot.api.interaction.GroupInteraction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public record ServerGroupInteraction(@NotNull LocalTeamspeakClientSocket teamspeakClient) implements GroupInteraction {

    @Override
    public void addServerGroup(@NotNull Client client, int groupId) {
        try {
            teamspeakClient.serverGroupAddClient(groupId, client.getDatabaseId());
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void removeServerGroup(@NotNull Client client, int groupId) {
        try {
            teamspeakClient.serverGroupRemoveClient(groupId, client.getDatabaseId());
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }
}
