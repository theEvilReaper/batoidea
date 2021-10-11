package net.theEvilReaper.batoidea.command.console;

import net.theEvilReaper.batoidea.Batoidea;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.command.CommandSender;
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
        super("exit");
        this.batoidea = batoidea;
    }

    @Override
    public void apply(@NotNull CommandSender sender, @NotNull String command, @Nullable String... args) {
        sender.sendMessage("Shutting down");
        batoidea.disconnect();
    }
}
