package net.theevilreaper.batoidea.user;

import net.theevilreaper.bot.api.user.IUserService;
import net.theevilreaper.bot.api.user.User;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserService<T extends User> implements IUserService<T> {

    private final Map<Integer, T> userMap;

    public UserService() {
        this.userMap = new HashMap<>();
    }

    @Override
    public void add(@NotNull T user) {
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
        throw new NotImplementedException("Not implemented for the user service");
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
    public Map<Integer, T> getUser() {
        return Collections.unmodifiableMap(this.userMap);
    }
}
