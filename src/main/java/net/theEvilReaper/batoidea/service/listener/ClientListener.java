package net.theEvilReaper.batoideas.service.listener;

import com.github.manevolent.ts3j.event.ClientJoinEvent;
import com.github.manevolent.ts3j.event.ClientLeaveEvent;
import com.github.manevolent.ts3j.event.TS3Listener;
import net.theEvilReaper.bot.api.provider.IClientProvider;

import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class ClientListener implements TS3Listener {

    private static final Pattern BOT_PATTER = Pattern.compile("(\\[.*\\])(.*)");
    private final Logger logger;
    private final IClientProvider clientProvider;
    private final int botID;

    public ClientListener(IClientProvider clientProvider, Logger logger, int botID) {
        this.logger = logger;
        this.clientProvider = clientProvider;
        this.botID = botID;
    }

    @Override
    public void onClientJoin(ClientJoinEvent event) {
        if (event.getClientId() == botID) return;

        var client = clientProvider.getClientById(event.getClientId());

        if (client == null) {
            logger.info("A client was null");
            return;
        }

        logger.info("The user " + client.getNickname() + " joined the server");
        clientProvider.add(client);
    }

    @Override
    public void onClientLeave(ClientLeaveEvent event) {
        if (event.getClientId() == botID) return;
        logger.info("The user with the id: " + event.getClientId()
                + " left the server. Reason: " + event.getReasonMessage());
        clientProvider.remove(event.getClientId());
    }
}