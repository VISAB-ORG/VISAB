package org.visab.newgui.visualize.settlers.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.LiveStatisticsViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;
import org.visab.newgui.visualize.settlers.model.PlayerPlanOccurance;
import org.visab.newgui.visualize.settlers.model.SettlersImplicator.BuildingType;
import org.visab.newgui.visualize.settlers.model.comparison.BuildingsBuiltComparisonRow;
import org.visab.newgui.visualize.settlers.model.comparison.ResourcesGainedByDiceComparisonRow;
import org.visab.newgui.visualize.settlers.model.comparison.ResourcesSpentComparisonRow;
import org.visab.newgui.visualize.settlers.model.comparison.VictoryPointsComparisonRow;

import de.saxsys.mvvmfx.InjectScope;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart.Data;

public class SettlersStatisticsViewModel extends LiveStatisticsViewModelBase<SettlersFile, SettlersStatistics> {

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

    /**
     * Called by javafx/mvvmfx once view is loaded - but before initialize in the
     * view.
     */
    public void initialize() {
        if (scope.isLive()) {
            super.initializeLive(scope.getSessionListener());

            // Initialize the data structures used for visualization
            initializeDataStructures(file);

            // Notify for all the already received statistics
            for (var statistics : listener.getReceivedStatistics())
                onStatisticsAdded(statistics);
        } else {
            super.initialize(scope.getFile());

            // Initialize the data structures used for visualization
            initializeDataStructures(file);

            for (var statistics : file.getStatistics())
                onStatisticsAdded(statistics);
        }
    }

    private void initializeDataStructures(SettlersFile file) {
        // Initialize player names
        playerNames = new ArrayList<String>(file.getPlayerInformation().keySet());

        // Initialize comparison statistics
        comparisonStatistics = FXCollections.observableArrayList();
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

        // TODO:
    }

    private void updateComparisonStatistics(SettlersFile file) {
        for (var row : comparisonStatistics)
            row.updateValues(file);
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
    public void onStatisticsAdded(SettlersStatistics newStatistics) {
        updateComparisonStatistics(file);
        updatePlanUsage(newStatistics);
    }

    @Override
    public void onSessionClosed() {
        // TODO Auto-generated method stub

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
