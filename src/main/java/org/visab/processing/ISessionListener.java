package org.visab.processing;

import java.time.LocalTime;
import java.util.UUID;

import org.visab.globalmodel.IMetaInformation;

/**
 * The ISessionListener interface, that all SessionListeners have to implement.
 * SessionListeners are responsible for processing the information that was sent
 * to VISABs WebApi by the game. Every open transmission session has exactly one
 * SessionListener instance.
 * 
 * Instead of implementing this yourself, consider inheriting from
 * SessionListenerBase or ReplayListenerBase instead.
 * 
 * @param <TStatistics> The type of the statistics that will be processed by the
 *                      listener
 */
public interface ISessionListener {

    /**
     * Returns the game of the listener.
     * 
     * @return The game of the listener
     */
    String getGame();

    /**
     * The last time statistics were received by the listener.
     * 
     * @return
     */
    LocalTime getLastReceived();

    /**
     * The session listeners corresponding tranmission session sessionId.
     * 
     * @return The sessionId
     */
    UUID getSessionId();

    /**
     * Whether the ISessionListener is still actively listening to Events.
     * 
     * @return True if still listening
     */
    boolean isActive();

    /**
     * Initializes the SessionListener with the meta information for the session.
     * 
     * @param metaInformation The meta information that was sent
     */
    void initialize(IMetaInformation metaInformation);

}
