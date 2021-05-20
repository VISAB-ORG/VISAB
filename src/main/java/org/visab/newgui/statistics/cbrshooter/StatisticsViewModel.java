package org.visab.newgui.statistics.cbrshooter;

import org.visab.newgui.ViewModelBase;
import org.visab.newgui.statistics.ILiveViewModel;
import org.visab.newgui.statistics.cbrshooter.model.StatisticsRow;
import org.visab.processing.ILiveViewable;
import org.visab.processing.cbrshooter.model.CBRShooterStatistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * TODO: Do we name this Class CBRShooterStatisticsViewModel? Current name is
 * technically sufficient, due to the packaging
 * (org.visab.newgui.statistics.cbrshooter.StatisticsViewModel !=
 * org.visab.newgui.statistics.someothegame.StatisticsViewModel)
 */
public class StatisticsViewModel extends ViewModelBase implements ILiveViewModel<CBRShooterStatistics> {

    private boolean isActive;

    private boolean isLive;

    private ObservableList<StatisticsRow> overviewStatistics = FXCollections.observableArrayList();

    public StatisticsViewModel() {
    }

    public StatisticsViewModel(ILiveViewable<CBRShooterStatistics> listener) {
        listener.addViewModel(this);
        isLive = true;
        isActive = true;

        // Add all the already received statistics
        for (var statistics : listener.getReceivedStatistics())
            overviewStatistics.add(mapToRow(statistics));
    }

    public ObservablePropety<Boolean> getIsActive() {

    }

    @Override
    public void notifyStatisticsAdded(CBRShooterStatistics newStatistics) {
        overviewStatistics.add(mapToRow(newStatistics));
    }

    private StatisticsRow mapToRow(CBRShooterStatistics statistics) {
        return new StatisticsRow(statistics.getScriptPlayer().getPosition());
    }

    public ObservableList<StatisticsRow> getOverviewStatistics() {
        return overviewStatistics;
    }

    @Override
    public void notifySessionClosed() {
        isActive = false;
    }

}
