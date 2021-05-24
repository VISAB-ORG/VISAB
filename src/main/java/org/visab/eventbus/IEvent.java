package org.visab.eventbus;

import java.util.UUID;

/**
 * The IEvent interface that all events have to implement.
 *
 * @author moritz
 *
 */
public interface IEvent {

    /**
     * Gets the transmission sessions sessionId by which the event was caused.
     * 
     * @return The transmission sessions sessionId
     */
    UUID getSessionId();
}
