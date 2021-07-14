package net.theEvilReaper.batoideas.service.listener;

import com.github.manevolent.ts3j.api.Channel;
import com.github.manevolent.ts3j.command.CommandException;
import com.github.manevolent.ts3j.event.ChannelCreateEvent;
import com.github.manevolent.ts3j.event.ChannelDeletedEvent;
import com.github.manevolent.ts3j.event.TS3Listener;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theEvilReaper.bot.api.provider.IChannelProvider;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class ChannelListener implements TS3Listener {

    private final Logger logger;
    private final IChannelProvider channelProvider;
    private final LocalTeamspeakClientSocket localTeamspeakClientSocket;

    public ChannelListener(Logger logger, IChannelProvider channelProvider, LocalTeamspeakClientSocket teamspeakClientSocket) {
        this.logger = logger;
        this.channelProvider = channelProvider;
        this.localTeamspeakClientSocket = teamspeakClientSocket;
    }

    @Override
    public void onChannelCreate(ChannelCreateEvent event) {
        try {
            for (Channel current : localTeamspeakClientSocket.listChannels()) {
                if (current.getId() != event.getChannelId()) continue;
                channelProvider.add(current);
                logger.info("Added Channel: " + current.getName());
            }
        } catch (IOException | CommandException | InterruptedException | TimeoutException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onChannelDeleted(ChannelDeletedEvent event) {
        logger.info("Removed Channel: " + event.getChannelId());
        this.channelProvider.removeChannel(event.getChannelId());
    }
}
