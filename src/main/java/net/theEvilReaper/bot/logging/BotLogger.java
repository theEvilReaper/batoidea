package net.theEvilReaper.bot.logging;

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

public class BotLogger extends Logger {

    public BotLogger() {
        super("BotLogger", null);

        setLevel(Level.ALL);

        Path logsFolder = Paths.get("logs");
        try {
            Files.createDirectories(logsFolder);
            FileHandler fh = new FileHandler("logs/latest.%g.log", 80000, 100, false);
            var formatter = new SimpleFormatter();
            fh.setEncoding(StandardCharsets.UTF_8.name());
            fh.setLevel(Level.ALL);
            fh.setFormatter(formatter);
            addHandler(fh);
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setEncoding(StandardCharsets.UTF_8.name());
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(formatter);
            addHandler(consoleHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void log(LogRecord record) {
        record.setMessage(record.getMessage());
        super.log(record);
    }
}