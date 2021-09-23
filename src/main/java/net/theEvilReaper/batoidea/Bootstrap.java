package net.theEvilReaper.batoidea;

import net.theEvilReaper.batoidea.logging.BotLogger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class Bootstrap {

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format","[%1$td.%1$tm.%1$ty %1$tH:%1$tM:%1$tS] %4$s: %5$s%n");
        var logger = new BotLogger();

        logger.info("Starting...");
        logger.info("Loading Config...");


        logger.info("""

                 ____        _        _     _           \s
                |  _ \\      | |      (_)   | |          \s
                | |_) | __ _| |_ ___  _  __| | ___  __ _\s
                |  _ < / _` | __/ _ \\| |/ _` |/ _ \\/ _` |
                | |_) | (_| | || (_) | | (_| |  __/ (_| |
                |____/ \\__,_|\\__\\___/|_|\\__,_|\\___|\\__,_|
                """);
        logger.info("I am only an test bot. I have bugs lol");

        if (args.length == 0) {
            logger.info("Loading configs from runtime path");
        }

        new Batoidea();
    }
}
