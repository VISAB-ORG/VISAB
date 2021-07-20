package org.visab.newgui.visualize.cbrshooter.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.LiveViewModelBase;
import org.visab.newgui.visualize.cbrshooter.model.Collectable;
import org.visab.newgui.visualize.cbrshooter.model.PlayerPlanTime;
import org.visab.newgui.visualize.cbrshooter.model.comparison.*;
import org.visab.util.StreamUtil;

import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart.Series;

// TODO: Add end of game thingy.
public class CBRShooterStatisticsViewModel extends LiveViewModelBase<CBRShooterFile, CBRShooterStatistics> {

    private List<PlayerPlanTime> planTimes = new ArrayList<>();

    private Map<String, ObservableList<Data>> planUsages = new HashMap<>();

    private ObservableList<ComparisonRowBase<?>> comparisonStatistics = FXCollections.observableArrayList();

    private ObjectProperty<ComparisonRowBase<?>> selectedStatistics = new SimpleObjectProperty<>();

    private ObservableList<Series<Integer, Number>> playerStatsSeries = FXCollections.observableArrayList();

    // Set in command on show stats button click
    private ComparisonRowBase<?> graphComparisonRow;
    private StringProperty yLabel = new SimpleStringProperty();

    private Command playerStatsChartCommand;

    /**
     * Called after the instance was constructed by javafx/mvvmfx.
     */
    public void initialize() {
        if (scope.isLive()) {
            super.initializeLive(scope.getSessionListener());
            // Register ourselves, for when the view closes
            scope.registerForStageClosing(this);
            // Initialize the data structures used for visualization
            initializeDataStructures(file);
        } else {
            super.initialize(scope.getFile());

            // Initialize the data structures used for visualization
            initializeDataStructures(file);
        }
    }

    public ObservableList<Series<Integer, Number>> getPlayerStatsSeries() {
        return playerStatsSeries;
    }

    public ObjectProperty<ComparisonRowBase<?>> selectedStatisticsProperty() {
        return selectedStatistics;
    }

    public StringProperty yLabelProperty() {
        return yLabel;
    }

    public Command playerStatsChartCommand() {
        if (playerStatsChartCommand == null) {
            playerStatsChartCommand = runnableCommand(() -> {
                var selectedRow = selectedStatistics.get();
                if (selectedRow != null) {

                    selectedRow.updateSeries(file);
                    playerStatsSeries.clear();
                    playerStatsSeries.addAll(selectedRow.getPlayerSeries().values());

                    // check if Row needs accumulated prefix
                    if (selectedRow.getRowDescription().equals("Kills")
                            || selectedRow.getRowDescription().equals("Deaths")
                            || selectedRow.getRowDescription().equals("Health items collected")
                            || selectedRow.getRowDescription().equals("Ammunition items collected")
                            || selectedRow.getRowDescription().equals("Weapon items collected")) {
                        yLabel.set("Accumulated " + selectedRow.getRowDescription());
                    } else {
                        yLabel.set(selectedRow.getRowDescription());
                    }

                    graphComparisonRow = selectedRow;
                }
            });
        }
        return playerStatsChartCommand;
    }

    private void initializeDataStructures(CBRShooterFile file) {
        for (var name : file.getPlayerNames()) {
            // Initialize plan visualization
            var planTime = new PlayerPlanTime(name);
            planTimes.add(planTime);
            planUsages.put(name, FXCollections.observableArrayList());

        }

        // Add the comparison rows.
        // comparisonStatistics.add(new PlayerTypeComparisonRow()); TODO: set PlayerType
        // in column header description
        comparisonStatistics.add(new KillsComparisonRow());
        comparisonStatistics.add(new DeathsComparisonRow());
        comparisonStatistics.add(new ShotsComparisonRow());
        comparisonStatistics.add(new HitsComparisonRow());
        comparisonStatistics.add(new AimRatioComparisonRow());
        comparisonStatistics.add(new MovementComparisonRow());
        comparisonStatistics.add(new CollectedComparisonRow(Collectable.Health));
        comparisonStatistics.add(new CollectedComparisonRow(Collectable.Ammunition));
        comparisonStatistics.add(new CollectedComparisonRow(Collectable.Weapon));

        // Set to the current values
        for (var row : comparisonStatistics) {
            row.updateValues(file);
            row.updateSeries(file);
        }

        var statisticsCopy = new ArrayList<>(file.getStatistics());
        for (var statistics : statisticsCopy)
            updatePlanUsage(statistics);

    }

    public List<String> getPlayerNames() {
        return file.getPlayerNames();
    }

    public Map<String, String> getPlayerInformation() {
        return file.getPlayerInformation();
    }

    public ObservableList<ComparisonRowBase<?>> getComparisonStatistics() {
        return comparisonStatistics;
    }

    public ObservableList<Data> getPlanUsage(String playerName) {
        return planUsages.get(playerName);
    }

    private ObservableList<Series<Double, Integer>> playerKillsSeries = FXCollections.observableArrayList();

    public ObservableList<Series<Double, Integer>> getPlayerKillsSeries() {
        return playerKillsSeries;
    }

    public Map<String, String> getPlayerColors() {
        return file.getPlayerColors();
    }

    @Override
    public void onStatisticsAdded(CBRShooterStatistics newStatistics, List<CBRShooterStatistics> statisticsCopy) {
        updatePlanUsage(newStatistics);

        for (var row : comparisonStatistics) {
            row.updateValues(file);
        }

        if (graphComparisonRow != null)
            graphComparisonRow.updateSeries(file);
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

}
