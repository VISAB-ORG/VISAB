package org.visab.newgui.visualize.settlers;

import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.newgui.visualize.LiveStatisticsViewModelBase;

public class SettlersStatisticsViewModel extends LiveStatisticsViewModelBase<SettlersFile, SettlersStatistics> {

    /**
     * Called by javafx/mvvmfx once view is loaded - but before initialize in the
     * view.
     */
    public void initialize() {

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
