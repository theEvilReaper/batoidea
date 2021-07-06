package net.theEvilReaper.bot.api.database.broker;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public interface Subscribe {

    void subscribe(@NotNull String exchange, Consumer<byte[]> consumer);
}
