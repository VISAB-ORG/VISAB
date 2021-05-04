package org.newgui;

import java.io.IOException;

import org.visab.api.WebApi;
import org.visab.util.Settings;

public class Main {

    private static WebApi webApi;
    public static void main(String[] args) {
        new Thread(() -> javafx.application.Application.launch(SampleApplication.class)).start();
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
