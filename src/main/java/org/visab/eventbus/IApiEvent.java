package org.visab.eventbus;

import java.util.UUID;

import org.visab.globalmodel.TransmissionSessionStatus;

/**
 * The IApiEvent interface that all api events have to implement.
 *
 * @author moritz
 *
 */
public interface IApiEvent extends IEvent {

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
    TransmissionSessionStatus getStatus();
}
