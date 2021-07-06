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

public class VerifyCommand extends Command {

    private final UserInteraction userInteraction;

    public VerifyCommand(Interaction... interaction) {
        this.userInteraction = (UserInteraction) interaction[0];
    }

    //!verify theEvilReaper

    @Override
    public void onCommand(@NotNull Client executor, @NotNull String command, @Nullable String... args) {
        if (args == null || args.length == 0) {
            userInteraction.sendPrivateMessage(executor, "Wrong syntax. Use !verify <name>");
            return;
        }

        if (args[0] != null && args[0].trim().isEmpty()) {
            userInteraction.sendPrivateMessage(executor, "The given name can not be empty");
            return;
        }
    }
}
