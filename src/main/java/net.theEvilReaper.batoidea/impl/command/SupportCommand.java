package net.theEvilReaper.batoidea.impl.command;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.batoidea.impl.Batoidea;
import net.theEvilReaper.batoidea.impl.interaction.ClientInteraction;
import net.theEvilReaper.batoidea.service.SupportService;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.interaction.ChannelInteraction;
import net.theEvilReaper.bot.api.interaction.InteractionType;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
import org.jetbrains.annotations.NotNull;

public class SupportCommand extends Command {

    private final UserInteraction userInteraction;
    private ChannelInteraction channelInteraction;
    private final SupportService supportService;

    public SupportCommand(Batoidea bot, SupportService supportService) {
        super(bot.getInteractionFactory());
        this.supportService = supportService;
        this.userInteraction = bot.getInteractionFactory().getInteraction(InteractionType.CLIENT, ClientInteraction.class);
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
               // var client = batoidea.getClient(executor);
               // supportService.add(client);
                break;
            case "remove":
              //  var sss = batoidea.getClient(executor);//supportService.remove(sss.getId());
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
