package net.theEvilReaper.bot.api.service;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public interface IService {

    /**
     * Change the ready state from the bot.
     * @param ready The new state
     */

    void setReady(boolean ready);

    /**
     * Returns if the service is ready.
     * @return the underlying boolean value
     */

    boolean isReady();

    /**
     * Returns the name of the service.
     * @return the given name
     */

    String getName();
}
