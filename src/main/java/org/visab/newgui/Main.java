package org.visab.newgui;

import java.io.IOException;

import org.visab.api.WebApi;

import javafx.application.Application;

public class Main {

    public static void main(String[] args) {
        new Thread(() -> Application.launch(AppMain.class)).start();
        startWebApi();
    }

    public static void shutdownWebApi() {
        WebApi.instance.shutdown();
    }

    private static void startWebApi() {
        try {
            WebApi.instance.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
