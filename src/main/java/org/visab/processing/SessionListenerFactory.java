package org.visab.processing;

import java.util.UUID;

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

    public static void addListener(UUID sessionId, String game) {
        // TODO: This is more of a sanity check, that can be removed when deploying
        if (SessionListenerAdministration.getSessionListener(sessionId) == null) {
            var newListener = AssignByGame.getListenerInstanceByGame(game, sessionId);
            SessionListenerAdministration.addListener(newListener);
            // Notify the listener that the session started
            newListener.onSessionStarted();
        } else {
            System.out.println("TRIED TO ADD SAME UUID SESSION!!!");
        }
    }

    public SessionListenerFactory() {
        super(SessionOpenedEvent.class);
        WebApi.getEventBus().subscribe((ISubscriber) this);
    }

    @Override
    public void invoke(SessionOpenedEvent event) {
        addListener(event.getSessionId(), event.getGame());
    }
}
