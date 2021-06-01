package org.visab.newgui.statistics;

import org.visab.globalmodel.IStatistics;
import org.visab.processing.ILiveViewable;

public interface ILiveViewModel<TStatistics extends IStatistics> {

    /**
     * 
     * Initializer for live view. Docks into the given listener and invokes
     * notifyStatisticsAdded for all statistics of the listener.
     * 
     * @param listener The listener to dock onto
     */
    void initialize(ILiveViewable<? extends IStatistics> listener);

    /**
     * Called by the docked listener upon reciving new statistics.
     * 
     * @param newStatistics The newely received statistics.
     */
    void notifyStatisticsAdded(TStatistics newStatistics);

    /**
     * Called by the docked listener upon closing the session.
     */
    void notifySessionClosed();

}
