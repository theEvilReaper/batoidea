package net.theEvilReaper.bot.impl.command;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.interaction.Interaction;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
import net.theEvilReaper.bot.impl.Batoidea;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class NickCommand extends Command {

    private static final String DEFAULT_NAME = "ReaperBot";

    private final Batoidea batoidea;
    private final UserInteraction userInteraction;

    public NickCommand(Batoidea batoidea, Interaction... interaction) {
        this.batoidea = batoidea;
        this.userInteraction = (UserInteraction) interaction[0];
    }

    @Override
    public void onCommand(@NotNull Client executor, @NotNull String command, @Nullable String... args) {
       /* if (!isDefault()) {
            userInteraction.sendPrivateMessage(executor, "You don't have the permission to execute that command");
        } else {*/

        if ("reset".equals(args[0])) {
            userInteraction.sendPrivateMessage(executor, "Reset my name to default");
            batoidea.getTeamspeakClient().setNickname(DEFAULT_NAME);
            Logger.getLogger("BotLogger").info("Reset name requested");
        } else {

            var builder = new StringBuilder();

            for (int i = 0; i < args.length; i++) {
                builder.append(args[i]).append(" ");
            }

            var name = builder.toString();

            if (name.trim().isEmpty()) {
                userInteraction.sendPrivateMessage(executor, "The name can not be empty");
                return;
            }
            Logger.getLogger("BotLogger").info("Changing my name to: " + name);
            userInteraction.sendPrivateMessage(executor, "Changed name to: " + name);
            batoidea.getTeamspeakClient().setNickname(name);
        }
    }
}
