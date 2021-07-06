package net.theEvilReaper.bot.impl.command;

import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.command.ConsoleCommand;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
import net.theEvilReaper.bot.impl.Batoidea;
import net.theEvilReaper.bot.impl.interaction.ClientInteraction;
import net.theEvilReaper.bot.service.FollowService;
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

public class UserCommandProvider {

    private final Logger logger = Logger.getLogger("BotLogger");
    private final Map<String, Command> userCommands;
    private final UserInteraction userInteraction;

    public UserCommandProvider(Batoidea batoidea, LocalTeamspeakClientSocket socket, FollowService followService) {
        this.userCommands = new HashMap<>();
        this.userInteraction = new ClientInteraction(socket);
        registerCommand("ping", new PongCommand(userInteraction));
        registerCommand("follow", new FollowCommand(batoidea, followService, userInteraction));
        registerCommand("unfollow", new UnfollowCommand(followService, userInteraction));
        registerCommand("help", new HelpCommand(userInteraction));
        registerCommand("nick", new NickCommand(batoidea, userInteraction));
        registerCommand("info", new InfoCommand(batoidea, userInteraction));
        registerCommand("support", new SupportCommand(batoidea, batoidea.getSupportService(), userInteraction));
    }

    /**
     * Register a new {@link ConsoleCommand} to the known commands.
     * @param name The name fo the command
     * @param command The class from the command that inherit from the {@link ConsoleCommand}
     */

    public void registerCommand(@NotNull String name, @NotNull Command command) {
        userCommands.put(name.toLowerCase(), command);
    }

    /**
     * Unregister a command from the console commands.
     * @param name The name of the command to remove
     */

    public void unregisterCommand(@NotNull String name) {
        userCommands.remove(name);
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
            logger.info("Unknown command. Type help for help");
            return;
        }

        try {
            command.onCommand(client, cmd, args);
        } catch (Exception exception) {
            logger.info("Error while executing command: " + cmd);
            logger.warning(exception.getMessage());
        }
    }
}
