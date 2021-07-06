package net.theEvilReaper.bot.impl.command;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.interaction.Interaction;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
import net.theEvilReaper.bot.impl.Batoidea;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class JoinChannelCommand extends Command {

    private final Batoidea batoidea;
    private final UserInteraction userInteraction;

    public JoinChannelCommand(Batoidea batoidea, Interaction... interaction) {
        this.batoidea = batoidea;
        this.userInteraction = (UserInteraction) interaction[0];
    }

    @Override
    public void onCommand(@NotNull Client executor, @NotNull String command, @Nullable String... args) {
        if (args.length > 1) {
            userInteraction.sendPrivateMessage(executor, "Please use: !join <id> or !join <name>");
            return;
        } else {
            var input = args[0];
        }
    }
}
