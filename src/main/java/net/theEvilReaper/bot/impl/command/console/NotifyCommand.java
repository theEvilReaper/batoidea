package net.theEvilReaper.bot.impl.command.console;

import net.theEvilReaper.bot.api.command.ConsoleCommand;
import net.theEvilReaper.bot.api.console.Console;
import net.theEvilReaper.bot.service.SupportService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class NotifyCommand extends ConsoleCommand {

    private final SupportService supportService;

    public NotifyCommand(SupportService supportService) {
        this.supportService = supportService;
    }

    @Override
    public void execute(@NotNull Console console, @NotNull String command, @Nullable String... args) {
        console.sendMessage("Notifying supporter");
        supportService.notifySupporter();
    }
}
