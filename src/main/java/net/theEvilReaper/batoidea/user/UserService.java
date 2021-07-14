package net.theEvilReaper.batoidea.user;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.bot.api.user.IUserService;
import net.theEvilReaper.bot.api.user.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class UserService implements IUserService {

    private final Map<Integer, User> userMap;

    public UserService() {
        this.userMap = new HashMap<>();
    }

    @Override
    public void addUser(@NotNull Client client) {
        this.userMap.putIfAbsent(client.getId(), TeamSpeakUser.of(client));
    }

    @Override
    public void removeUser(int clientID) {
        this.userMap.remove(clientID);
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

    @Override
    public Map<Integer, User> getUser() {
        return userMap;
    }
}
