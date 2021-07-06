package net.theEvilReaper.bot.api;

import net.theEvilReaper.bot.api.interaction.BotInteraction;
import net.theEvilReaper.bot.api.provider.IChannelProvider;
import net.theEvilReaper.bot.api.provider.IClientProvider;
import net.theEvilReaper.bot.api.service.ServiceRegistry;
import net.theEvilReaper.bot.api.user.IUserIService;
import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public interface IBot extends Connectable {

    /**
     * Set / overwrite the given implementation of the {@link BotInteraction}.
     * @param botInteraction the interaction to set
     */

    void setBotInteraction(@NotNull BotInteraction botInteraction);

    /**
     * Gets if the bot is running.
     * @return true if the bot is running, false otherwise.
     */
    default boolean isRunning() {
        return getState() == BotState.RUNNING;
    }

    /**
     * Gets if the bot is stopping.
     * @return true if the bot is stopping, false otherwise.
     */
    default boolean isStopping() {
        return getState() == BotState.STOPPING;
    }

    /**
     * Gets the state of the bot.
     * @return Bot state.
     */
    BotState getState();

    /**
     * Returns the id from the bot.
     * The id would be set if the bot has successfully connected to the given server
     * @return the generated id from the bot.
     */

    int getBotID();

    /**
     * Returns the given instance from the {@link IUserIService}.
     * @return the underlying service
     */

    IUserIService getUserService();

    /**
     * Returns the given instance from the {@link IClientProvider}.
     * @return the underlying instance
     */

    IClientProvider getClientProvider();

    /**
     * Returns the given instance from the {@link IChannelProvider}.
     * @return the underlying instance
     */

    IChannelProvider getChannelProvider();

    /**
     * Returns the given instance from the {@link ServiceRegistry}.
     * @return the underlying instance
     */

    ServiceRegistry getServiceRegistry();

    /**
     * Returns the given instance from the {@link BotInteraction}.
     * @return the underlying instance
     */

    BotInteraction getBotInteraction();
}
