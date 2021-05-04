package org.newgui;

import java.io.IOException;

import org.visab.api.WebApi;
import org.visab.util.Settings;

import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SampleApplication extends Application {

    private WebApi webApi;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("VISAB");

        // mvvmfx magic
        var viewTupel = FluentViewLoader.fxmlView(WebApiView.class).load();

        var root = viewTupel.getView();
        stage.setScene(new Scene(root));
        stage.show();
        
        startApiServer();
    }

    private void startApiServer() {
        try {
            webApi = new WebApi(Settings.API_PORT);
            webApi.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return;
    }

    /**
     * Is called when the Application is closed.
     */
    @Override
    public void stop() {
        webApi.shutdown();
    }
}
