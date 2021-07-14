package net.theEvilReaper.batoidea.config;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class ConfigurationProvider {

    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private static final String ENVIRONMENT = System.getProperty("user.dir");

    private boolean generated;

    public static String RESOURCE_PATH = "src/main/resources/";

    public ConfigurationProvider() {
        Path rootPath = Paths.get(ENVIRONMENT);

    }

    public void loadFiles() {
    }
}
