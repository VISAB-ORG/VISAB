package org.visab.newgui.visualize;

import org.visab.globalmodel.IImageContainer;
import org.visab.globalmodel.IStatistics;
import org.visab.processing.IReplayLiveViewable;

public interface ILiveReplayViewModel<TStatistics extends IStatistics, TImage extends IImageContainer> extends IVisualizeViewModel {

    void initialize(IReplayLiveViewable<? extends IStatistics, ? extends IImageContainer> listener);

    /**
     * Called by the docked listener upon reciving new statistics.
     * 
     * @param newStatistics The newely received statistics.
     */
    void notifyStatisticsAdded(TStatistics newStatistics);

    void notifyImageAdded(TImage image);

    /**
     * Called by the docked listener upon closing the session.
     */
    void notifySessionClosed();

}
