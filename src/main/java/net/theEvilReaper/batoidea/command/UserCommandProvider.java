package net.theEvilReaper.batoidea.command;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.command.ConsoleCommand;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

@Deprecated(forRemoval = true)
public class UserCommandProvider {

    private final Logger logger;
    private final Map<String, Command> userCommands;
    private final UserInteraction userInteraction;

    public UserCommandProvider(Logger logger, UserInteraction userInteraction) {
        this.logger = logger;
        this.userInteraction = userInteraction;
        this.userCommands = new HashMap<>();
    }

    /**
     * Register a new {@link ConsoleCommand} to the known commands.
     * @param command The class from the command that inherit from the {@link ConsoleCommand}
     */

    public void registerCommand(@NotNull Command command) {
        userCommands.put(command.getName(), command);
    }

    /**
     * Unregister a command from the console commands.
     * @param name The name of the command to remove
     */

    public void unregisterCommand(@NotNull String name) {
        userCommands.remove(name);
    }

    public void unregisterCommand(@NotNull Command command) {
        this.userCommands.remove(command.getName());
    }

    /**
     * Executes a {@link ConsoleCommand}.
     * @param cmd The name of the command to dispatch
     * @param args The arguments from the command
     */

    public void dispatch(@NotNull Client client, @NotNull String cmd, @Nullable String... args) {
        var command = userCommands.get(cmd.toLowerCase());

        if (command == null) {
            userInteraction.sendPrivateMessage(client, "Unknown command. Type !help for help");
            return;
        }

        try {
            //command.onCommand(client, cmd, args);
        } catch (Exception exception) {
            logger.info("Error while executing command: " + cmd);
            logger.warning(exception.getMessage());
        }
    }
}
