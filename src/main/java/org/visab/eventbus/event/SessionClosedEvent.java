package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.newgui.webapi.model.TransmissionSessionStatus;

/**
 * The SessionClosedEvent, that occurs when a VISAB transmission session is
 * closed.
 *
 * @author moritz
 *
 */
public class SessionClosedEvent extends ApiEventBase {

    private boolean closedByTimeout;

    public boolean isClosedByTimeout() {
        return closedByTimeout;
    }

    public SessionClosedEvent(UUID sessionId, TransmissionSessionStatus status, boolean closedByTimeout) {
        super(sessionId, status);
        this.closedByTimeout = closedByTimeout;
    }
}
