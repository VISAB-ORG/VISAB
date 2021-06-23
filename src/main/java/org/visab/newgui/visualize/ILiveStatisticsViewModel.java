package org.visab.newgui.visualize;

import org.visab.globalmodel.IStatistics;
import org.visab.processing.ILiveViewable;

public interface ILiveStatisticsViewModel<TStatistics extends IStatistics> extends IStatisticsViewModel {

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
