package org.visab.processing;

import java.time.LocalTime;
import java.util.UUID;

import org.visab.globalmodel.IMetaInformation;
import org.visab.globalmodel.IStatistics;

/**
 * The ISessionListener interface, that all SessionListeners have to implement.
 * 
 * @param <TStatistics> The type of the statistics that will be processed by the
 *                      listener
 * @author moritz
 *
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
     * The session listeners corresponding sessionId.
     * 
     * @return
     */
    UUID getSessionId();

    /**
     * Whether the ISessionListener is still actively listening to Events.
     * 
     * @return True if still listening
     */
    boolean isActive();

    void initialize(IMetaInformation metaInformation);

}
