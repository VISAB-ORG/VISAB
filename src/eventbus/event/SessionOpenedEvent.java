package eventbus.event;

import java.util.UUID;

/**
 * The SessionOpenedEvent that occurs when a new VISAB transmission session is
 * started.
 *
 * @author moritz
 *
 */
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
