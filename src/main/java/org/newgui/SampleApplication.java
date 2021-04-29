package org.newgui;

import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SampleApplication extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Sample Application");
        var viewTupel = FluentViewLoader.fxmlView(SampleView.class).load();

        var root = viewTupel.getView();
        stage.setScene(new Scene(root));
        stage.show();
    }
    
}
