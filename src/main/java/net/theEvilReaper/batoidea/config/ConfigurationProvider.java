package net.theEvilReaper.batoidea.config;

import net.theEvilReaper.bot.api.config.BotConfig;
import net.theEvilReaper.bot.api.config.IConfig;
import org.jetbrains.annotations.NotNull;

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

//TODO: Finish the provider
public class ConfigurationProvider {

    public static final Charset UTF_8_CHARSET = StandardCharsets.UTF_8;
    private static final String ENVIRONMENT = System.getProperty("user.dir");

    private static final String CONFIG_FOLDER = File.pathSeparator + "config";

    private final Path rootPath;

    private final BotConfig botConfig;

    public ConfigurationProvider() {
        this.rootPath = Paths.get(ENVIRONMENT);

        if (!Files.exists(Paths.get(rootPath.toString(), CONFIG_FOLDER))) {
            System.out.println("Creating folder for the config...");
        }
        botConfig = new BotConfigImpl(rootPath);
    }

    public IConfig loadConfiguration(@NotNull Path path) {
        return null;
    }

    public BotConfig getBotConfig() {
        return botConfig;
    }
}
