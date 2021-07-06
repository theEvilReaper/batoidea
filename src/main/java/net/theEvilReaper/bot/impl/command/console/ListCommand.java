package net.theEvilReaper.bot.impl.command.console;

import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theEvilReaper.bot.api.command.ConsoleCommand;
import net.theEvilReaper.bot.api.console.Console;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class ListCommand extends ConsoleCommand {

    private final LocalTeamspeakClientSocket clientSocket;

    public ListCommand(LocalTeamspeakClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void execute(@NotNull Console console, @NotNull String command, @Nullable String... args) {
    }
}
