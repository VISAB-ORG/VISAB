package org.visab.newgui.main.viewmodel;

import org.visab.newgui.DynamicViewLoader;
import org.visab.newgui.ViewModelBase;
import org.visab.newgui.main.MainScope;
import org.visab.newgui.sessionoverview.view.SessionOverviewView;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Command;

@ScopeProvider(MainScope.class)
public class HomeViewModel extends ViewModelBase implements ViewModel {

    @InjectScope
    MainScope scope;

    private Command openApiDashboard;

    public Command openApi() {
        if (openApiDashboard == null) {
            openApiDashboard = runnableCommand(() -> {
                DynamicViewLoader.showView(SessionOverviewView.class, "API Dashboard");

            });
        }

        return openApiDashboard;
    }

}
