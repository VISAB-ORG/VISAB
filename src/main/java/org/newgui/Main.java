package org.newgui;

import java.io.IOException;

import org.newgui.repository.Test;
import org.newgui.repository.Test2;
import org.visab.api.WebApi;
import org.visab.util.Settings;
import javafx.application.Application;
public class Main {

    private static WebApi webApi;
    public static void main(String[] args) {
        // new Thread(() -> Application.launch(SampleApplication.class)).start();
        new Thread(() -> Application.launch(Test.class)).start();
        new Thread(() -> Application.launch(Test2.class)).start();
        startWebApi();
    }

    private static void startWebApi() {
        try {
            webApi = new WebApi(Settings.API_PORT);
            webApi.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void shutdownWebApi() {
        webApi.shutdown();
    }
}
