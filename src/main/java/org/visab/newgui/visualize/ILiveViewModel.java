package org.visab.newgui.visualize;

import org.visab.globalmodel.IStatistics;
import org.visab.processing.ILiveViewable;

public interface ILiveViewModel<TStatistics extends IStatistics> extends IVisualizeViewModel {

    void initializeLive(ILiveViewable<? extends IStatistics> listener);

    /**
     * Called by the docked listener upon reciving new statistics.
     * 
     * @param newStatistics The newely received statistics.
     */
    void onStatisticsAdded(TStatistics newStatistics);

    /**
     * Called by the docked listener upon closing the session.
     */
    void onSessionClosed();

}
