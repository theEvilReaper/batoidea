package net.theEvilReaper.batoidea.impl.console;

import net.theEvilReaper.bot.api.console.Console;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class BotConsole implements Console {

    private final Logger logger;

    public BotConsole(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        logger.log(Level.INFO, message);
    }

    @Override
    public String getName() {
        return "Console";
    }
}

