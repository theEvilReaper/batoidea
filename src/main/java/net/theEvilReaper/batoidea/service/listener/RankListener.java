package net.theEvilReaper.batoidea.service.listener;

import com.github.manevolent.ts3j.event.ServerGroupClientAddedEvent;
import com.github.manevolent.ts3j.event.ServerGroupClientDeletedEvent;
import com.github.manevolent.ts3j.event.TS3Listener;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class RankListener implements TS3Listener {

    @Override
    public void onServerGroupClientAdded(ServerGroupClientAddedEvent e) {
        TS3Listener.super.onServerGroupClientAdded(e);
    }

    @Override
    public void onServerGroupClientDeleted(ServerGroupClientDeletedEvent e) {
        TS3Listener.super.onServerGroupClientDeleted(e);
    }
}
