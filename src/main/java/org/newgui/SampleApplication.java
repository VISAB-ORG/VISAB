package org.newgui;

import org.newgui.webapi.WebApiView;

import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SampleApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("VISAB");

        // mvvmfx magic
        var viewTupel = FluentViewLoader.fxmlView(WebApiView.class).load();

        var root = viewTupel.getView();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Is called when the Application is closed.
     */
    @Override
    public void stop() {
        Main.shutdownWebApi();
    }
}
