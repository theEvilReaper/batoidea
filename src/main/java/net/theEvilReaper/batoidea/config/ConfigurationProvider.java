package net.theevilreaper.batoidea.config;

import net.theevilreaper.bot.api.config.BotConfig;
import net.theevilreaper.bot.api.config.IConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class ConfigurationProvider {

    public static final Charset UTF_8_CHARSET = StandardCharsets.UTF_8;
    private static final String ENVIRONMENT = System.getProperty("user.dir");

    private static final String CONFIG_FOLDER = File.pathSeparator + "config";
    private final Path rootPath;

    private final BotConfig botConfig;

    public ConfigurationProvider() {
        BotConfig botConfig1;
        this.rootPath = Paths.get(ENVIRONMENT);

        if (!Files.exists(Paths.get(rootPath.toString(), CONFIG_FOLDER))) {
            Logger.info("Creating folder for the config...");
            botConfig1 = new BotConfigImpl(rootPath);
            ((BotConfigImpl) botConfig1).generateDefaultConfig();
        }

        botConfig = new BotConfigImpl(rootPath);
    }

    @Nullable
    public IConfig loadConfig(@NotNull Path path) {
        if (!Files.exists(path)) {
            Logger.info("Unable to load the file at the path: {}", path);
            Logger.info("Please check if the path is correct");
        }
        return null;
    }

    public BotConfig getBotConfig() {
        return botConfig;
    }
}
