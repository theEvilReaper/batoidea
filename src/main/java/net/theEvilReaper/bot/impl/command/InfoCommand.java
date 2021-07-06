package net.theEvilReaper.bot.impl.command;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.interaction.Interaction;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
import net.theEvilReaper.bot.impl.Batoidea;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;

public class InfoCommand extends Command {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("hh:mm:ss");

    private final Batoidea batoidea;
    private final UserInteraction userInteraction;

    public InfoCommand(Batoidea batoidea, Interaction... interaction) {
        this.batoidea = batoidea;
        this.userInteraction = (UserInteraction) interaction[0];
    }

    @Override
    public void onCommand(@NotNull Client executor, @NotNull String command, @Nullable String... args) {
        var now = System.currentTimeMillis();
        userInteraction.sendPrivateMessage(executor, "The bot started at: " + batoidea.getStarted().toString());
    }
}
