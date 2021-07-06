package net.theEvilReaper.bot.impl.command;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.interaction.Interaction;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class HelpCommand extends Command {

    private final UserInteraction userInteraction;

    public HelpCommand(Interaction... interaction) {
        this.userInteraction = (UserInteraction) interaction[0];
    }

    @Override
    public void onCommand(@NotNull Client executor, @NotNull String command, @Nullable String... args) {
        userInteraction.sendPrivateMessage(executor, "Currently i don't have any help");
    }
}
