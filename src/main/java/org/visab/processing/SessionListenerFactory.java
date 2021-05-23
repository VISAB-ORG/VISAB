package org.visab.processing;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.WebApi;
import org.visab.dynamic.DyanmicInstatiator;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.subscriber.SubscriberBase;

/**
 * The SessionListenerFactory that will instantiate new SessionListeners
 * whenever a new transmission session is opened at the WebApi.
 *
 * @author moritz
 *
 */
public class SessionListenerFactory extends SubscriberBase<SessionOpenedEvent> {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(SessionListenerFactory.class);

    /**
     * Instantiates a new listener based on the given game and adds it to the
     * SessionListenerAdministration.
     * 
     * @param sessionId The sessionId
     * @param game      The game for which to instantiate a listener
     */
    public void addListener(UUID sessionId, String game) {
        var newListener = DyanmicInstatiator.instantiateSessionListener(game, sessionId);
        SessionListenerAdministration.addListener(newListener);
        // Notify the listener that the session started
        newListener.onSessionStarted();
    }

    /**
     * Whether the factory is started
     */
    private boolean isStarted;

    /**
     * Starts the Factory by subscribing it to the EventBus
     */
    public void startFactory() {
        if (!isStarted) {
            WebApi.instance.getEventBus().subscribe(this);
            isStarted = true;
        }
    }

    /**
     * Stops the factory by unsubscribing it from the eventbus
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
