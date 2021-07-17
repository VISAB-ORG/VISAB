package org.visab.newgui.visualize.cbrshooter.viewmodel;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.visualize.LiveViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;

import de.saxsys.mvvmfx.InjectScope;

public class CBRShooterMetaViewModel extends LiveViewModelBase<CBRShooterFile, CBRShooterStatistics> {

    public void initialize() {
        if (scope.isLive()) {
            super.initializeLive(scope.getSessionListener());
        } else {

        }
    }

    @Override
    public void onStatisticsAdded(CBRShooterStatistics newStatistics) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSessionClosed() {
        // TODO Auto-generated method stub

    }

}
