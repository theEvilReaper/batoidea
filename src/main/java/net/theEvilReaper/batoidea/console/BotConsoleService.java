package net.theEvilReaper.batoidea.console;

import net.theEvilReaper.batoidea.Batoidea;
import net.theEvilReaper.bot.api.command.CommandManager;
import net.theEvilReaper.bot.api.command.CommandSender;
import net.theEvilReaper.bot.api.command.ConsoleSender;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;
import java.util.logging.Level;

import static net.theEvilReaper.bot.api.util.Strings.*;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

@Deprecated(forRemoval = true)
public class BotConsoleService {

    private static final CommandSender CONSOLE = new ConsoleSender();

    private final CommandManager commandManager;

    private final Scanner scanner;
    private final Thread thread;
    private boolean exit;

    public BotConsoleService(Batoidea batoidea, CommandManager commandManager) {
        this.exit = batoidea.isStopping();
        this.commandManager = commandManager;
        this.scanner = new Scanner(System.in);
        this.thread = this.init();
        this.thread.setDaemon(true);
        this.thread.setUncaughtExceptionHandler((thread1, throwable) -> {
            batoidea.getLogger().log(Level.WARNING, throwable.getMessage());
        });
        this.thread.setName("Console-Thread");
        this.thread.start();
    }

    private Thread init() {
        return new Thread(() -> {
            while (!exit) {
                String input = this.scanner.nextLine();
                if (input == null || input.isEmpty()) return;
                handleInput(input);
            }
        });
    }

    private void handleInput(@NotNull String line) {
        var split = SPLIT_PATTERN.split(line);

        if (split.length == 0) {
            return;
        }

        String command = split[0];
        String[] arguments;

        arguments = split.length == 1 ? EMPTY_ARRAY : new String[split.length - 1];
        System.arraycopy(split, 1, arguments, 0, split.length - 1);
        commandManager.executeCommand(CONSOLE, command, arguments);
    }
}
