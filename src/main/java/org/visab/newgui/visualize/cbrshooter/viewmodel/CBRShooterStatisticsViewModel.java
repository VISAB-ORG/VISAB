package org.visab.newgui.visualize.cbrshooter.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.visualize.LiveStatisticsViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;
import org.visab.newgui.visualize.cbrshooter.model.ComparisonRowBase;
import org.visab.newgui.visualize.cbrshooter.model.MovementComparisonRow;
import org.visab.newgui.visualize.cbrshooter.model.PlayerPlanTime;
import org.visab.processing.ILiveViewable;
import org.visab.util.StreamUtil;

import de.saxsys.mvvmfx.InjectScope;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart.Series;

// TODO: Add end of game thingy.
public class CBRShooterStatisticsViewModel extends LiveStatisticsViewModelBase<CBRShooterFile, CBRShooterStatistics> {

    /**
     * Called after the instance was constructed by javafx/mvvmfx.
     */
    public void initialize() {
        killsScript.setName("Kills Script Bot");
        killsCBR.setName("Kills CBR Bot");

        playerKillsSeries.add(killsCBR);
        playerKillsSeries.add(killsScript);

        if (scope.isLive()) {
            super.initializeLive(scope.getSessionListener());

            // Add the player names
            playerNames.addAll(file.getPlayerInformation().keySet());

            // TODO: Temporary
            var row = new MovementComparisonRow();
            row.updateValues(file);
            comparisonStatistics.add(row);

            // Notify for all the already received statistics
            for (var statistics : listener.getReceivedStatistics())
                notifyStatisticsAdded(statistics);
        } else {
            super.initialize(scope.getFile());

            for (var statistics : file.getStatistics()) {
                notifyStatisticsAdded(statistics);
            }
        }
    }

    @InjectScope
    VisualizeScope scope;

    private ObservableList<String> playerNames = FXCollections.observableArrayList();

    public ObservableList<String> getPlayerNames() {
        return playerNames;
    }

    private ObservableList<ComparisonRowBase<?>> comparisonStatistics = FXCollections.observableArrayList();

    public ObservableList<ComparisonRowBase<?>> getComparisonStatistics() {
        return comparisonStatistics;
    }

    private List<PlayerPlanTime> planTimes = new ArrayList<>();

    private Map<String, ObservableList<Data>> planUsages = new HashMap<>();

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

    private ObservableList<Series<Double, Integer>> playerKillsSeries = FXCollections.observableArrayList();

    public ObservableList<Series<Double, Integer>> getPlayerKillsSeries() {
        return playerKillsSeries;
    }

    @Override
    public void notifyStatisticsAdded(CBRShooterStatistics newStatistics) {
        // Updates the pie charts for plan usage
        updatePlanUsage(newStatistics);
        updatePlayerStatistics(newStatistics);
        snapshotsPerSecond.set(comparisonStatistics.size() / newStatistics.getTotalTime());

    }

    private void updatePlanUsage(CBRShooterStatistics newStatistics) {
        for (var player : newStatistics.getPlayers()) {
            // If plan is "" substitute with No plan
            var plan = player.getPlan().equals("") ? "No plan" : player.getPlan();

            var isCbr = file.getPlayerInformation().get(player.getName()).equals("cbr");

            // If player plan time doesnt for player exist yet, add it.
            var playerPlanTime = StreamUtil.firstOrNull(planTimes, x -> x.getPlayerName().equals(player.getName()));
            if (playerPlanTime == null) {
                playerPlanTime = new PlayerPlanTime(player.getName(), isCbr);
                planTimes.add(playerPlanTime);
            }

            // Increment the plan time
            playerPlanTime.incrementOccurance(plan, newStatistics.getRound(), newStatistics.getRoundTime());

            // Check if plan is new
            var isNewPlan = false;
            if (isCbr)
                isNewPlan = !StreamUtil.contains(planUsageCBR, x -> x.getName().equals(plan));
            else
                isNewPlan = !StreamUtil.contains(planUsageScript, x -> x.getName().equals(plan));

            // If plan is newly added, add new data with bound value to observable list
            if (isNewPlan) {
                // Create new data
                var data = new Data(plan, playerPlanTime.getTotalTime(plan));
                data.pieValueProperty().bind(playerPlanTime.getTimeProperty(plan));

                // Add to observable list
                if (isCbr)
                    planUsageCBR.add(data);
                else
                    planUsageScript.add(data);
            }
        }
    }

    private Map<String, Integer> lastKills = new HashMap<>();

    private void updatePlayerStatistics(CBRShooterStatistics newStatistics) {
        for (var player : newStatistics.getPlayers()) {
            var isCbr = file.getPlayerInformation().get(player.getName()).equals("cbr");
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

                if (isCbr)
                    killsCBR.getData().add(newData);
                else
                    killsScript.getData().add(newData);
            }
        }
    }

    @Override
    public void notifySessionClosed() {
        liveSessionActiveProperty.set(false);
        // TODO: Render some future "who won" graphs an such
    }

    private FloatProperty snapshotsPerSecond = new SimpleFloatProperty();

    public FloatProperty snapshotPerSecondProperty() {
        return snapshotsPerSecond;
    }

}
