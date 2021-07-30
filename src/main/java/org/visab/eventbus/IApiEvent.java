package org.visab.eventbus;

import java.util.UUID;

import org.visab.globalmodel.SessionStatus;

/**
 * The IApiEvent interface that all api events have to implement.
 *
 * @author moritz
 *
 */
public interface IAPIEvent extends IEvent {

    /**
     * Gets the transmission sessions sessionId by which the event was caused.
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
