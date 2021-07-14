package net.theEvilReaper.batoidea.service.listener;

import com.github.manevolent.ts3j.event.ServerGroupClientAddedEvent;
import com.github.manevolent.ts3j.event.ServerGroupClientDeletedEvent;
import com.github.manevolent.ts3j.event.TS3Listener;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class GroupListener implements TS3Listener {

    private final int botID;

    public GroupListener(int botID) {
        this.botID = botID;
    }

    @Override
    public void onServerGroupClientAdded(ServerGroupClientAddedEvent e) {
        if (e.getInvokerId() == botID) return;
    }

    @Override
    public void onServerGroupClientDeleted(ServerGroupClientDeletedEvent e) {
        if (e.getInvokerId() == botID) return;
    }
}
