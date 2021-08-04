package org.visab.eventbus;

import java.util.UUID;

import org.visab.globalmodel.SessionStatus;

/**
 * A base API event class, that all API events should inherit from.
 */
public abstract class APIEventBase implements IAPIEvent {

    private UUID sessionId;
    private SessionStatus sessionStatus;

    public APIEventBase(UUID sessionId, SessionStatus sessionStatus) {
        this.sessionId = sessionId;
        this.sessionStatus = sessionStatus;
    }

    @Override
    public UUID getSessionId() {
        return sessionId;
    }

    @Override
    public SessionStatus getStatus() {
        return sessionStatus;
    }
}
