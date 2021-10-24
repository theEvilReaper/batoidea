package net.theEvilReaper.batoidea.command;

import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.command.CommandCaller;
import net.theEvilReaper.bot.api.command.CommandSender;
import net.theEvilReaper.bot.api.command.result.CommandResult;
import net.theEvilReaper.bot.api.util.Strings;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class CommandCallerImpl implements CommandCaller {

    public static final Pattern SPLIT_PATTERN = Pattern.compile(Strings.SPACE);

    private final Map<String, Command> commandMap;
    private final Set<Command> commands;

    public CommandCallerImpl() {
        this.commandMap = new HashMap<>();
        this.commands = new HashSet<>();
    }

    @Override
    public void register(@NotNull Command command) {
        this.commandMap.put(command.getName().toLowerCase(), command);

        var aliases = command.getAliases();

        if (aliases != null) {
            for (int i = 0; i < aliases.length; i++) {
                this.commandMap.put(aliases[i].toLowerCase(), command);
            }
        }
        this.commands.add(command);
    }

    @Override
    public void unregister(@NotNull Command command) {
        this.commandMap.remove(command.getName().toLowerCase());

        final String[] aliases = command.getAliases();
        if (aliases != null) {
            for (String alias : aliases) {
                this.commandMap.remove(alias.toLowerCase());
            }
        }

        this.commands.remove(command);
    }

    @Override
    public CommandResult executeCommand(@NotNull CommandSender sender, @NotNull String command, @NotNull String... args) {
        var result = getResult(command);

        if (result.type() != CommandResult.ResultType.UNKNOWN && result.command() != null) {
            var commandInstance = result.command();
            commandInstance.apply(sender, commandInstance.getName(), result.args());
        }

        return result;
    }

    @Override
    public Command getCommand(@NotNull String commandName) {
        commandName = commandName.toLowerCase();
        return commandMap.getOrDefault(commandName, null);
    }

    private CommandResult getResult(@NotNull String commandString) {
        commandString = commandString.toLowerCase();

        var parts = SPLIT_PATTERN.split(commandString);

        var commandName = parts[0];

        var command = getCommand(commandName);

        if (command == null) {
            return CommandResult.ofUnknown(commandString);
        }

        var args = new String[parts.length - 1];

        if (parts.length > 1) {
            System.arraycopy(parts, 1, args, 0, parts.length - 1);
        }

        return CommandResult.of(commandString, command, CommandResult.ResultType.SUCCESS, args);
    }

    @NotNull
    @Override
    public Set<Command> getCommands() {
        return Collections.unmodifiableSet(commands);
    }
}
