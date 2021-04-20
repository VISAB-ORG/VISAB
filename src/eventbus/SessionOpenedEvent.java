package eventbus;

import java.util.UUID;

public class SessionOpenedEvent implements IEvent {

	public UUID getSessionId() {
		return sessionId;
	}

	public String getGame() {
		return game;
	}

	private UUID sessionId;
	
	private String game;

	public SessionOpenedEvent(UUID sessionId, String game) {
		this.sessionId = sessionId;
		this.game = game;
	}
}
