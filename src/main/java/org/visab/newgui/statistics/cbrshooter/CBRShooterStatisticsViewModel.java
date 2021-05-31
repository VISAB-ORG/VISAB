package org.visab.newgui.statistics.cbrshooter;

import java.util.ArrayList;
import java.util.List;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.statistics.LiveStatisticsViewModelBase;
import org.visab.newgui.statistics.cbrshooter.model.CBRShooterStatisticsRow;
import org.visab.newgui.statistics.cbrshooter.model.PlayerPlanTime;
import org.visab.newgui.statistics.cbrshooter.model.Vector2;
import org.visab.util.StreamUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart.Data;

public class CBRShooterStatisticsViewModel extends LiveStatisticsViewModelBase<CBRShooterFile, CBRShooterStatistics> {

    private ObservableList<CBRShooterStatisticsRow> overviewStatistics = FXCollections.observableArrayList();

    private List<PlayerPlanTime> planTimes = new ArrayList<>();

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
            // If plan is "" substitute with No plan
            var plan = player.getPlan().equals("") ? "No plan" : player.getPlan();

            // If player plan time doesnt for player exist yet, add it.
            var playerPlanTime = StreamUtil.firstOrNull(planTimes, x -> x.getPlayerName().equals(player.getName()));
            if (playerPlanTime == null) {
                playerPlanTime = new PlayerPlanTime(player.getName(), player.getIsCBR());
                planTimes.add(playerPlanTime);
            }

            // Increment the plan time
            playerPlanTime.incrementOccurance(plan, newStatistics.getRound(), newStatistics.getRoundTime());

            // Check if plan is new
            var isNewPlan = false;
            if (player.getIsCBR())
                isNewPlan = !StreamUtil.contains(planUsageCBR, x -> x.getName().equals(plan));
            else
                isNewPlan = !StreamUtil.contains(planUsageScript, x -> x.getName().equals(plan));

            // If plan is newly added, add new data with bound value to observable list
            if (isNewPlan) {
                // Create new data
                var data = new Data(plan, playerPlanTime.getTotalTime(plan));
                data.pieValueProperty().bind(playerPlanTime.getTimeProperty(plan));

                // Add to observable list
                if (player.getIsCBR())
                    planUsageCBR.add(data);
                else
                    planUsageScript.add(data);
            }
        }
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
        liveSessionActiveProperty.set(false);
        // TODO: Render some future "who won" graphs an such
    }

    @Override
    public void afterInitialize(CBRShooterFile file) {
        for (var statistics : file.getStatistics()) {
            notifyStatisticsAdded(statistics);
        }
    }

}
