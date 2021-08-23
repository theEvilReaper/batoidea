package net.theEvilReaper.batoidea.service;

import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.command.SingleCommand;
import com.github.manevolent.ts3j.command.parameter.CommandSingleParameter;
import com.github.manevolent.ts3j.protocol.ProtocolRole;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theEvilReaper.batoidea.interaction.ClientInteraction;
import net.theEvilReaper.batoidea.user.TeamSpeakUser;
import net.theEvilReaper.bot.api.database.IRedisEventManager;
import net.theEvilReaper.bot.api.database.events.RTeamSpeakSupportEvent;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
import net.theEvilReaper.bot.api.service.IService;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class SupportService implements IService {

    private static final RTeamSpeakSupportEvent event = new RTeamSpeakSupportEvent();

    private final String[] states = new String[]{"[OPEN]", "[CLOSED]"};
    private final CommandSingleParameter channelParameter;
    private final UserInteraction userInteraction;
    private final Map<Integer, TeamSpeakUser> supporter;
    private final int[] channelIDs;
    private final IRedisEventManager iRedisEventManager;
    private boolean open;

    public SupportService(ClientInteraction clientInteraction, int channelID, int afkChannel) {
        this.userInteraction = clientInteraction;
        this.channelIDs = new int[]{channelID, afkChannel};
        this.supporter = new HashMap<>();
        this.channelParameter = new CommandSingleParameter("cid" , Integer.toString(channelID));
        this.iRedisEventManager = null;
    }

    public void notifySupporter() {
        if (supporter.isEmpty()) return;

        for (TeamSpeakUser value : supporter.values()) {
            if (value.getClient().getChannelId() == getAfkChannel()) continue;
            userInteraction.sendPrivateMessage(value.getClient(), "Someone joins the support channel");
            iRedisEventManager.callEvent(event);
        }
    }

    public void add(@NotNull Client client) {
        this.supporter.putIfAbsent(client.getId(), TeamSpeakUser.of(client));
    }

    public void remove(int clientID) {
        this.supporter.remove(clientID);
    }

    public boolean changeChannelStatus(LocalTeamspeakClientSocket socket) {
        this.open = !open;
        var command = buildCommand(open);
        try {
            var result = socket.executeCommand(command);
            for (SingleCommand singleCommand : result.get()) {
                System.out.println(singleCommand.toString());
            }
        } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
            exception.printStackTrace();
        }
        return open;
    }

    private SingleCommand buildCommand(boolean status) {
        var singleCommand = new SingleCommand("channeledit",  ProtocolRole.CLIENT);
        singleCommand.add(channelParameter);
        singleCommand.add(new CommandSingleParameter("channel_name", "Support " + (status ? states[0] : states[1])));
        singleCommand.add(new CommandSingleParameter("i_channel_needed_join_power", Integer.toString(85)));
        singleCommand.add(new CommandSingleParameter("i_channel_needed_subscribe_power", Integer.toString(80)));
        return singleCommand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupportService that = (SupportService) o;
        return Arrays.equals(channelIDs, that.channelIDs);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(channelIDs);
    }

    @Override
    public void setReady(boolean ready) {
        throw new UnsupportedOperationException("Not implemented for the support service");
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public String getName() {
        return "SupportService";
    }

    /**
     * Returns if the channel is open or not.
     * @return true when the channel is open otherwise false
     */

    public boolean isOpen() {
        return open;
    }

    /**
     * Returns the channel id from the support channel.
     * @return The given channel id
     */

    public int getChannelID() {
        return channelIDs[0];
    }

    /**
     * Returns the id from the afk channel to ignore supporter in the specific channel.
     * @return the id from the akf channel
     */

    public int getAfkChannel() {
        return channelIDs[1];
    }
}