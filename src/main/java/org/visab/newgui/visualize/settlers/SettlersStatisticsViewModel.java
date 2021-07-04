package org.visab.newgui.visualize.settlers;

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

    ObservableList<ComparisonRowBase<?>> comparisonRows = FXCollections.observableArrayList();

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

        // DO something with file from scope
    }

    private void initializeDataStructures(SettlersFile file) {

    }

    @Override
    public void notifyStatisticsAdded(SettlersStatistics newStatistics) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifySessionClosed() {
        // TODO Auto-generated method stub

    }

}
