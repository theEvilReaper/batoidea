package net.theevilreaper.batoidea.listener;

import com.github.manevolent.ts3j.event.ClientJoinEvent;
import com.github.manevolent.ts3j.event.ClientLeaveEvent;
import com.github.manevolent.ts3j.event.TS3Listener;
import net.theevilreaper.batoidea.user.TeamSpeakUser;
import net.theevilreaper.bot.api.provider.IClientProvider;
import net.theevilreaper.bot.api.user.IUserService;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class ClientListener implements TS3Listener {

    private final IClientProvider clientProvider;
    private final IUserService<TeamSpeakUser> iUserService;
    private final int botID;

    public ClientListener(@NotNull IClientProvider clientProvider, @NotNull IUserService<TeamSpeakUser> userService, int botID) {
        this.clientProvider = clientProvider;
        this.iUserService = userService;
        this.botID = botID;
    }

    @Override
    public void onClientJoin(ClientJoinEvent event) {
        if (event.getClientId() == botID) return;

        var client = clientProvider.getClientById(event.getClientId());

        if (client == null) {
            Logger.info("A client was null");
            return;
        }

        Logger.info("The user {} joined the server", client.getNickname());
        clientProvider.add(client);
    }

    @Override
    public void onClientLeave(ClientLeaveEvent event) {
        if (event.getClientId() == botID) return;
        Logger.info("The user with the id: {} left the server", event.getClientId());
        clientProvider.remove(event.getClientId());
        iUserService.remove(event.getClientId());
    }
}