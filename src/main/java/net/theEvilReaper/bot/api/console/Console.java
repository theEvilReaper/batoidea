package net.theEvilReaper.bot.api.console;

import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

@Deprecated(forRemoval = true)
public interface Console {

    void sendMessage(@NotNull String message);

    String getName();
}
