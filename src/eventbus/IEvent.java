package eventbus;

import java.util.UUID;

/**
 * The IEvent interface that all events have to implement.
 *
 * @author moritz
 *
 */
public interface IEvent {

    UUID getSessionId();
}
