package org.visab.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.visab.api.WebApi;
import org.visab.eventbus.ISubscriber;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.subscriber.SubscriberBase;
import org.visab.util.AssignByGame;

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
	var listener = AssignByGame.getListenerInstanceByGame(game, sessionId);
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

    public static void removeListener(ISessionListener listener) {
	activeListeners.remove(listener);
    }

    public SessionListenerFactory() {
	super(new SessionOpenedEvent(null, null).getClass().getSimpleName());
	WebApi.getEventBus().subscribe((ISubscriber) this);
    }

    @Override
    public void invoke(SessionOpenedEvent event) {
	addListener(event.getSessionId(), event.getGame());
    }
}
