package net.theEvilReaper.batoideas.service;

import com.github.manevolent.ts3j.api.Channel;
import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.command.SingleCommand;
import com.github.manevolent.ts3j.command.parameter.CommandSingleParameter;
import com.github.manevolent.ts3j.protocol.ProtocolRole;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theEvilReaper.batoideas.user.TeamSpeakUser;
import net.theEvilReaper.bot.api.interaction.ChannelInteraction;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
import net.theEvilReaper.bot.api.service.IService;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class SupportService implements IService {

    private final String[] states = new String[]{"[OPEN]", "[CLOSED]"};
    private final Map<Integer, TeamSpeakUser> supporter;
    private final CommandSingleParameter channelParameter;
    private UserInteraction userInteraction;
    private LocalTeamspeakClientSocket socket;
    private final ChannelInteraction channelInteraction;
    private final int channelID;
    private Channel channel;
    private boolean open;

    public SupportService(ChannelInteraction channelInteraction, int channelID) {
        this.channelInteraction = channelInteraction;
        this.channelID = channelID;
        this.supporter = new HashMap<>();
        this.channelParameter = new CommandSingleParameter("cid" , Integer.toString(channelID));
    }

    public void setSocket(LocalTeamspeakClientSocket socket) {
        this.socket = socket;
    }

    public void setUserInteraction(UserInteraction userInteraction) {
        this.userInteraction = userInteraction;
    }

    public void notifySupporter() {
        if (supporter.isEmpty()) return;

        for (TeamSpeakUser value : supporter.values()) {
            userInteraction.sendPrivateMessage(value.getClient(), "Someone needs support");
        }
    }

    public void add(@NotNull Client client) {
        this.supporter.putIfAbsent(client.getId(), TeamSpeakUser.of(client));
    }

    public void remove(int clientID) {
        this.supporter.remove(clientID);
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean changeChannelStatus() {
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

    public boolean isOpen() {
        return open;
    }

    public int getChannelID() {
        return channelID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SupportService that = (SupportService) o;

        return channelID == that.channelID;
    }

    @Override
    public int hashCode() {
        return channelID;
    }

    @Override
    public void setReady(boolean ready) {

    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public String getName() {
        return "SupportService";
    }

    public Channel getChannel() {
        return channel;
    }
}