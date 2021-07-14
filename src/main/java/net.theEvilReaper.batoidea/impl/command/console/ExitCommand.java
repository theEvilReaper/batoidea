package net.theEvilReaper.batoidea.impl.command.console;

import net.theEvilReaper.batoidea.impl.Batoidea;
import net.theEvilReaper.bot.api.command.ConsoleCommand;
import net.theEvilReaper.bot.api.console.Console;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class ExitCommand extends ConsoleCommand {

    private final Batoidea batoidea;

    public ExitCommand(Batoidea batoidea) {
        this.batoidea = batoidea;
    }

    @Override
    public void execute(@NotNull Console console, @NotNull String command, @Nullable String... args) {
        console.sendMessage("Shutting down");
        batoidea.disconnect();
    }
}
