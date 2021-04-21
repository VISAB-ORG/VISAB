package eventbus.event;

import java.util.UUID;

import eventbus.IEvent;

public abstract class EventBase implements IEvent {

    private UUID sessionId;

    public EventBase(UUID sessionId) {
	this.sessionId = sessionId;
    }

    @Override
    public UUID getSessionId() {
	return sessionId;
    }
}
