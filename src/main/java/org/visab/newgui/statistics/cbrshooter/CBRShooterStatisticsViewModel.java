package org.visab.newgui.statistics.cbrshooter;

import org.visab.generalmodelchangeme.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.ViewModelBase;
import org.visab.newgui.statistics.ILiveViewModel;
import org.visab.newgui.statistics.cbrshooter.model.CBRShooterStatisticsRow;
import org.visab.processing.ILiveViewable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * TODO: Create a abstract base class from this example, once vanessa is done
 * with the view
 */
public class CBRShooterStatisticsViewModel extends ViewModelBase implements ILiveViewModel<CBRShooterStatistics> {

    private boolean isActive;

    private boolean isLive;

    private ObservableList<CBRShooterStatisticsRow> overviewStatistics = FXCollections.observableArrayList();

    public CBRShooterStatisticsViewModel() {
    }

    public boolean supportsLiveViewing() {
        return this instanceof ILiveViewModel;
    }

    public void initiateLiveView(ILiveViewable<CBRShooterStatistics> listener) {
        isLive = true;
        isActive = true;

        listener.addViewModel(this);

        // Add all the already received statistics
        for (var statistics : listener.getReceivedStatistics())
            overviewStatistics.add(mapToRow(statistics));
    }

    @Override
    public void notifyStatisticsAdded(CBRShooterStatistics newStatistics) {
        overviewStatistics.add(mapToRow(newStatistics));
    }

    private CBRShooterStatisticsRow mapToRow(CBRShooterStatistics statistics) {
        return new CBRShooterStatisticsRow(statistics.getScriptPlayer().getPosition());
    }

    public ObservableList<CBRShooterStatisticsRow> getOverviewStatistics() {
        return overviewStatistics;
    }

    @Override
    public void notifySessionClosed() {
        isActive = false;
    }

}
