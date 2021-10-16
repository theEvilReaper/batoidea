package net.theEvilReaper.batoidea.console;

import net.theEvilReaper.batoidea.Batoidea;
import net.theEvilReaper.batoidea.command.console.ExitCommand;
import net.theEvilReaper.batoidea.command.console.HelpCommand;
import net.theEvilReaper.batoidea.command.console.NotifyCommand;
import net.theEvilReaper.batoidea.command.console.UserCommand;
import net.theEvilReaper.bot.api.command.ConsoleCommand;
import net.theEvilReaper.bot.api.util.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class BotConsoleService {

    private final Map<String, ConsoleCommand> commandMap;
    private final Logger logger;
    private final BotConsole console;

    private final Scanner scanner;
    private final Thread thread;
    private boolean exit;

    public BotConsoleService(Logger logger, Batoidea batoidea) {
        this.logger = logger;
        this.exit = batoidea.isStopping();
        this.commandMap = new HashMap<>();
        this.console = new BotConsole(logger);
        this.scanner = new Scanner(System.in);
        this.thread = this.init();
        this.thread.setDaemon(true);
        this.thread.setUncaughtExceptionHandler((thread1, throwable) -> {
            logger.log(Level.WARNING, throwable.getMessage());
        });
        this.thread.setName("Console-Thread");
        this.thread.start();
        this.initCommands(batoidea);
    }

    private void initCommands(Batoidea batoidea) {
        this.logger.log(Level.INFO, "Registering commands");
        this.registerCommand("exit", new ExitCommand(batoidea));
        this.registerCommand("help", new HelpCommand());
        this.registerCommand("notify", new NotifyCommand(batoidea.getSupportService()));
        this.registerCommand("user", new UserCommand(batoidea.getUserService(), batoidea.getClientProvider()));
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
        var split = Strings.SPACE_PATTERN.split(line);

        if (split.length == 0) {
            return;
        }

        String cmd = split[0];
        String[] args;

        if (split.length == 1) {
            args = new String[0];
        } else {
            args = new String[split.length - 1];

            System.arraycopy(split, 1, args, 0, split.length - 1);
        }

        dispatch(cmd, args);
    }

    /**
     * Register a new {@link ConsoleCommand} to the known commands.
     * @param name The name fo the command
     * @param command The class from the command that inherit from the {@link ConsoleCommand}
     */

    public void registerCommand(@NotNull String name, @NotNull ConsoleCommand command) {
        commandMap.put(name.toLowerCase(), command);
    }

    /**
     * Unregister a command from the console commands.
     * @param name The name of the command to remove
     */

    public void unregisterCommand(@NotNull String name) {
        commandMap.remove(name);
    }

    /**
     * Executes a {@link ConsoleCommand}.
     * @param cmd The name of the command to dispatch
     * @param args The arguments from the command
     */

    public void dispatch(@NotNull String cmd, @Nullable String... args) {
        var command = commandMap.get(cmd.toLowerCase());

        if (command == null) {
           logger.log(Level.INFO, "Unknown command. Type help for help");
            return;
        }

        try {
            command.execute(console, cmd, args);
        } catch (Exception exception) {
            logger.log(Level.SEVERE,"Error while executing command: " + cmd, exception);
        }
    }
}
