package eventbus.event;

import java.util.UUID;

public class SessionOpenedEvent extends EventBase {

    private String game;

    public SessionOpenedEvent(UUID sessionId, String game) {
	super(sessionId);
	this.game = game;
    }

    public String getGame() {
	return game;
    }
}
