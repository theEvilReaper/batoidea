package net.theEvilReaper.batoidea.database.impl;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import net.theEvilReaper.batoidea.database.AbstractMongoProcessor;
import net.theEvilReaper.bot.api.database.MongoConnector;
import net.theEvilReaper.bot.api.database.model.MongoModel;
import org.bson.UuidRepresentation;
import org.jetbrains.annotations.NotNull;

/**
 * The class represents the implementation for the {@link AbstractMongoProcessor}.
 * The class needs an T reference which binds an specific object to the implementation
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class MongoProcessorImpl<T extends MongoModel> extends AbstractMongoProcessor<T> {

    protected Datastore datastore;

    /**
     * Creates a new instance from the {@link MongoProcessorImpl} with the given values.
     *
     * @param connector  An valid instance from the {@link MongoConnector}
     * @param clazz      The T reference which binds an specific object to the class
     * @param collection The collection to access the data
     * @param database   The database where the objects are stored
     */

    public MongoProcessorImpl(@NotNull MongoConnector connector, @NotNull Class<T> clazz, @NotNull String collection, @NotNull String database) {
        super(connector, clazz, collection, database);
        this.datastore = Morphia.createDatastore(connector.getConnection(), database, MapperOptions.builder().uuidRepresentation(UuidRepresentation.JAVA_LEGACY).build());
    }

    /**
     * Inserts some data to the database.
     *
     * @param t The object to add
     */

    @Override
    public void insert(T t) {
        this.datastore.insert(t);
    }

    /**
     * Deletes some data in the database.
     *
     * @param document The document to remove
     */

    @Override
    public void delete(T document) {
        this.datastore.delete(document);
    }

    /**
     * Updates one given data in the database.
     */

    @Override
    public void update(T model) {
        this.datastore.save(model);
    }

    @Override
    public Datastore getDatastore() {
        return this.datastore;
    }
}
