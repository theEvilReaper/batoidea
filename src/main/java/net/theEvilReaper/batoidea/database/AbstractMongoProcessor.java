package net.theEvilReaper.batoidea.database;

import net.theEvilReaper.bot.api.database.MongoConnector;
import net.theEvilReaper.bot.api.database.MongoProcessor;
import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public abstract class AbstractMongoProcessor<T> implements MongoProcessor<T> {

    protected final MongoConnector connector;
    protected final String collection;
    protected final Class<T> clazz;
    protected final String database;

    public AbstractMongoProcessor(@NotNull MongoConnector connector, @NotNull Class<T> clazz, @NotNull String collection, @NotNull String database) {
        this.connector = connector;
        this.collection = collection;
        this.clazz = clazz;
        this.database = database;
    }
}
