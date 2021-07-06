package net.theEvilReaper.bot.api.provider;

import com.github.manevolent.ts3j.api.Channel;
import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public interface IClientProvider {

    Client getClientByUniqueIdentifier(String identifier);

}
