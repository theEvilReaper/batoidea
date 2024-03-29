package net.theevilreaper.batoidea.logging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
@Deprecated(forRemoval = true, since = "Use the logger from TinyLogger instead")
public class BotLogger extends Logger {

    public BotLogger() {
        super("BotLogger", null);
        setLevel(Level.ALL);
        Path logsFolder = Paths.get("logs");
        try {
            Files.createDirectories(logsFolder);
            FileHandler fh = new FileHandler("logs/latest.%g.log", 0, 100, false);
            var formatter = new SimpleFormatter();
            fh.setEncoding(StandardCharsets.UTF_8.name());
            fh.setLevel(Level.ALL);
            fh.setFormatter(formatter);
            addHandler(fh);
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setEncoding(StandardCharsets.UTF_8.name());
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(formatter);
            addHandler(consoleHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void log(LogRecord aRecord) {
        aRecord.setMessage(aRecord.getMessage());
        super.log(aRecord);
    }
}