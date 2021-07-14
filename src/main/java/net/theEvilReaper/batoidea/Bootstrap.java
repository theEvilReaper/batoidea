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

        logger.info("\n" +
                "  ____                 ____        _   \n" +
                " / __ \\               |  _ \\      | |  \n" +
                "| |  | | __ _ ___  ___| |_) | ___ | |_ \n" +
                "| |  | |/ _` / __|/ _ \\  _ < / _ \\| __|\n" +
                "| |__| | (_| \\__ \\  __/ |_) | (_) | |_ \n" +
                " \\____/ \\__,_|___/\\___|____/ \\___/ \\__|\n" +
                "                                       ");
        logger.info("I am only an test bot. I have bugs lol");

        new Batoidea();
    }
}
