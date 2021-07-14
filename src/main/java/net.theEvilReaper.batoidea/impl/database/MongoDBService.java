package net.theEvilReaper.batoidea.impl.database;

import net.theEvilReaper.batoidea.impl.user.TeamSpeakUser;
import net.theEvilReaper.bot.api.Connectable;
import net.theEvilReaper.bot.api.config.Config;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class MongoDBService implements Connectable {

    private final Config mongoConfig;

    public MongoDBService(Config config) {
        this.mongoConfig = config;
    }

    @Override
    public void connect() {
       /* MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(new ServerAddress(OaseDataManager.getInstance().getPassword(DataType.HOST), 27017))))
                .build();*/
    }

    public synchronized TeamSpeakUser loadUser(int databaseID) {
        return null;
    }

    @Override
    public void disconnect() {
    }

    @Override
    public boolean isConnected() {
        return false;
    }
}
