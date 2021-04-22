package processing.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import eventbus.event.SessionOpenedEvent;
import eventbus.subscriber.SubscriberBase;
import processing.ISessionListener;

/**
 * The SessionListenerFactory that will create new SessionListeners whenever a
 * new transmission session is opened.
 *
 * @author moritz
 *
 */
public class SessionListenerFactory extends SubscriberBase<SessionOpenedEvent> {

    private static List<ISessionListener> activeListeners = new ArrayList<>();

    public static void addListener(UUID sessionId, String game) {
	var listener = getListenerInstanceByGame(game, sessionId);
	activeListeners.add(listener);
    }

    public static List<ISessionListener> getActiveListeners() {
	return activeListeners;
    }

    public static List<ISessionListener> getActiveListeners(String game) {
	return activeListeners.stream().filter(x -> x.getGame() == game).collect(Collectors.toList());
    }

    public static List<ISessionListener> getActiveListeners(UUID sessionId) {
	return activeListeners.stream().filter(x -> x.getSessionId() == sessionId).collect(Collectors.toList());
    }

    private static ISessionListener getListenerInstanceByGame(String game, UUID sessionId) {
	// TODO: Load the allowed game names at VISAB start from YAML file
	switch (game) {
	case "CBRShooter":
	    return new CBRShooterListener(game, sessionId);
	default:
	    // TODO: Raise exception
	    return null;
	}
    }

    public static void removeListener(ISessionListener listener) {
	activeListeners.remove(listener);
    }

    public SessionListenerFactory() {
	super(new SessionOpenedEvent(null, null).getClass().getSimpleName());
    }

    @Override
    public void invoke(SessionOpenedEvent event) {
	addListener(event.getSessionId(), event.getGame());
    }
}
