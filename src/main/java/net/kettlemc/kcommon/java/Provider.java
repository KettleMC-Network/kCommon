package net.kettlemc.kcommon.java;

/**
 * A provider for a generic type.
 * Used for providing instances of a type with optional arguments.
 *
 * @param <T> The type to provide.
 */
@FunctionalInterface
public interface Provider<T> {

    /**
     * Gets an instance of the type.
     *
     * @param args The arguments to use when getting the instance.
     * @return The instance.
     */
    T get(Object... args);

}
