package org.visab.newgui.visualize.cbrshooter.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.UiHelper;
import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.LiveStatisticsViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterComparisonRowBase;
import org.visab.newgui.visualize.cbrshooter.model.Collectable;
import org.visab.newgui.visualize.cbrshooter.model.PlayerPlanTime;
import org.visab.newgui.visualize.cbrshooter.model.comparison.*;
import org.visab.processing.ILiveViewable;
import org.visab.util.StreamUtil;

import de.saxsys.mvvmfx.InjectScope;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart.Series;

// TODO: Add end of game thingy.
public class CBRShooterStatisticsViewModel extends LiveStatisticsViewModelBase<CBRShooterFile, CBRShooterStatistics> {

    private List<PlayerPlanTime> planTimes = new ArrayList<>();

    @InjectScope
    VisualizeScope scope;

    private List<String> playerNames = new ArrayList<>();
    private Map<String, ObservableList<Data>> planUsages = new HashMap<>();

    private ObservableList<ComparisonRowBase<?>> comparisonStatistics = FXCollections.observableArrayList();
    private FloatProperty snapshotsPerIngamesSecond = new SimpleFloatProperty();

    /**
     * Called after the instance was constructed by javafx/mvvmfx.
     */
    public void initialize() {
        if (scope.isLive()) {
            super.initializeLive(scope.getSessionListener());

            // Initialize the data structures used for visualization
            initializeDataStructures(file);

            // Notify for all the already received statistics
            for (var statistics : listener.getReceivedStatistics())
                notifyStatisticsAdded(statistics);
        } else {
            super.initialize(scope.getFile());

            // Initialize the data structures used for visualization
            initializeDataStructures(file);

            for (var statistics : file.getStatistics()) {
                notifyStatisticsAdded(statistics);
            }
        }
    }

    private void initializeDataStructures(CBRShooterFile file) {
        killsScript.setName("Kills Script Bot");
        killsCBR.setName("Kills CBR Bot");

        playerKillsSeries.add(killsCBR);
        playerKillsSeries.add(killsScript);

        // Add the player names
        playerNames.addAll(file.getPlayerInformation().keySet());

        for (var name : playerNames) {
            // Initialize plan visualization
            var planTime = new PlayerPlanTime(name);
            planTimes.add(planTime);
            planUsages.put(name, FXCollections.observableArrayList());

        }

        // Add the comparison rows.
        comparisonStatistics.add(new PlayerTypeComparisonRow());
        comparisonStatistics.add(new KillsComparisonRow());
        comparisonStatistics.add(new DeathsComparisonRow());
        comparisonStatistics.add(new ShotsComparisonRow());
        comparisonStatistics.add(new HitsComparisonRow());
        comparisonStatistics.add(new AimRatioComparisonRow());
        comparisonStatistics.add(new MovementComparisonRow());
        comparisonStatistics.add(new CollectedComparisonRow(Collectable.Health));
        comparisonStatistics.add(new CollectedComparisonRow(Collectable.Ammunition));
        comparisonStatistics.add(new CollectedComparisonRow(Collectable.Weapon));

        for (var row : comparisonStatistics) {
            row.updateValues(file);
        }
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public ObservableList<ComparisonRowBase<?>> getComparisonStatistics() {
        return comparisonStatistics;
    }

    public ObservableList<Data> getPlanUsage(String playerName) {
        return planUsages.get(playerName);
    }

    private Series<Double, Integer> killsScript = new Series<>();
    private Series<Double, Integer> killsCBR = new Series<>();

    private ObservableList<Series<Double, Integer>> playerKillsSeries = FXCollections.observableArrayList();

    public ObservableList<Series<Double, Integer>> getPlayerKillsSeries() {
        return playerKillsSeries;
    }

    @Override
    public void notifyStatisticsAdded(CBRShooterStatistics newStatistics) {
        snapshotsPerIngamesSecond.set(comparisonStatistics.size() / newStatistics.getTotalTime());

        updatePlanUsage(newStatistics);
        updatePlayerKills(newStatistics);
        updateComparisonStatistics();
    }

    private void updatePlanUsage(CBRShooterStatistics newStatistics) {
        for (var player : newStatistics.getPlayers()) {
            // If plan is "" substitute with 'No plan'
            var plan = player.getPlan().equals("") ? "No plan" : player.getPlan();

            var playerPlanTime = StreamUtil.firstOrNull(planTimes, x -> x.getPlayerName().equals(player.getName()));

            // Increment the plan time. This change is reflected in the view, since
            // PlayerPlanTimes uses a DoubleProperty for the plan times.
            playerPlanTime.incrementOccurance(plan, newStatistics.getRound(), newStatistics.getRoundTime());

            var dataList = planUsages.get(player.getName());
            var isNewPlan = !StreamUtil.contains(dataList, x -> x.getName().equals(plan));

            // If plan is newly added, add new data with bound value to observable list
            if (isNewPlan) {
                var data = new Data(plan, playerPlanTime.getTotalTime(plan));
                data.pieValueProperty().bind(playerPlanTime.getTimeProperty(plan));

                dataList.add(data);
            }
        }
    }

    private Map<String, Integer> lastKills = new HashMap<>();

    private void updatePlayerKills(CBRShooterStatistics newStatistics) {
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

    private void updateComparisonStatistics() {
        if (isLiveViewProperty.get()) {
            for (var row : comparisonStatistics)
                row.updateValues(file);
        }
    }

    @Override
    public void notifySessionClosed() {
        liveSessionActiveProperty.set(false);
        // TODO: Render some future "who won" graphs an such
    }

    public FloatProperty snapshotPerIngameSecondProperty() {
        return snapshotsPerIngamesSecond;
    }

}
