package org.visab.newgui.statistics.cbrshooter;

import java.util.HashMap;
import java.util.Map;

import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.ViewModelBase;
import org.visab.newgui.statistics.ILiveViewModel;
import org.visab.newgui.statistics.cbrshooter.model.CBRShooterStatisticsRow;
import org.visab.newgui.statistics.cbrshooter.model.Vector2;
import org.visab.processing.ILiveViewable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart.Data;

/**
 * TODO: Create a abstract base class from this example, once vanessa is done
 * with the view
 */
public class CBRShooterStatisticsViewModel extends ViewModelBase implements ILiveViewModel<CBRShooterStatistics> {

    private boolean isActive;

    private boolean isLive;

    private ObservableList<CBRShooterStatisticsRow> overviewStatistics = FXCollections.observableArrayList();

    private PlanOccurance planOccurance = new PlanOccurance();

    private ObservableList<Data> planUsageCBR = FXCollections.observableArrayList();

    public CBRShooterStatisticsViewModel() {
    }

    public ObservableList<Data> getPlanUsageCBR() {
        return planUsageCBR;
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
        System.out.println("HI");
        updatePlanUsage(newStatistics);
        overviewStatistics.add(mapToRow(newStatistics));
    }

    // TODO: This could posssible take the current round timer into consideration
    // also.
    private void updatePlanUsage(CBRShooterStatistics newStatistics) {
        var cbrPlayer = newStatistics.getCBRPlayer();
        var cbrPlan = cbrPlayer.getPlan();

        var occuranceCbr = planOccurance.getCBR();
        if (!occuranceCbr.containsKey(cbrPlan))
            occuranceCbr.put(cbrPlan, 1);
        else
            occuranceCbr.put(cbrPlan, occuranceCbr.get(cbrPlan) + 1);

        // Update chart data
        if (!planUsageCBR.stream().filter(x -> x.getName().equals(cbrPlan)).findAny().isPresent()) {
            planUsageCBR.add(new Data(cbrPlan, 1));
        } else {
            for (var data : planUsageCBR) {
                if (data.getName().equals(cbrPlan))
                    data.setPieValue(occuranceCbr.get(cbrPlan) + 1);
            }
        }

        // TODO: For script guy

    }

    private CBRShooterStatisticsRow mapToRow(CBRShooterStatistics statistics) {
        var position = statistics.getPlayers().get(0).getPosition();
        return new CBRShooterStatisticsRow(new Vector2(position.getX(), position.getY()));
    }

    public ObservableList<CBRShooterStatisticsRow> getOverviewStatistics() {
        return overviewStatistics;
    }

    @Override
    public void notifySessionClosed() {
        isActive = false;
        // TODO: Render some future "who won" graphs an such
    }

}
