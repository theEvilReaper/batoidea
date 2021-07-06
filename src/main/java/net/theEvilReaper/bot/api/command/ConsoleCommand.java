package net.theEvilReaper.bot.api.command;

import net.theEvilReaper.bot.api.console.Console;
import net.theEvilReaper.bot.impl.Batoidea;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public abstract class ConsoleCommand {

    protected Logger logger = Batoidea.getBotLogger();

    public abstract void execute(@NotNull Console console, @NotNull String command, @Nullable String... args);
}
