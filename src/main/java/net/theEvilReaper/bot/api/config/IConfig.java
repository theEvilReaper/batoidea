package net.theEvilReaper.bot.api.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @since 1.0.0
 * @version 1.0.0
 */

public interface IConfig {

    /**
     * Sets an string into a given file.
     * @param key The name of the key
     * @param value The value to set
     */

    void setString(@NotNull String key, @NotNull String value);

    /**
     * Sets an integer into a given file.
     * @param key The name of the key
     * @param value The value to set
     */

    void setInt(@NotNull String key, int value);

    /**
     * Sets an double into a given file.
     * @param key The name of the key
     * @param value The value to set
     */

    void setDouble(@NotNull String key, double value);

    /**
     * Sets an long into a given file.
     * @param key The name of the key
     * @param value The value to set
     */

    void setLong(@NotNull String key, long value);

    /**
     * Get a string from a given file.
     * @param key The key to determine the string.
     * @return the fetched string
     */

    @Nullable
    String getString(@NotNull String key);

    /**
     * Get a int from a given file.
     * @param key The key to determine the string.
     * @return the fetched int
     */

    int getInt(@NotNull String key);

    /**
     * Get a double from a given file.
     * @param key The key to determine the string.
     * @return the fetched double
     */

    double getDouble(@NotNull String key);

    /**
     * Get a long from a given file.
     * @param key The key to determine the string.
     * @return the fetched double
     */

    long getLong(@NotNull String key);
}
