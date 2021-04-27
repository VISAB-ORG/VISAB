package org.visab.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SessionListenerAdministration {

    private static List<ISessionListener> activeListeners = new ArrayList<>();

    /**
     * Returns a list of the currently active listeners. Warning: Does not return
     * the reference to activeListeners, so don't try modifying the listeners via
     * this.
     * 
     * @return A copy of the currently active listeners
     */
    public static List<ISessionListener> getActiveListeners() {
        return new ArrayList<ISessionListener>(activeListeners);
    }

    public static ISessionListener getSessionListener(UUID sessionId) {
        var listeners = getActiveListeners(sessionId);

        return listeners.size() > 0 ? listeners.get(0) : null;
    }

    public static List<ISessionListener> getActiveListeners(String game) {
        return activeListeners.stream().filter(x -> x.getGame() == game).collect(Collectors.toList());
    }

    public static void addListener(ISessionListener listener) {
        activeListeners.add(listener);
    }

    public static List<ISessionListener> getActiveListeners(UUID sessionId) {
        return activeListeners.stream().filter(x -> x.getSessionId() == sessionId).collect(Collectors.toList());
    }

    // TODO: Remove gracefully?
    public static void removeListener(ISessionListener listener) {
        activeListeners.remove(listener);
    }
}
