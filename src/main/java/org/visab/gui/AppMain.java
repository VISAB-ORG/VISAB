package org.visab.gui;

import java.util.Arrays;

import org.visab.api.WebAPI;
import org.visab.gui.about.view.AboutView;
import org.visab.gui.main.view.HomeView;

import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
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
    	// TODO: Change this call with usage per DialogHelper to make use of scopes
        primaryStage = stage;
        stage.setTitle("VISAB");
        
        var viewConfig = new ShowViewConfiguration(HomeView.class, "VISAB", true);
        var viewStep = FluentViewLoader.fxmlView(viewConfig.getViewClass());

        stage.setTitle(viewConfig.getStageTitle());

        if (viewConfig.getWidth() != 0)
            stage.setMinWidth(viewConfig.getWidth());

        if (viewConfig.getHeight() != 0)
            stage.setMinHeight(viewConfig.getHeight());

        var additionalScope = new GeneralScope();
        additionalScope.setStage(stage);

        viewStep.providedScopes(additionalScope);

        var viewTuple = viewStep.load();
        var view = viewTuple.getView();

        stage.setScene(new Scene(view));

        stage.show();

        // Only access the HomeView at start, because any other View will be opened in a
        // new frame due to navigation
        // var viewTupel = FluentViewLoader.fxmlView(HomeView.class).load();
        // var root = viewTupel.getView();
        // stage.setMinHeight(400);
        // stage.setMinWidth(800);
        // stage.setScene(new Scene(root));
        // stage.show();
    }

    /**
     * Is called before the Application is csosed.
     */
    @Override
    public void stop() {
        WebAPI.getInstance().shutdown();
    }

}
