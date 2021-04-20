package eventbus;

import java.util.UUID;

public class SessionClosedEvent implements IEvent {

	public UUID getSessionId() {
		return sessionId;
	}

	private UUID sessionId;

	public SessionClosedEvent(UUID sessionId) {
		this.sessionId = sessionId;
	}
}
