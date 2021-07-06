package net.theEvilReaper.bot.api.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Properties;

/**
 * @author theEvilReaper
 * @since 1.0.0
 * @version 1.0.0
 */

public abstract class Config implements IConfig {

    protected static final String FILE_PATH = "src/main/resources/";
    protected Path path;

    protected Properties properties;

    /**
     * Loads the {@link Properties}.
     */

    public abstract void load();

    /**
     * Reloads the {@link Properties}.
     */

    public abstract void reload();

    /**
     * Saves the {@link Properties}.
     */

    public abstract void save();

    /**
     * Checks if the underlying {@link Properties} is loaded
     */

    private void checkPropertiesState() {
        if (properties == null) {
            throw new IllegalStateException("The properties file is null. Can not set / get values");
        }
    }

    /**
     * Checks if a given String is empty.
     * @param string the string to check
     */

    private void checkIfStringEmpty(@NotNull String string) {
        if (string.isEmpty()) {
            throw new IllegalArgumentException("The given key can not be empty");
        }
    }

    @Override
    public void setString(@NotNull String key, @NotNull String value) {
        checkPropertiesState();
        checkIfStringEmpty(key);
        properties.setProperty(key, value);
    }

    @Override
    public void setInt(@NotNull String key, int value) {
        checkPropertiesState();
        checkIfStringEmpty(key);
        properties.setProperty(key, String.valueOf(value));
    }

    @Override
    public void setDouble(@NotNull String key, double value) {
        checkPropertiesState();
        checkIfStringEmpty(key);
        properties.setProperty(key, String.valueOf(value));
    }

    @Override
    public void setLong(@NotNull String key, long value) {
        checkPropertiesState();
        checkIfStringEmpty(key);
        properties.setProperty(key, String.valueOf(value));
    }

    @Override
    @Nullable
    public String getString(@NotNull String key) {
        checkPropertiesState();
        checkIfStringEmpty(key);
        return properties.getProperty(key);
    }

    @Override
    public int getInt(@NotNull String key) {
        checkPropertiesState();
        checkIfStringEmpty(key);
        return Integer.parseInt(properties.getProperty(key));
    }

    @Override
    public double getDouble(@NotNull String key) {
        checkPropertiesState();
        checkIfStringEmpty(key);
        return Double.parseDouble(properties.getProperty(key));
    }

    @Override
    public long getLong(@NotNull String key) {
        checkPropertiesState();
        checkIfStringEmpty(key);
        return Long.parseLong(properties.getProperty(key));
    }
}