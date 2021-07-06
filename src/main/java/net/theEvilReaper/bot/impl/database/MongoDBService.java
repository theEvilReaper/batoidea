package net.theEvilReaper.bot.impl.database;
import net.theEvilReaper.bot.api.Connectable;
import net.theEvilReaper.bot.api.config.Config;
import net.theEvilReaper.bot.api.service.Service;

import java.util.Collections;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class MongoDBService extends Service implements Connectable {

    private final Config mongoConfig;

    public MongoDBService(Config config) {
        super("MongoDB", -1);
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

    @Override
    public void disconnect() {
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    protected void update() {

    }
}
