package eventbus;

import java.util.UUID;

public interface IEvent {

    UUID getSessionId();
}
