package net.theEvilReaper.batoidea.impl.command;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.batoidea.impl.Batoidea;
import net.theEvilReaper.bot.api.command.Command;
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

    public NickCommand(Batoidea batoidea) {
        super(batoidea.getInteractionFactory());
        this.batoidea = batoidea;
    }

    @Override
    public void onCommand(@NotNull Client executor, @NotNull String command, @Nullable String... args) {
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
