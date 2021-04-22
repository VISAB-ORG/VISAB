package eventbus.event;

import java.util.UUID;

import eventbus.IEvent;

/**
 * A base event class, that all events should inherit from.
 *
 * @author moritz
 *
 */
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
