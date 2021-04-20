package eventbus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import processing.SessionListener;
import processing.CBRShooterListener;

public class SessionListenerFactory implements ISubscriber<SessionOpenedEvent> {
	
	private static Collection<SessionListener> activeListeners = new ArrayList<SessionListener>();
	
	public void invoke(SessionOpenedEvent event) {
		addListener(event.getSessionId(), event.getGame());
	}
	
	public static void addListener(UUID sessionId, String game) {
		// TODO: Switch to get type for game
		
		var listener = new CBRShooterListener(game, sessionId);
		activeListeners.add(listener);
	}
	
	public static void removeListener(SessionListener listener) {
		activeListeners.remove(listener);
	}
	
	public static Collection<SessionListener> getActiveListeners() {
		return activeListeners;
	}
}
