package net.theEvilReaper.bot.impl.command.console;

import net.theEvilReaper.bot.api.command.ConsoleCommand;
import net.theEvilReaper.bot.api.console.Console;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class HelpCommand extends ConsoleCommand {

    public HelpCommand() {

    }

    @Override
    public void execute(@NotNull Console console, @NotNull String command, @Nullable String... args) {
        console.sendMessage("Currently i don't have any help");
    }
}
