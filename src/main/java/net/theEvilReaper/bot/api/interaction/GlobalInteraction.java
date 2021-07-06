package net.theEvilReaper.bot.api.interaction;

import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public interface GlobalInteraction extends Interaction {

    void broadcastMessage(@NotNull String message);
}
