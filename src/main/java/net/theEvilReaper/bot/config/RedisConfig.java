package net.theEvilReaper.bot.config;

import net.theEvilReaper.bot.api.config.Config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static net.theEvilReaper.bot.config.ConfigurationProvider.RESOURCE_PATH;

public class RedisConfig extends Config {

    private final Path filePath = Paths.get(RESOURCE_PATH, "rabbit.properties");

    @Override
    public void load() {
        if (Files.exists(filePath)) {

        }
    }

    @Override
    public void reload() {

    }

    @Override
    public void save() {

    }
}
