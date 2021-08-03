package org.visab.eventbus;

import java.util.UUID;

import org.visab.globalmodel.SessionStatus;

/**
 * The IAPIEvent interface that all API related events have to implement.
 */
public interface IAPIEvent extends IEvent {

    /**
     * Gets the transmission sessions id who the event was caused by.
     * 
     * @return The transmission sessions sessionId
     */
    UUID getSessionId();

    /**
     * Gets the status of the transmission session associated with the event.
     * 
     * @return The status of the transmission session
     */
    SessionStatus getStatus();
}
