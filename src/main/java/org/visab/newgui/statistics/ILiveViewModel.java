package org.visab.newgui.statistics;

import org.visab.processing.IStatistics;

public interface ILiveViewModel<TStatistics extends IStatistics> {

    void notifyStatisticsAdded(TStatistics newStatistics);

    void notifySessionClosed();

}
