package net.theEvilReaper.bot.impl.command;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.interaction.Interaction;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
import net.theEvilReaper.bot.service.FollowService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class UnfollowCommand extends Command {

    private final UserInteraction userInteraction;
    private final FollowService followService;

    public UnfollowCommand(FollowService followService, Interaction... interaction) {
        setMinimumGroup(-1);
        this.userInteraction = (UserInteraction) interaction[0];
        this.followService = followService;
    }

    @Override
    public void onCommand(@NotNull Client executor, @NotNull String command, @Nullable String... args) {
        if (!followService.isFollowing()) {
            userInteraction.sendPrivateMessage(executor, "I am not following someone currently");
        } else {
            userInteraction.sendPrivateMessage(executor, "Stopped the following from: " + followService.getFollowing());
            followService.reset();
        }
    }
}
