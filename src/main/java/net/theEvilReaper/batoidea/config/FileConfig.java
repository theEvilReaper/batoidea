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

public class FileConfig extends Config implements BotConfig {

    private static final String FILE_NAME = "config.properties";

    private final Logger logger;
    private final Path configPath;

    public FileConfig(String directory) {
        this.logger = Logger.getLogger("BotLogger");
        this.configPath = Paths.get(directory, FILE_NAME);
    }

    @Override
    public void load() {
      if (!Files.exists(configPath)) {
            logger.info("No config found. Creating config from scratch");
            this.generateDefaultConfig();
            save();
        } else {
          logger.info("Loading config file");
          this.properties = new Properties();
          try (InputStream inputStream = Files.newInputStream(configPath)) {
                this.properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
          logger.info("Config successfully loaded");
          logger.info("The name of the bot is: " + getName());
        }
    }

    @Override
    public void save() {
       try (OutputStream outputStream = Files.newOutputStream(configPath)) {
            this.properties.store(outputStream, "This is the config for the bot");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

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