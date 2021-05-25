package org.visab.newgui.statistics.cbrshooter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.IStatistics;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.statistics.ILiveViewModel;
import org.visab.newgui.statistics.LiveStatisticsViewModelBase;
import org.visab.newgui.statistics.StatisticsViewModelBase;
import org.visab.newgui.statistics.cbrshooter.model.CBRShooterStatisticsRow;
import org.visab.newgui.statistics.cbrshooter.model.PlayerPlanOccurance;
import org.visab.newgui.statistics.cbrshooter.model.Vector2;
import org.visab.processing.ILiveViewable;
import org.visab.util.StreamUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart.Data;

/**
 * TODO: Create a abstract base class from this example, once vanessa is done
 * with the view
 */
public class CBRShooterStatisticsViewModel extends LiveStatisticsViewModelBase<CBRShooterFile, CBRShooterStatistics> {

    private boolean isActive;

    private boolean isLive;

    private ObservableList<CBRShooterStatisticsRow> overviewStatistics = FXCollections.observableArrayList();

    private List<PlayerPlanOccurance> planOccurances = new ArrayList<>();

    private ObservableList<Data> planUsageCBR = FXCollections.observableArrayList();

    private ObservableList<Data> planUsageScript = FXCollections.observableArrayList();

    public ObservableList<Data> getPlanUsageCBR() {
        return planUsageCBR;
    }

    public ObservableList<Data> getPlanUsageScript() {
        return planUsageScript;
    }

    @Override
    public void notifyStatisticsAdded(CBRShooterStatistics newStatistics) {
        // Updates the pie charts for plan usage
        updatePlanUsage(newStatistics);
        overviewStatistics.add(mapToRow(newStatistics));
    }

    private void updatePlanUsage(CBRShooterStatistics newStatistics) {
        for (var player : newStatistics.getPlayers()) {
            var plan = player.getPlan();

            // Update our plan occurances
            PlayerPlanOccurance occurances = StreamUtil.firstOrNull(planOccurances,
                    x -> x.getPlayerName().equals(player.getName()));

            if (occurances == null) {
                occurances = new PlayerPlanOccurance(player.getName(), player.getIsCBR());
                planOccurances.add(occurances);
            }

            occurances.incrementOccurance(plan);

            // Update our pie charts
            if (player.getIsCBR())
                updatePieChart(planUsageCBR, plan, occurances.getOccurance(plan));
            else
                updatePieChart(planUsageScript, plan, occurances.getOccurance(plan));
        }
    }

    private void updatePieChart(ObservableList<Data> planUsageList, String plan, int occurances) {
        var data = StreamUtil.firstOrNull(planUsageList, x -> x.getName().equals(plan));

        if (data == null)
            planUsageList.add(new Data(plan, 1));
        else
            data.setPieValue(occurances);
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
