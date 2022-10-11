package net.theevilreaper.batoidea.config;

import net.theevilreaper.bot.api.config.BotConfig;
import net.theevilreaper.bot.api.config.IConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

//TODO: Finish the provider
public class ConfigurationProvider {

    public static final Charset UTF_8_CHARSET = StandardCharsets.UTF_8;
    private static final String ENVIRONMENT = System.getProperty("user.dir");

    private static final String CONFIG_FOLDER = File.pathSeparator + "config";
    private final Logger logger;
    private final Path rootPath;

    private final BotConfig botConfig;

    public ConfigurationProvider(@NotNull Logger logger) {
        BotConfig botConfig1;
        this.logger = logger;
        this.rootPath = Paths.get(ENVIRONMENT);

        if (!Files.exists(Paths.get(rootPath.toString(), CONFIG_FOLDER))) {
            logger.info("Creating folder for the config...");
            botConfig1 = new BotConfigImpl(logger, rootPath);
            ((BotConfigImpl) botConfig1).generateDefaultConfig();
        }

        botConfig1 = null;
        //botConfig = new BotConfigImpl(rootPath);
        botConfig = botConfig1;
    }

    @Nullable
    public IConfig loadConfig(@NotNull Path path) {
        if (!Files.exists(path)) {
            logger.info("Unable to load the file at the path: " + path);
            logger.info("Please check if the path is correct");
        }
        return null;
    }

    public BotConfig getBotConfig() {
        return botConfig;
    }
}
