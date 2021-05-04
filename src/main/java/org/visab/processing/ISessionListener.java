package org.visab.processing;

import java.time.LocalTime;
import java.util.UUID;

/**
 * The ISessionListener interface, that all SessionListeners have to implement.
 *
 * @author moritz
 *
 */
public interface ISessionListener {

    String getGame();

    LocalTime getLastReceived();

    UUID getSessionId();

    boolean isActive();

    void onSessionClosed();

    void onSessionStarted();

}
