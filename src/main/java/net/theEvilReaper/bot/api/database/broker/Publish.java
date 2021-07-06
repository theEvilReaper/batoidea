package net.theEvilReaper.bot.api.database.broker;

import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public interface Publish {

    void publish(@NotNull String channel, byte[] values);
}
