package processing;

import java.util.UUID;

/**
 * The ISessionListener interface, that all SessionListeners have to implement.
 *
 * @author moritz
 *
 */
public interface ISessionListener {

    String getGame();

    UUID getSessionId();

    void onSessionClosed();

    void onSessionStarted();

}
