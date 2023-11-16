package net.kettlemc.kcommon.data;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Future;

/**
 * Interface for data handlers that save and load entities
 *
 * @param <T> the type of entity
 */
public interface DataHandler<T> {

    /**
     * Saves an entity to the database
     *
     * @param entity the entity to save
     * @return a future containing the entity
     */
    Future<T> save(@NotNull T entity);

    /**
     * Loads an entity from the database and creates a new one if it doesn't exist
     *
     * @param uuid the uuid of the entity
     * @return a future containing the entity
     */
    Future<T> load(@NotNull String uuid);

    /**
     * Loads an entity from the database
     *
     * @param uuid              the uuid of the entity
     * @param createIfNotExists if true, a new entity will be created if it doesn't exist
     * @return a future containing the entity
     */
    Future<T> load(@NotNull String uuid, boolean createIfNotExists);

    /**
     * Initializes the data handler
     *
     * @return true if successful
     */
    boolean initialize();

    /**
     * Checks if the data handler is initialized
     *
     * @return true if initialized
     */
    boolean initialized();

    /**
     * Closes the data handler
     */
    void close();

}
