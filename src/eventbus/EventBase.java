package eventbus;

import java.util.UUID;

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
