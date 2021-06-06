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
        WebApi.getInstance().shutdown();
    }

    private static void startWebApi() {
        try {
            WebApi.getInstance().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
