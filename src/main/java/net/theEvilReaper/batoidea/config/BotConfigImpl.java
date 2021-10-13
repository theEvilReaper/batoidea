package net.theEvilReaper.batoidea.config;

import net.theEvilReaper.bot.api.config.BotConfig;
import net.theEvilReaper.bot.api.config.Config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class BotConfigImpl extends Config implements BotConfig {

    private static final String FILE_NAME = "config.properties";

    private final Logger logger;
    private final Path configPath;

    public BotConfigImpl(String directory) {
        this.logger = Logger.getLogger("BotLogger");
        this.configPath = Paths.get(directory, FILE_NAME);
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
                this.properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
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
     * Generates a default config
     */

    private void generateDefaultConfig() {
        this.properties = new Properties();

        this.properties.setProperty("name", "TeamSpeakBot");
        this.properties.setProperty("description", "I am bot <3");
        this.properties.setProperty("server", "");
        this.properties.setProperty("password","");
        this.properties.setProperty("securityLevel", "0");
        this.properties.setProperty("connectionTimeOut", "5000");
        this.properties.setProperty("defaultChannel", "-1");
    }
}