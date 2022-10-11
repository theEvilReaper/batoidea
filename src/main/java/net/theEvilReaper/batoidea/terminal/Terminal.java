package net.theevilreaper.batoidea.terminal;

import net.theevilreaper.bot.api.command.CommandManager;
import net.theevilreaper.bot.api.command.CommandParser;
import net.theevilreaper.bot.api.command.CommandSender;
import net.theevilreaper.bot.api.command.ConsoleSender;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class Terminal implements CommandParser {

    private static final Logger LOGGER = Logger.getLogger("BotLogger");
    private static final CommandSender CONSOLE = new ConsoleSender();
    private volatile boolean running = false;
    private final CommandManager commandManager;
    private Thread inputThread;
    private Scanner scanner;

    public Terminal(@NotNull CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void startTerminal() {
        if (this.running) {
            throw new IllegalCallerException("Can't start terminal twice");
        }

        if (this.inputThread != null) {
            throw new IllegalCallerException("The thread is not null. Can't start the terminal");
        }

        this.running = true;
        this.scanner = new Scanner(System.in);
        this.inputThread = getThread(this.scanner, this.commandManager);
        this.inputThread.setDaemon(true);
        this.inputThread.setName("Console-Thread");
        this.inputThread.setUncaughtExceptionHandler((t, e) -> LOGGER.warning(e.getMessage()));
        this.inputThread.start();
    }

    private Thread getThread(Scanner scanner, CommandManager commandManager) {
        return new Thread(() -> {
            while (this.running) {
                String input = scanner.nextLine();
                if (input == null || input.trim().isEmpty()) return;
                parse(commandManager, CONSOLE, input);
            }
        });
    }

    public void stopTerminal() {
        if (!this.running) return;

        this.running = false;

        if (this.inputThread != null && !inputThread.isInterrupted()) {
            this.inputThread.interrupt();

            if (this.scanner != null) {
                this.scanner.close();
            }
        }
    }
}
