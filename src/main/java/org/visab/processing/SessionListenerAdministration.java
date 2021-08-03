package org.visab.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The SessionListenerAdministration that holds a reference to all active
 * SessionListeners.
 */
public final class SessionListenerAdministration {

    private static List<ISessionListener> activeListeners = new ArrayList<>();

    /**
     * Returns a list of the currently active listeners.
     * 
     * @return A copy of the list of the currently active listeners
     */
    public static List<ISessionListener> getActiveListeners() {
        return new ArrayList<ISessionListener>(activeListeners);
    }

    /**
     * Returns a list of the currently active listeners filtered by their game.
     * 
     * @param game The game of which to get the listeners
     * @return The list of listeners
     */
    public static List<ISessionListener> getActiveListeners(String game) {
        return activeListeners.stream().filter(x -> x.getGame() == game).collect(Collectors.toList());
    }

    /**
     * Gets a session listener based on a given sessionId.
     * 
     * @param sessionId The sessionId to get the listener for
     * @return The session listener if found, null else
     */
    public static ISessionListener getSessionListener(UUID sessionId) {
        for (var listener : activeListeners) {
            if (listener.getSessionId().equals(sessionId))
                return listener;
        }

        return null;
    }

    /**
     * Adds a listener to the list of active listeners. Should only be called by the
     * SessionListenerFactory.
     * 
     * @param listener The listener to add
     */
    protected static void addListener(ISessionListener listener) {
        if (listener != null)
            activeListeners.add(listener);
    }

    /**
     * Removes a listener from the list of active listeners. Should only be called
     * from the session listeners themselves.
     * 
     * @param listener The listener to remove
     */
    protected static void removeListener(ISessionListener listener) {
        activeListeners.remove(listener);
    }
}
