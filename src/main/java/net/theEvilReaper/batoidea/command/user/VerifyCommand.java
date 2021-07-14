package net.theEvilReaper.batoidea.command.user;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.interaction.AbstractInteractionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class VerifyCommand extends Command {

    public VerifyCommand(AbstractInteractionFactory factory) {
        super(factory);
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
