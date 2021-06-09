package org.visab.newgui;

import org.visab.newgui.main.view.HomeView;
import org.visab.newgui.settings.SettingsView;

import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application {

    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        stage.setTitle("VISAB");

        // mvvmfx magic
        // var viewTupel = FluentViewLoader.fxmlView(WebApiView.class).load();
        // var viewTupel = FluentViewLoader.fxmlView(DatabaseView.class).load();
        // var viewTupel = FluentViewLoader.fxmlView(WebApiView.class).load();
        /*var viewTupel = FluentViewLoader.fxmlView(SettingsView.class).load();
        var root = viewTupel.getView();
        stage.setMinHeight(400);
        stage.setMinWidth(600);
        stage.setScene(new Scene(root));
        stage.show();*/
        var viewTupel = FluentViewLoader.fxmlView(HomeView.class).load();
        var root = viewTupel.getView();
        stage.setMinHeight(400);
        stage.setMinWidth(600);
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
