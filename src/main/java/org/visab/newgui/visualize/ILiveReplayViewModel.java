package org.visab.newgui.visualize;

import org.visab.globalmodel.IImage;
import org.visab.globalmodel.IStatistics;
import org.visab.processing.IReplayLiveViewable;

public interface ILiveReplayViewModel<TStatistics extends IStatistics, TImage extends IImage> extends IReplayViewModel {

    void initialize(IReplayLiveViewable<? extends IStatistics, ? extends IImage> listener);

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
