package net.theEvilReaper.batoidea.command;

import net.theEvilReaper.batoidea.command.commands.HelpCommand;
import net.theEvilReaper.batoidea.command.commands.PongCommand;
import net.theevilreaper.bot.api.command.Command;
import net.theevilreaper.bot.api.command.CommandCallback;
import net.theevilreaper.bot.api.command.CommandCaller;
import net.theevilreaper.bot.api.command.CommandManager;
import net.theevilreaper.bot.api.command.CommandSender;
import net.theevilreaper.bot.api.command.result.CommandResult;
import net.theevilreaper.bot.api.util.Conditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class CommandManagerImpl implements CommandManager {

    private static String COMMAND_PREFIX = "/";

    private final CommandCaller dispatcher = new CommandCallerImpl();

    private CommandCallback unknownCommandCallback;

    public CommandManagerImpl() {
        setUnknownCommandCallback((commandSender, command) -> commandSender.sendMessage("Unknown Command: " + command));
        registerBaseCommands();
    }

    /**
     * Register's some base commands.
     */

    private void registerBaseCommands() {
        register(new PongCommand());
        register(new HelpCommand());
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
    public String getCommandPrefix() {
        return COMMAND_PREFIX;
    }
}
