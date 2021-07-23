package org.visab.newgui.visualize.settlers.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.LiveViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterImplicator;
import org.visab.newgui.visualize.settlers.model.PlayerPlanOccurance;
import org.visab.newgui.visualize.settlers.model.SettlersImplicator.BuildingType;
import org.visab.newgui.visualize.settlers.model.comparison.BuildingsBuiltComparisonRow;
import org.visab.newgui.visualize.settlers.model.comparison.PlayerTypeComparisonRow;
import org.visab.newgui.visualize.settlers.model.comparison.ResourcesGainedByDiceComparisonRow;
import org.visab.newgui.visualize.settlers.model.comparison.ResourcesSpentComparisonRow;
import org.visab.newgui.visualize.settlers.model.comparison.VictoryPointsComparisonRow;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart.Series;

public class SettlersStatisticsViewModel extends LiveViewModelBase<SettlersFile, SettlersStatistics> {

    @InjectScope
    VisualizeScope scope;

    /**
     * Only used within this viewmodel as a helper object to update the plan
     * occurances.
     */
    private HashMap<String, PlayerPlanOccurance> planOccuranceHelperMap;

    private ObservableList<ComparisonRowBase<?>> comparisonStatistics;
    private List<String> playerNames;
    private ObjectProperty<ComparisonRowBase<?>> selectedRowProperty = new SimpleObjectProperty<>();
    private Map<String, ObservableList<Data>> planUsages;

    private Map<String, Series<Integer, Number>> comparisonStatisticsSeries;
    
    private Command playerStatsChartCommand;
    
    private ObjectProperty<ComparisonRowBase<?>> selectedStatistics = new SimpleObjectProperty<>();
    
    private ObservableList<Series<Integer, Number>> playerStatsSeries = FXCollections.observableArrayList();
    
    // Set in command on show stats button click
    private ComparisonRowBase<?> graphComparisonRow;
    private StringProperty yLabel = new SimpleStringProperty();
    
    public ObservableList<Series<Integer, Number>> getPlayerStatsSeries() {
        return playerStatsSeries;
    }
    
    public ObjectProperty<ComparisonRowBase<?>> selectedStatisticsProperty() {
        return selectedStatistics;
    }
    
    public Command playerStatsChartCommand() {
        if (playerStatsChartCommand == null) {
            playerStatsChartCommand = runnableCommand(() -> {
                var selectedRow = selectedStatistics.get();
                if (selectedRow != null) {

                    selectedRow.updateSeries(file);
                    playerStatsSeries.clear();
                    playerStatsSeries.addAll(selectedRow.getPlayerSeries().values());
                    
                    yLabel.set(selectedRow.getRowDescription());

                    graphComparisonRow = selectedRow;
                }
            });
        }
        return playerStatsChartCommand;
    }

    /**
     * Called by javafx/mvvmfx once view is loaded - but before initialize in the
     * view.
     */
    public void initialize() {
        if (scope.isLive()) {
            super.initializeLive(scope.getSessionListener());

            // Register ourselves, for when the view closes
            scope.registerOnStageClosing(e -> onSessionClosed());

            // Initialize the data structures used for visualization
            initializeDataStructures(file);

            // Notify for all the already received statistics
            for (var statistics : listener.getStatisticsCopy())
                onStatisticsAdded(statistics, listener.getStatisticsCopy());
        } else {
            super.initialize(scope.getFile());

            // Initialize the data structures used for visualization
            initializeDataStructures(file);

            for (var statistics : file.getStatistics())
                onStatisticsAdded(statistics, file.getStatistics());
        }
    }

    private void initializeDataStructures(SettlersFile file) {
        // Initialize player names
        playerNames = new ArrayList<String>(file.getPlayerNames());

        // Initialize comparison statistics
        comparisonStatistics = FXCollections.observableArrayList();
        comparisonStatistics.add(new PlayerTypeComparisonRow());
        comparisonStatistics.add(new BuildingsBuiltComparisonRow(BuildingType.Road));
        comparisonStatistics.add(new BuildingsBuiltComparisonRow(BuildingType.Village));
        comparisonStatistics.add(new BuildingsBuiltComparisonRow(BuildingType.Town));
        comparisonStatistics.add(new ResourcesGainedByDiceComparisonRow());
        comparisonStatistics.add(new ResourcesSpentComparisonRow());
        comparisonStatistics.add(new VictoryPointsComparisonRow());

        // Initialize plan usages
        planUsages = new HashMap<>();
        planOccuranceHelperMap = new HashMap<>();
        for (String name : playerNames) {
            var dataList = FXCollections.<Data>observableArrayList();
            planUsages.put(name, dataList);
            planOccuranceHelperMap.put(name, new PlayerPlanOccurance(name));
        }

        comparisonStatisticsSeries = new HashMap<>();
        for (String name : playerNames) {
            var series = new Series<Integer, Number>();
            comparisonStatisticsSeries.put(name, series);
        }
    }

    private void updateComparisonStatistics(SettlersFile file) {
        for (var row : comparisonStatistics)
            row.updateValues(file);
    }

    private void updateComparisonStatisticsSeries(SettlersFile file) {

    }

    private void updatePlanUsage(SettlersStatistics newStatistics) {
        for (var player : newStatistics.getPlayers()) {
            var occurance = planOccuranceHelperMap.get(player.getName());
            occurance.incrementOccurance(player.getPlanActions());

            for (String plan : player.getPlanActions()) {
                // If a plan was just added
                if (occurance.getTotalOccurances(plan) == 1) {
                    var data = new Data(plan, 0);
                    data.pieValueProperty().bind(occurance.getOccuranceProperty(plan));
                    planUsages.get(player.getName()).add(data);
                }
            }
        }
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    @Override
    public void onStatisticsAdded(SettlersStatistics newStatistics, List<SettlersStatistics> statisticsCopy) {
        updateComparisonStatistics(file);
        updatePlanUsage(newStatistics);
    }

    @Override
    public void onSessionClosed() {
        liveSessionActiveProperty().set(false);
        listener.removeViewModel(this);
    }

    public ObservableList<ComparisonRowBase<?>> getComparisonStatistics() {
        return comparisonStatistics;
    }

    public ObjectProperty<ComparisonRowBase<?>> selectedRowProperty() {
        return selectedRowProperty;
    }

    public Map<String, ObservableList<Data>> getPlanUsages() {
        return planUsages;
    }

}
