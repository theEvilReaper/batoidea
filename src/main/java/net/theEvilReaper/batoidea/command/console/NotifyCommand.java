package net.theEvilReaper.batoidea.command.console;

import net.theEvilReaper.batoidea.service.SupportService;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class NotifyCommand extends Command {

    private final SupportService supportService;

    public NotifyCommand(SupportService supportService) {
        super("notify");
        this.supportService = supportService;
    }

    @Override
    public void apply(@NotNull CommandSender sender, @NotNull String command, @Nullable String... args) {
        sender.sendMessage("Notifying supporter");
        supportService.notifySupporter();
    }
}
