package net.theEvilReaper.batoidea.command;

import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.command.CommandCallback;
import net.theEvilReaper.bot.api.command.CommandCaller;
import net.theEvilReaper.bot.api.command.CommandManager;
import net.theEvilReaper.bot.api.command.CommandSender;
import net.theEvilReaper.bot.api.command.ConsoleSender;
import net.theEvilReaper.bot.api.command.result.CommandResult;
import net.theEvilReaper.bot.api.user.User;
import net.theEvilReaper.bot.api.util.Conditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class CommandManagerImpl implements CommandManager {

    private static String COMMAND_PREFIX = "/";
    private final ConsoleSender consoleSender = new ConsoleSender();

    private final CommandCaller dispatcher = new CommandCallerImpl();

    private CommandCallback unknownCommandCallback;

    public CommandManagerImpl() {
    }

    @Override
    public synchronized void register(@NotNull Command command) {
        if (hasCommand(command.getName())) {
            //TODO: LOG
        }

        this.dispatcher.register(command);

    }

    @Override
    public void unregister(@NotNull Command command) {
        if (!hasCommand(command.getName())) {
            //TODO: LOG
        }

        this.dispatcher.unregister(command);
    }

    @Override
    public boolean hasCommand(@NotNull String commandName) {
        Conditions.checkForEmpty(commandName);
        return this.dispatcher.getCommand(commandName) != null;
    }

    @Override
    public void setCommandPrefix(@NotNull String prefix) {
        Conditions.checkForEmpty(prefix);
        COMMAND_PREFIX = prefix;
    }

    @Override
    public void setUnknownCommandCallback(@NotNull CommandCallback commandCallback) {
        this.unknownCommandCallback = commandCallback;
    }

    @Override
    @NotNull
    public CommandResult executeCommand(@NotNull CommandSender sender, @NotNull String command, @Nullable String... args) {
        if (sender instanceof User user) {
            //TODO: Update client
        }

        var result = this.dispatcher.executeCommand(sender, command, args);

        if (result.type() == CommandResult.ResultType.UNKNOWN) {
            if (unknownCommandCallback != null) {
                this.unknownCommandCallback.apply(sender, command);
            }
        }

        return result;
    }

    @Nullable
    @Override
    public CommandCallback getUnknownCommandCallback() {
        return unknownCommandCallback;
    }

    @Override
    @Nullable
    public Command getCommand(@NotNull String commandName) {
        Conditions.checkForEmpty(commandName);
        return dispatcher.getCommand(commandName);
    }

    @Override
    @NotNull
    public ConsoleSender getConsoleSender() {
        return consoleSender;
    }

    @NotNull
    @Override
    public String getCommandPrefix() {
        return COMMAND_PREFIX;
    }
}
