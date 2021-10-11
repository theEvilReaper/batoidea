package net.theEvilReaper.batoidea.command.console;

import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help");
    }

    @Override
    public void apply(@NotNull CommandSender sender, @NotNull String command, @Nullable String... args) {
        sender.sendMessage("Currently i don't have any help");
    }
}
