package org.visab.processing;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.dynamic.DynamicInstatiator;
import org.visab.eventbus.ApiEventBus;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.subscriber.ApiSubscriberBase;
import org.visab.globalmodel.IMetaInformation;
import org.visab.util.StringFormat;

/**
 * The SessionListenerFactory that will instantiate new SessionListeners
 * whenever a new transmission session is opened at the WebApi.
 *
 * @author moritz
 *
 */
public class SessionListenerFactory extends ApiSubscriberBase<SessionOpenedEvent> {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(SessionListenerFactory.class);

    /**
     * Whether the factory is started
     */
    private boolean isStarted;

    public SessionListenerFactory() {
        super(SessionOpenedEvent.class);
    }

    /**
     * Instantiates a new listener based on the given game and adds it to the
     * SessionListenerAdministration.
     */
    public void addListener(UUID sessionId, IMetaInformation metaInformation) {
        if (metaInformation == null || metaInformation.getGame() == null || metaInformation.getGame().isBlank()) {
            logger.error(StringFormat.niceString(
                    "Received invalid meta information {0}. Wont create listener instance.", metaInformation));
            return;
        }

        var newListener = DynamicInstatiator.instantiateSessionListener(metaInformation.getGame(), sessionId);
        SessionListenerAdministration.addListener(newListener);
        // Notify the listener that the session started
        newListener.onSessionStarted(metaInformation);
    }

    @Override
    public void notify(SessionOpenedEvent event) {
        addListener(event.getSessionId(), event.getMetaInformation());
    }

    /**
     * Starts the Factory by subscribing it to the EventBus
     */
    public void startFactory() {
        if (!isStarted) {
            logger.info("Starting SessionListenerFactory.");
            ApiEventBus.getInstance().subscribe(this);
            isStarted = true;
        }
    }

    /**
     * Stops the factory by unsubscribing it from the eventbus
     */
    public void stopFactory() {
        if (isStarted) {
            logger.info("Stopping SessionListenerFactory.");
            ApiEventBus.getInstance().unsubscribe(this);
            isStarted = false;
        }
    }
}
