package net.theevilreaper.batoidea.interaction;

import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theevilreaper.bot.api.interaction.GlobalInteraction;
import net.theevilreaper.bot.api.util.Conditions;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public record ServerInteraction(LocalTeamspeakClientSocket clientSocket) implements GlobalInteraction {

    @Override
    public void broadcast(@NotNull String message) {
        Conditions.checkForEmpty(message);

        try {
            this.clientSocket.sendServerMessage(message);
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void broadcast(@NotNull String... messages) {
        if (messages.length == 0) return;

        if (messages.length == 1) {
            this.broadcast(messages[0]);
        }

        for (int i = 0; i < messages.length; i++) {
            this.broadcast(messages[i]);
        }
    }

    @Override
    public void broadcast(int groupID, @NotNull String message) {
        //TODO: Implement
    }
}
