package org.visab.processing;

import java.time.LocalTime;
import java.util.UUID;

import org.visab.globalmodel.IStatistics;

/**
 * The ISessionListener interface, that all SessionListeners have to implement.
 *
 * @author moritz
 *
 */
public interface ISessionListener<TStatistics extends IStatistics> {

    String getGame();

    LocalTime getLastReceived();

    UUID getSessionId();

    boolean isActive();

    void onSessionClosed();

    void onSessionStarted();

    void processStatistics(TStatistics statistics);

}
