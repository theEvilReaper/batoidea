package net.theevilreaper.batoidea.command.commands;

import net.theevilreaper.batoidea.Batoidea;
import net.theevilreaper.bot.api.command.Command;
import net.theevilreaper.bot.api.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class ExitCommand extends Command {

    private final Batoidea batoidea;

    public ExitCommand(Batoidea batoidea) {
        super("exit", "stop");
        this.batoidea = batoidea;
    }

    @Override
    public void apply(@NotNull CommandSender sender, @NotNull String command, @Nullable String... args) {
        sender.sendMessage("Shutting down");
        batoidea.disconnect();
    }
}
