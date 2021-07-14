package net.theEvilReaper.batoidea.impl.command;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.interaction.AbstractInteractionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InfoCommand extends Command {

    public InfoCommand(AbstractInteractionFactory factory) {
        super(factory);
    }

    @Override
    public void onCommand(@NotNull Client executor, @NotNull String command, @Nullable String... args) {
    }
}
