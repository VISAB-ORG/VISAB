package org.visab.newgui;

import java.io.IOException;

import org.visab.api.WebApi;
import org.visab.util.Settings;

import javafx.application.Application;

public class Main {

    private static WebApi webApi;

    public static void main(String[] args) {
        new Thread(() -> Application.launch(AppMain.class)).start();
        startWebApi();
    }

    public static void shutdownWebApi() {
        webApi.shutdown();
    }

    private static void startWebApi() {
        try {
            webApi = new WebApi(Settings.API_PORT);
            webApi.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
