package net.theEvilReaper.batoidea.terminal;

import net.theEvilReaper.bot.api.command.CommandManager;
import net.theEvilReaper.bot.api.command.CommandParser;
import net.theEvilReaper.bot.api.command.CommandSender;
import net.theEvilReaper.bot.api.command.ConsoleSender;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class Terminal implements CommandParser {

    private static final Logger LOGGER = Logger.getLogger("BotLogger");
    private static final CommandSender CONSOLE = new ConsoleSender();

    private static volatile boolean running = false;

    private final CommandManager commandManager;

    private Thread inputThread;
    private Scanner scanner;

    public Terminal(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void startTerminal() {
        if (running) {
            throw new IllegalCallerException("Can't start the terminal twice");
        }

        if (inputThread != null) {
            throw new IllegalCallerException("The thread is not null. Can't start the terminal");
        }

        running = true;
        scanner = new Scanner(System.in);
        inputThread = getThread(scanner, commandManager);
        inputThread.setDaemon(true);
        inputThread.setName("Console-Thread");
        inputThread.setUncaughtExceptionHandler((t, e) -> LOGGER.warning(e.getMessage()));
        inputThread.start();

    }

    private Thread getThread(Scanner scanner, CommandManager commandManager) {
        return new Thread(() -> {
            while (running) {
                String input = scanner.nextLine();
                if (input == null || input.isEmpty()) return;
                parse(commandManager, CONSOLE, input);
            }
        });
    }


    public void stopTerminal() {
        if (!running) return;

        running = false;

        if (inputThread != null && !inputThread.isInterrupted()) {
            inputThread.interrupt();

            if (scanner != null) {
                scanner.close();
            }
        }
    }
}
