package net.theEvilReaper.bot.impl.command;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.interaction.ChannelInteraction;
import net.theEvilReaper.bot.api.interaction.Interaction;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
import net.theEvilReaper.bot.impl.Batoidea;
import net.theEvilReaper.bot.service.SupportService;
import org.jetbrains.annotations.NotNull;

public class SupportCommand extends Command {

    private final UserInteraction userInteraction;
    private ChannelInteraction channelInteraction;
    private final Batoidea batoidea;
    private final SupportService supportService;

    public SupportCommand(Batoidea bot, SupportService supportService, Interaction... interactions) {
        this.batoidea = bot;
        this.supportService = supportService;
        this.userInteraction = (UserInteraction)  interactions[0];
       // this.channelInteraction = (ChannelInteraction) interactions[1];
        this.setUsage("!support <open,close,join,remove, status>");
     }

    @Override
    public void onCommand(@NotNull Client executor, @NotNull String command, String... args) {
        if (args.length != 1) {
            userInteraction.sendPrivateMessage(executor, "Wrong Syntax. Use " + getUsage());
            return;
        }

        switch (args[0]) {
            case "status":
                supportService.changeChannelStatus();
                break;
            case "join":
                var client = batoidea.getClient(executor);
                supportService.add(client);
                break;
            case "remove":
                var sss = batoidea.getClient(executor);
                supportService.remove(sss.getId());
                break;
            case "open":

                break;
            case "close":
                break;
            default:
                userInteraction.sendPrivateMessage(executor, "Wrong Syntax. Use " + getUsage());
                break;
        }
    }
}
