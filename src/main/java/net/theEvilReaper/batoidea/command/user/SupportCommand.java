package net.theEvilReaper.batoidea.command.user;

import net.theEvilReaper.batoidea.Batoidea;
import net.theEvilReaper.batoidea.service.SupportService;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.command.CommandSender;
import net.theEvilReaper.bot.api.interaction.ChannelInteraction;
import net.theEvilReaper.bot.api.interaction.InteractionType;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SupportCommand extends Command {

    private final UserInteraction userInteraction;
    private ChannelInteraction channelInteraction;
    private final SupportService supportService;

    public SupportCommand(Batoidea bot, SupportService supportService) {
        super(bot.getInteractionFactory(), "support");
        this.supportService = supportService;
        this.userInteraction = bot.getInteractionFactory().getInteraction(InteractionType.CLIENT, UserInteraction.class);
        // this.channelInteraction = (ChannelInteraction) interactions[1];
        this.setUsage("!support <open,close,join,remove, status>");
    }

    @Override
    public void apply(@NotNull CommandSender sender, @NotNull String command, @Nullable String... args) {
        if (args.length != 1) {
            sender.sendMessage("Wrong Syntax. Use " + getUsage());
            return;
        }

        switch (args[0]) {
            case "status":
               // supportService.changeChannelStatus();
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
                sender.sendMessage("Wrong Syntax. Use " + getUsage());
                break;
        }
    }
}
