package net.theEvilReaper.batoidea.user;

import net.theEvilReaper.bot.api.interaction.UserInteraction;
import net.theEvilReaper.bot.api.user.IUserService;
import net.theEvilReaper.bot.api.user.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserService implements IUserService<TeamSpeakUser> {

    private final Map<Integer, TeamSpeakUser> userMap;
    private final UserInteraction userInteraction;

    public UserService(UserInteraction userInteraction) {
        this.userInteraction = userInteraction;
        this.userMap = new HashMap<>();
    }

    @Override
    public void add(@NotNull TeamSpeakUser user) {
        this.userMap.put(user.getID(), user);
    }

    @Override
    public void remove(int clientID) {
        this.userMap.remove(clientID);
    }

    public void updateChannel(int clientID, int newChannel) {
        var user = this.userMap.get(clientID);

        if (user == null) return;

        user.setCurrentChannel(newChannel);
    }

    @Override
    @Nullable
    public User getUser(int clientID) {
        return userMap.get(clientID);
    }

    @Override
    public void setReady(boolean ready) {
        throw new RuntimeException("Not implemented for the user service");
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
        return "UserService";
    }

    /**
     * Returns a Map which contains all current user which are online.
     * @return the map which contains the user
     */

    @Override
    public Map<Integer, TeamSpeakUser> getUser() {
        return Collections.unmodifiableMap(this.userMap);
    }
}
