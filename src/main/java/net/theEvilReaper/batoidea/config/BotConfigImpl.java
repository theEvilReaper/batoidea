package net.theEvilReaper.batoidea.config;

import net.theevilreaper.bot.api.config.BotConfig;
import net.theevilreaper.bot.api.config.Config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

import static net.theEvilReaper.batoidea.config.ConfigurationProvider.UTF_8_CHARSET;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class BotConfigImpl extends Config implements BotConfig {

    private static final String FILE_NAME = "config.properties";

    private final Logger logger;
    private final Path configPath;

    private int[] blockedGroups;

    public BotConfigImpl(Logger logger, Path directory) {
        this.logger = logger;
        this.configPath = Paths.get(directory.toString(), FILE_NAME);
        this.load();
    }

    /**
     * Loads the config {@link Properties}.
     * A {@link Properties} will be created when there is no config available
     */

    @Override
    public void load() {
      if (!Files.exists(configPath)) {
            this.logger.info("No config found. Creating config from scratch");
            this.generateDefaultConfig();
            this.save();
        } else {
          this.logger.info("Loading config file");
          this.properties = new Properties();
          try (InputStream inputStream = Files.newInputStream(this.configPath)) {
                this.properties.load(new InputStreamReader(inputStream, UTF_8_CHARSET));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
          this.logger.info("Config successfully loaded");
        }
    }

    /**
     * Writes the data into the config {@link Properties}.
     */

    @Override
    public void save() {
       try (OutputStream outputStream = Files.newOutputStream(this.configPath)) {
            this.properties.store(outputStream, "This is the config for the bot");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Overrides the underlying implementation of the method to reduce parsing of the array.
     * @return The parsed array for the blocked groups
     */

    @Override
    public int[] getBlockedGroups() {
        if (blockedGroups == null) {
            blockedGroups = BotConfig.super.getBlockedGroups();
        }

        return blockedGroups;
    }

    /**
     * Generates a default config
     */

    protected void generateDefaultConfig() {
        this.properties = new Properties();

        this.properties.setProperty("name", "TeamSpeakBot");
        this.properties.setProperty("description", "I am bot <3");
        this.properties.setProperty("server", "");
        this.properties.setProperty("port", "9987");
        this.properties.setProperty("password","");
        this.properties.setProperty("securityLevel", "0");
        this.properties.setProperty("connectionTimeOut", "5000");
        this.properties.setProperty("defaultChannel", "-1");
        this.properties.setProperty("ignoreGroups", "");
    }
}