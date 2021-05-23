package org.visab.processing;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.WebApi;
import org.visab.dynamic.DyanmicInstatiator;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.subscriber.SubscriberBase;

/**
 * The SessionListenerFactory that will create new SessionListeners whenever a
 * new transmission session is opened.
 *
 * @author moritz
 *
 */
public class SessionListenerFactory extends SubscriberBase<SessionOpenedEvent> {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(SessionListenerFactory.class);

    public void addListener(UUID sessionId, String game) {
        var newListener = DyanmicInstatiator.instantiateSessionListener(game, sessionId);
        SessionListenerAdministration.addListener(newListener);
        // Notify the listener that the session started
        newListener.onSessionStarted();
    }

    private boolean isStarted;

    /**
     * Starts the Factory by adding it to the EventBus
     */
    public void startFactory() {
        if (!isStarted) {
            WebApi.instance.getEventBus().subscribe(this);
            isStarted = true;
        }
    }

    /**
     * Stops the factory, by unsubscribing it from the eventbus
     */
    public void stopFactory() {
        if (isStarted) {
            WebApi.instance.getEventBus().unsubscribe(this);
            isStarted = false;
        }
    }

    public SessionListenerFactory() {
        super(SessionOpenedEvent.class);
    }

    @Override
    public void notify(SessionOpenedEvent event) {
        addListener(event.getSessionId(), event.getGame());
    }
}
