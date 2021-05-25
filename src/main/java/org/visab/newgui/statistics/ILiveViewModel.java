package org.visab.newgui.statistics;

import org.visab.globalmodel.IStatistics;
import org.visab.processing.ILiveViewable;

public interface ILiveViewModel<TStatistics extends IStatistics> {

    void initialize(ILiveViewable<? extends IStatistics> listener);

    void notifyStatisticsAdded(TStatistics newStatistics);

    void notifySessionClosed();

}
