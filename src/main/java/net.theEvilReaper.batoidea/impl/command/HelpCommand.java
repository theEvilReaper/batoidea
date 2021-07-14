package net.theEvilReaper.batoidea.impl.command;

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

public class HelpCommand extends Command {

    public HelpCommand(AbstractInteractionFactory factory) {
        super(factory);
    }

    @Override
    public void onCommand(@NotNull Client executor, @NotNull String command, @Nullable String... args) {
        userInteraction.sendPrivateMessage(executor, "Currently i don't have any help");
    }
}
