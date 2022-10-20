package net.theevilreaper.batoidea;

import org.tinylog.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class Bootstrap {


    public static void main(String[] args) {
        Logger.info("Starting...");
        Logger.info("Loading Config...");
        Logger.info("""

                 ____        _        _     _           \s
                |  _ \\      | |      (_)   | |          \s
                | |_) | __ _| |_ ___  _  __| | ___  __ _\s
                |  _ < / _` | __/ _ \\| |/ _` |/ _ \\/ _` |
                | |_) | (_| | || (_) | | (_| |  __/ (_| |
                |____/ \\__,_|\\__\\___/|_|\\__,_|\\___|\\__,_|
                """);
        Logger.info("I am only an test bot. I have bugs lol");

        if (args.length == 0) {
            Logger.info("Loading configs from runtime path");
        }
        new Batoidea();
    }
}
