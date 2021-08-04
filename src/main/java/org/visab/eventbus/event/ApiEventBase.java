package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.eventbus.IApiEvent;
import org.visab.globalmodel.SessionStatus;

/**
 * A base api event class, that all api events should inherit from.
 *
 * @author moritz
 *
 */
public abstract class ApiEventBase implements IApiEvent {

    private UUID sessionId;
    private SessionStatus sessionStatus;

    public ApiEventBase(UUID sessionId, SessionStatus sessionStatus) {
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