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
public interface ISessionListener<TStatistics extends IStatistics> {

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

    /**
     * Called when the listeners corresponding session is closed.
     */
    void onSessionClosed();

    /**
     * Called when the listeners is started.
     * 
     * @param metaInformation The meta information object using which the session
     *                        was opened.
     */
    void onSessionStarted(IMetaInformation metaInformation);

    /**
     * Called upon reciving statistics for the current session. Is only called if
     * the received Statistics object was not null.
     * 
     * @param statistics The received TStatistics object
     */
    void processStatistics(TStatistics statistics);

}
