package org.visab.newgui.visualize.cbrshooter.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.visualize.LiveStatisticsViewModelBase;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterStatisticsRow;
import org.visab.newgui.visualize.cbrshooter.model.PlayerPlanTime;
import org.visab.newgui.visualize.cbrshooter.model.Vector2;
import org.visab.util.StreamUtil;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart.Series;

// TODO: Add end of game thingy.
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

    private Series<Double, Integer> killsScript = new Series<>();
    private Series<Double, Integer> killsCBR = new Series<>();

    public CBRShooterStatisticsViewModel() {
        killsScript.setName("Kills Script Bot");
        killsCBR.setName("Kills CBR Bot");

        playerKillsSeries.add(killsCBR);
        playerKillsSeries.add(killsScript);
    }

    private ObservableList<Series<Double, Integer>> playerKillsSeries = FXCollections.observableArrayList();

    public ObservableList<Series<Double, Integer>> getPlayerKillsSeries() {
        return playerKillsSeries;
    }

    @Override
    public void notifyStatisticsAdded(CBRShooterStatistics newStatistics) {
        // Updates the pie charts for plan usage
        updatePlanUsage(newStatistics);
        updatePlayerStatistics(newStatistics);
        snapshotsPerSecond.set(overviewStatistics.size() / newStatistics.getTotalTime());
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

    private Map<String, Integer> lastKills = new HashMap<>();

    private void updatePlayerStatistics(CBRShooterStatistics newStatistics) {
        for (var player : newStatistics.getPlayers()) {
            var name = player.getName();
            if (!lastKills.containsKey(name)) {
                lastKills.put(name, 0);
            }

            var kills = player.getStatistics().getFrags();
            if (lastKills.get(name) != kills) {
                lastKills.put(name, kills);

                var newData = new javafx.scene.chart.LineChart.Data<Double, Integer>();
                newData.setYValue(kills);
                newData.setXValue(Double.valueOf(newStatistics.getTotalTime()));

                if (player.getIsCBR())
                    killsCBR.getData().add(newData);
                else
                    killsScript.getData().add(newData);
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

    private FloatProperty snapshotsPerSecond = new SimpleFloatProperty();

    public FloatProperty snapshotPerSecondProperty() {
        return snapshotsPerSecond;
    }

}
