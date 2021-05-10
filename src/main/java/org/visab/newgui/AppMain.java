package org.visab.newgui;

import org.visab.newgui.webapi.WebApiView;
import org.visab.newgui.workspace.database.DatabaseView;

import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        stage.setTitle("VISAB");

        // mvvmfx magic
        // var viewTupel = FluentViewLoader.fxmlView(WebApiView.class).load();
        // var viewTupel = FluentViewLoader.fxmlView(RepositoryView.class).load();
        var viewTupel = FluentViewLoader.fxmlView(DatabaseView.class).load();
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
