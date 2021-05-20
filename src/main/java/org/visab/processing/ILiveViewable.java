package org.visab.processing;

import java.util.List;

import org.visab.generalmodelchangeme.IStatistics;
import org.visab.newgui.statistics.ILiveViewModel;

public interface ILiveViewable<TStatistics extends IStatistics> {

    void addViewModel(ILiveViewModel<TStatistics> viewModel);

    List<TStatistics> getReceivedStatistics();

    void notifyStatisticsAdded(TStatistics addedStatistics);

    void notifySessionClosed();
}