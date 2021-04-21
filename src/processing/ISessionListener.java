package processing;

import java.util.UUID;

public interface ISessionListener {

    String getGame();

    UUID getSessionId();

    void onSessionClosed();

    void onSessionStarted();

}
