package org.visab.eventbus.event;

import java.util.UUID;

/**
 * The SessionClosedEvent, that occurs when a VISAB transmission session is
 * closed.
 *
 * @author moritz
 *
 */
public class SessionClosedEvent extends EventBase {

    private boolean closedByTimeout;

    public boolean isClosedByTimeout() {
        return closedByTimeout;
    }

    public SessionClosedEvent(UUID sessionId, boolean closedByTimeout) {
        super(sessionId);
        this.closedByTimeout = closedByTimeout;
    }
}
