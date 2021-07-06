package net.theEvilReaper.bot.config;

import net.theEvilReaper.bot.api.config.Config;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class BotConfig extends Config {

    private static final long DEFAULT_TIMEOUT = 5000L;
    private static final String FILE = "config.properties";

    private final Path path = Paths.get(FILE_PATH, FILE);

    @Override
    public void load() {
        if (!Files.exists(path)) {

        } else {
            try (InputStream inputStream = Files.newInputStream(path)) {
                this.properties.load(inputStream);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void reload() {
    }

    @Override
    public void save() {
    }

    public void setName(@NotNull String name) {
        this.setString("name", name);
    }

    public void setDescription(@NotNull String description) {
        this.setString("description", description);
    }

    public void setServer(@NotNull String server) {
        this.setString("server", server);
    }

    public void setPassword(@NotNull String password) {
        this.setString("password", password);
    }

    public void setSecurityLevel(int securityLevel) {
        this.setInt("securityLevel", securityLevel);
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.setLong("connectionTimeOut", connectionTimeout);
    }

    public void setDefaultChannel(int defaultChannel) {
        this.setInt("defaultChannel", defaultChannel);
    }

    public String getName() {
        var string = this.getString("name");
        return string == null ? "Bot" : string;
    }

    public String getDescription() {
        return this.getString("description");
    }

    public String getServer() {
        return this.getString("server");
    }

    public String getPassword() {
        return this.getString("password");
    }

    public int getSecurityLevel() {
        return this.getInt("securityLevel");
    }

    public long getConnectionTimeout() {
        var timeout = this.getLong("connectionTimeOut");
        return timeout == 0 ? DEFAULT_TIMEOUT : timeout;
    }

    public int getDefaultChannel() {
        return this.getInt("defaultChannel");
    }
}