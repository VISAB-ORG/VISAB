package org.visab.newgui.visualize.cbrshooter.viewmodel;

import org.visab.globalmodel.IVISABFile;
import org.visab.newgui.ViewModelBase;
import org.visab.newgui.visualize.IVisualizeMainViewModel;
import org.visab.newgui.visualize.VisualizeScope;
import org.visab.processing.ILiveViewable;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;

@ScopeProvider(VisualizeScope.class)
public class CBRShooterMainViewModel extends ViewModelBase implements IVisualizeMainViewModel {

    @InjectScope
    VisualizeScope scope;

    @Override
    public void setFile(IVISABFile file) {
        scope.setFile(file);
    }

    @Override
    public void setListener(ILiveViewable<?> listener) {
        scope.setSessionListener(listener);
    }
}
