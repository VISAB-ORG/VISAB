package org.visab.newgui.visualize.settlers.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.LiveStatisticsViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;

import de.saxsys.mvvmfx.InjectScope;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SettlersStatisticsViewModel extends LiveStatisticsViewModelBase<SettlersFile, SettlersStatistics> {

    @InjectScope
    VisualizeScope scope;

    private ObservableList<ComparisonRowBase<?>> comparisonStatistics = FXCollections.observableArrayList();

    private List<String> playerNames;

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

    private void initializeDataStructures(SettlersFile file) {
        playerNames = new ArrayList<String>(file.getPlayerInformation().values());

        // TODO:
    }

    @Override
    public void notifyStatisticsAdded(SettlersStatistics newStatistics) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifySessionClosed() {
        // TODO Auto-generated method stub

    }

    public ObservableList<ComparisonRowBase<?>> getComparisonStatistics() {
        return comparisonStatistics;
    }

}
