package net.theEvilReaper.bot.listener;

import com.github.manevolent.ts3j.api.TextMessageTargetMode;
import com.github.manevolent.ts3j.event.ClientJoinEvent;
import com.github.manevolent.ts3j.event.ClientLeaveEvent;
import com.github.manevolent.ts3j.event.ClientMovedEvent;
import com.github.manevolent.ts3j.event.ServerGroupClientAddedEvent;
import com.github.manevolent.ts3j.event.ServerGroupClientDeletedEvent;
import com.github.manevolent.ts3j.event.TS3Listener;
import com.github.manevolent.ts3j.event.TextMessageEvent;
import net.theEvilReaper.bot.impl.Batoidea;
import net.theEvilReaper.bot.impl.command.UserCommandProvider;
import net.theEvilReaper.bot.impl.interaction.ClientInteraction;
import net.theEvilReaper.bot.service.FollowService;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class TeamSpeakListener implements TS3Listener {

    private static final Pattern SPLIT_PATTERN = Pattern.compile(" ");

    private final Batoidea batoidea;
    private final FollowService followService;
    private final UserCommandProvider userCommandProvider;

    private final ClientInteraction clientInteraction;

    public TeamSpeakListener(Batoidea batoidea, FollowService followService) {
        this.batoidea = batoidea;
        this.clientInteraction = new ClientInteraction(batoidea.getTeamspeakClient());
        this.followService = followService;
        this.userCommandProvider = new UserCommandProvider(batoidea, batoidea.getTeamspeakClient(), followService);
    }

    @Override
    public void onTextMessage(TextMessageEvent event) {
        if (event.getInvokerId() == batoidea.getBotID())
            return; //Ignore our own sent messages

        if (event.getTargetMode() == TextMessageTargetMode.CHANNEL || event.getTargetMode() == TextMessageTargetMode.SERVER)
            return;

        var client = batoidea.getClientById(event.getInvokerId());

        if (client == null) return;


        Batoidea.getBotLogger().log(Level.INFO, "Received message " + event.getMessage() + " from: " + client.getNickname());
        if (event.getMessage().startsWith("!")) {
            var split = SPLIT_PATTERN.split(event.getMessage().replaceFirst("!", ""));
            String command = split[0];
            String[] args = new String[split.length - 1];
            if (split.length > 1) {
                System.arraycopy(split, 1, args, 0, split.length - 1);
            }
            userCommandProvider.dispatch(client, command, args);
        } else {
            Batoidea.getBotLogger().log(Level.INFO, "Received message without the right syntax");
            clientInteraction.sendPrivateMessage(client, "I could not find the command: " + event.getMessage() + "Please type !help for help");
        }
    }

    @Override
    public void onClientJoin(ClientJoinEvent e) {
    }

    @Override
    public void onClientLeave(ClientLeaveEvent e) {
        batoidea.getUpdateLock().lock();
        try {
            batoidea.getClientMap().remove(e.getClientId());
        } finally {
            batoidea.getUpdateLock().unlock();
        }
    }

    @Override
    public void onServerGroupClientAdded(ServerGroupClientAddedEvent e) {
        if (e.getInvokerId() == batoidea.getBotID())
            return;
    }

    @Override
    public void onServerGroupClientDeleted(ServerGroupClientDeletedEvent e) {
        if (e.getInvokerId() == batoidea.getBotID())
            return;
    }

    @Override
    public void onClientMoved(ClientMovedEvent event) {
        var client = batoidea.getClientById(event.getClientId());

        if (client == null) return;

        if (client.getId() == batoidea.getBotID()) {
            return;
        }

        if (!followService.isFollowing()) return;

        var channel = batoidea.findChannelID(event.getTargetChannelId());
        var name = client.getClientURI().split("~")[1];

        if (name.contains("+")) {
            name = name.split("\\+")[0];
        }


        if (name.equals(followService.getFollowing())) {
            if (channel == null) {
                Logger.getLogger("BotLogger").log(Level.WARNING, "Can't find channel by id. Channel ID: " + event.getTargetChannelId());
                return;
            }
            batoidea.getBotInteraction().joinChannel(channel);
        }
    }
}