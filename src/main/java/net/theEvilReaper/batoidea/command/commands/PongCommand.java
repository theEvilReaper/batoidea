package net.theEvilReaper.batoidea.command.commands;

import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.command.CommandSender;
import net.theEvilReaper.bot.api.interaction.AbstractInteractionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class PongCommand extends Command {

    public PongCommand(AbstractInteractionFactory factory) {
        super(factory, "pong");
    }

    @Override
    public void apply(@NotNull CommandSender sender, @NotNull String command, @Nullable String... args) {
        sender.sendMessage("P.. Po.. Pon... Pong!!");
    }
}