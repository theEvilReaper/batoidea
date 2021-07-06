package net.theEvilReaper.bot.api;

public interface Connectable {

    /**
     * Handles the logic when something connects.
     */

    void connect();

    /**
     * Handles the logic when something disconnect.
     */

    void disconnect();

    /**
     * Returns if the bot is connected.
     * @return True when the bot is connected otherwise false
     */

    boolean isConnected();
}
