package org.visab.processing;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.dynamic.DynamicInstatiator;
import org.visab.eventbus.APISubscriberBase;
import org.visab.eventbus.APIEventBus;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.globalmodel.IMetaInformation;
import org.visab.util.NiceString;

/**
 * The SessionListenerFactory that will instantiate new SessionListeners
 * whenever a new transmission session is opened at the WebApi.
 */
public class SessionListenerFactory extends APISubscriberBase<SessionOpenedEvent> {

    private static Logger logger = LogManager.getLogger(SessionListenerFactory.class);

    private boolean isStarted;

    public SessionListenerFactory() {
        super(SessionOpenedEvent.class);
    }

    /**
     * Instantiates a new listener based on the given game, adds it to the
     * SessionListenerAdministration and finally initializes it with the given meta
     * information.
     * 
     * @param sessionId       The sessionId that the listener will listen to
     * @param metaInformation The meta information using which the session was
     *                        opened
     */
    public void addListener(UUID sessionId, IMetaInformation metaInformation) {
        if (metaInformation == null || metaInformation.getGame() == null || metaInformation.getGame().isBlank()) {
            logger.error(NiceString.make(
                    "Received invalid meta information {0}. Wont create listener instance.", metaInformation));
            return;
        }

        var newListener = DynamicInstatiator.instantiateSessionListener(metaInformation.getGame(), sessionId);
        SessionListenerAdministration.addListener(newListener);
        
        // Notify the listener that the session started
        newListener.initialize(metaInformation);
    }

    @Override
    public void notify(SessionOpenedEvent event) {
        addListener(event.getSessionId(), event.getMetaInformation());
    }

    /**
     * Starts the Factory by subscribing it to the EventBus.
     */
    public void startFactory() {
        if (!isStarted) {
            logger.info("Starting SessionListenerFactory.");
            APIEventBus.getInstance().subscribe(this);
            isStarted = true;
        }
    }

    /**
     * Stops the factory by unsubscribing it from the eventbus.
     */
    public void stopFactory() {
        if (isStarted) {
            logger.info("Stopping SessionListenerFactory.");
            APIEventBus.getInstance().unsubscribe(this);
            isStarted = false;
        }
    }
}
