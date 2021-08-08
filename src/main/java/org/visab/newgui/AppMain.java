package org.visab.newgui;

import org.visab.api.WebAPI;
import org.visab.newgui.main.view.HomeView;

import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The javafx application.
 */
public class AppMain extends Application {

    private static Stage primaryStage;

    /**
     * The primary stage of the Application.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        stage.setTitle("VISAB");

        // Only access the HomeView at start, because any other View will be opened in a
        // new frame due to navigation
        var viewTupel = FluentViewLoader.fxmlView(HomeView.class).load();
        var root = viewTupel.getView();
        stage.setMinHeight(400);
        stage.setMinWidth(800);
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Is called before the Application is csosed.
     */
    @Override
    public void stop() {
        WebAPI.getInstance().shutdown();
    }

}
