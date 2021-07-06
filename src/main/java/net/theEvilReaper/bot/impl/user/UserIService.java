package net.theEvilReaper.bot.impl.user;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.bot.api.user.IUserIService;
import net.theEvilReaper.bot.api.user.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class UserIService implements IUserIService {

    private final Map<Integer, User> userMap;

    public UserIService() {
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
}
