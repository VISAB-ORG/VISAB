package org.visab.util;

import java.util.function.Predicate;

/**
 * Helper class for wrapping around the java stream library. Miserably attempts
 * to mimic C# Linq.
 */
public final class StreamUtil {

    /**
     * Returns whether an iterable contains an item for which the given predicate
     * returns true.
     * 
     * @param <T>       The generic type of the iterable
     * @param iterable  The iterable
     * @param predicate The predicate
     * @return True if the iterable contains the item
     */
    public static <T> boolean contains(Iterable<T> iterable, Predicate<T> predicate) {
        for (var item : iterable) {
            if (predicate.test(item))
                return true;
        }

        return false;
    }

    /**
     * Returns the first item from an iterable for which the given predicate returns
     * true. If no item is found null is returned instead.
     * 
     * @param <T>       The generic type of the items
     * @param iterable  The iterable
     * @param predicate The predicate
     * @return The first found item if found in iterable, null else
     */
    public static <T> T firstOrNull(Iterable<T> iterable, Predicate<T> predicate) {
        for (var item : iterable) {
            if (predicate.test(item))
                return item;
        }

        return null;
    }

}
