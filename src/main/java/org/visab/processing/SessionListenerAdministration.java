package org.visab.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The SessionListenerAdministration that holds a reference to all active
 * SessionListeners.
 * 
 * @author moritz
 * 
 */
public final class SessionListenerAdministration {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(SessionListenerAdministration.class);

    private static List<ISessionListener<?>> activeListeners = new ArrayList<>();

    /**
     * Returns a list of the currently active listeners. Warning: Does not return
     * the reference to activeListeners, so don't try modifying the listeners via
     * this.
     * 
     * @return A copy of the currently active listeners
     */
    public static List<ISessionListener<?>> getActiveListeners() {
        return new ArrayList<ISessionListener<?>>(activeListeners);
    }

    public static ISessionListener<?> getSessionListener(UUID sessionId) {
        for (var listener : activeListeners) {
            if (listener.getSessionId().equals(sessionId))
                return listener;
        }

        return null;
    }

    public static List<ISessionListener<?>> getActiveListeners(String game) {
        return activeListeners.stream().filter(x -> x.getGame() == game).collect(Collectors.toList());
    }

    public static void addListener(ISessionListener<?> listener) {
        activeListeners.add(listener);
    }

    // TODO: Remove gracefully?
    public static void removeListener(ISessionListener<?> listener) {
        activeListeners.remove(listener);
    }
}
