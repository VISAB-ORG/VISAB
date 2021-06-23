package org.visab.newgui.visualize.cbrshooter.view;

import java.util.List;

import org.visab.newgui.visualize.IVisualizeMainView;
import org.visab.newgui.visualize.cbrshooter.viewmodel.CBRShooterStatisticsViewModel;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewModel;

/**
 * View that has the tabcontrol containing all possible views
 */
public class CBRShooterMainView implements IVisualizeMainView {

    @InjectViewModel
    CBRShooterStatisticsViewModel statisticsViewModel;

    @Override
    public List<ViewModel> getTabViewModels() {
        return List.of(statisticsViewModel);
    }

}
