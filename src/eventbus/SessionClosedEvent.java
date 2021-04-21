package eventbus;

import java.util.UUID;

public class SessionClosedEvent extends EventBase {

    public SessionClosedEvent(UUID sessionId) {
	super(sessionId);
    }
}
