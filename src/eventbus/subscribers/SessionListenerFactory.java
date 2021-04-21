package eventbus.subscribers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import eventbus.event.SessionOpenedEvent;
import processing.listener.CBRShooterListener;

public class SessionListenerFactory extends SubscriberBase<SessionOpenedEvent> {

    private static Collection<ISessionListener> activeListeners = new ArrayList<>();

    public static void addListener(UUID sessionId, String game) {
	// TODO: Switch to get type for game

	var listener = new CBRShooterListener(game, sessionId);
	activeListeners.add((ISessionListener) listener);
    }

    public static Collection<ISessionListener> getActiveListeners() {
	return activeListeners;
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
