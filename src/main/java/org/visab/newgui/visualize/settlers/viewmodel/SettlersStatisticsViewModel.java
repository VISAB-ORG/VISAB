package org.visab.newgui.visualize.settlers.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.LiveStatisticsViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;
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

public class SettlersStatisticsViewModel extends LiveStatisticsViewModelBase<SettlersFile, SettlersStatistics> {

    @InjectScope
    VisualizeScope scope;

    private ObservableList<ComparisonRowBase<?>> comparisonStatistics;
    private List<String> playerNames;
    private ObjectProperty<ComparisonRowBase<?>> selectedRowProperty = new SimpleObjectProperty<>();

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
        playerNames = new ArrayList<String>(file.getPlayerInformation().keySet());

        comparisonStatistics = FXCollections.observableArrayList();
        comparisonStatistics.add(new BuildingsBuiltComparisonRow(BuildingType.Road));
        comparisonStatistics.add(new BuildingsBuiltComparisonRow(BuildingType.Village));
        comparisonStatistics.add(new BuildingsBuiltComparisonRow(BuildingType.Town));
        comparisonStatistics.add(new ResourcesGainedByDiceComparisonRow());
        comparisonStatistics.add(new ResourcesSpentComparisonRow());
        comparisonStatistics.add(new VictoryPointsComparisonRow());

        // TODO:
    }

    private void updateComparisonStatistics() {
        for (var row : comparisonStatistics)
            row.updateValues(file);
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    @Override
    public void onStatisticsAdded(SettlersStatistics newStatistics) {
        updateComparisonStatistics();
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

}
