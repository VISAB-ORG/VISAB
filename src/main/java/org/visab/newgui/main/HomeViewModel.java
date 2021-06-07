package org.visab.newgui.main;

import org.visab.newgui.AppMain;
import org.visab.newgui.DynamicViewLoader;
import org.visab.newgui.ViewModelBase;
import org.visab.newgui.webapi.WebApiView;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeViewModel extends ViewModelBase implements ViewModel {

	private Command openApiDashboard;

	public Command openApi() {
		if (openApiDashboard == null) {
			openApiDashboard = runnableCommand(() -> {
				DynamicViewLoader.showView(WebApiView.class, "API Dashboard");

			});
		}

		return openApiDashboard;

	}

}
