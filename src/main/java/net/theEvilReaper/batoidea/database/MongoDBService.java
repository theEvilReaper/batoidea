package net.theEvilReaper.batoidea.database;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mongodb.client.MongoClient;
import net.theEvilReaper.batoidea.user.TeamSpeakUser;
import net.theEvilReaper.bot.api.Connectable;
import net.theEvilReaper.bot.api.config.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class MongoDBService implements Connectable {

    private final ExecutorService executorService;
    private final Config mongoConfig;
    private final Logger logger;

    private MongoClient mongoClient;

    public MongoDBService(Logger logger, Config config) {
        this.logger = logger;
        this.mongoConfig = config;

        this.executorService = Executors.newCachedThreadPool(
                new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("mdb-pool-%d")
                .build());
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
        if (mongoClient != null) {
            logger.info("Closing connection to the database");
            mongoClient.close();
        }

        this.executorService.shutdown();
    }

    @Override
    public boolean isConnected() {
        return mongoClient != null;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}
