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

    public SessionClosedEvent(UUID sessionId) {
	super(sessionId);
    }
}
