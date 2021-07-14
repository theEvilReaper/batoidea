package net.theEvilReaper.batoideas.service;

import com.github.manevolent.ts3j.api.Client;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.theEvilReaper.batoideas.interaction.InteractionFactory;
import net.theEvilReaper.bot.api.interaction.GroupInteraction;
import net.theEvilReaper.bot.api.interaction.InteractionType;
import net.theEvilReaper.bot.api.provider.IClientProvider;
import net.theEvilReaper.bot.api.service.IService;
import net.theEvilReaper.bot.api.user.IUserService;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class RankService implements IService {

    private final BiMap<Integer, Integer> watchedGroup;
    private final GroupInteraction groupInteraction;
    private final IClientProvider clientProvider;
    private final IUserService iUserService;
    private final Logger logger;
    private final Lock lock;

    public RankService(IClientProvider clientProvider, IUserService iUserService, InteractionFactory interactionFactory) {
        this.clientProvider = clientProvider;
        this.groupInteraction = interactionFactory.getInteraction(InteractionType.GROUP, net.theEvilReaper.bot.api.interaction.GroupInteraction.class);
        this.iUserService = iUserService;
        this.logger = Logger.getLogger("BotLogger");
        this.watchedGroup = HashBiMap.create();
        this.lock = new ReentrantLock();
    }

    public void updateRanks() {
        logger.log(Level.INFO, "Updating ranks");
        lock.lock();
        try {
            watchedGroup.clear();
        } finally {
            lock.unlock();
        }
    }

    public void changeRank(@NotNull Client client, int newGroup) {
        var teamspeakUser = iUserService.getUser(client.getId());

        if (teamspeakUser == null || !teamspeakUser.isVerified()) return;

        //Getting proper client so that we can look at his server groups
        client = clientProvider.getClientById(client.getId());

        lock.lock();
        try {
            var groupID = watchedGroup.inverse().get(teamspeakUser.getGroupID());

            if (groupID != null && groupInteraction.isInServerGroup(client, groupID)) {
                removeToGroup(client, groupID);
            }

            var newGroupId = watchedGroup.inverse().get(newGroup);
            if (newGroupId != null && !groupInteraction.isInServerGroup(client, newGroupId)) {
                addToGroup(client, newGroupId);
            }

        } finally {
            lock.unlock();
        }
    }

    public void checkRank(@NotNull Client client) {
        if (client.getUniqueIdentifier() == null) {
            client = clientProvider.getClientById(client.getId());
        }

        try {
            lock.lock();
            var teamspeakUser = iUserService.getUser(client.getId());

            if (teamspeakUser == null) {
                throw new IllegalStateException("There is an error. A player has no group");
            }

            var rankGroup = watchedGroup.inverse().get(teamspeakUser.getGroupID());

            if (rankGroup == null)
                return;

            var groups = client.getServerGroups();

            for (int group : groups) {
                if (group != rankGroup && watchedGroup.containsKey(group)) {
                    addToGroup(client, rankGroup);
                }
            }

            if (!groupInteraction.isInServerGroup(client, rankGroup)) {
                removeToGroup(client, rankGroup);
            }
        } finally {
            lock.unlock();
        }
    }

    private void addToGroup(@NotNull Client client, int rankGroup) {
        try {
            groupInteraction.removeServerGroup(client, rankGroup);
        } catch (Exception exception) {
            logger.log(Level.WARNING, "An exception occurred whilst removing" +
                    client.getNickname() + "from server group " + rankGroup, exception);
        }
    }

    private void removeToGroup(@NotNull Client client, int rankGroup) {
        try {
            groupInteraction.removeServerGroup(client, rankGroup);
        } catch (Exception exception) {
            logger.log(Level.WARNING, "An exception occurred whilst adding" +
                    client.getNickname() + "from server group " + rankGroup, exception);
        }
    }

    @Override
    public void setReady(boolean ready) {
        throw new RuntimeException("Not available for the rank service");
    }

    /**
     * Returns if the service is ready.
     * @return the underlying boolean value
     */

    @Override
    public boolean isReady() {
        return true;
    }

    /**
     * Returns the name of the service.
     * @return the given name
     */

    @Override
    public String getName() {
        return "RankService";
    }
}
