package org.visab.newgui.statistics;

import org.visab.generalmodelchangeme.IStatistics;

public interface ILiveViewModel<TStatistics extends IStatistics> {

    void notifyStatisticsAdded(TStatistics newStatistics);

    void notifySessionClosed();

}
