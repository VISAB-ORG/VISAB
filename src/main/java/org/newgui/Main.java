package org.newgui;

public class Main {

    public static void main(String[] args) {
        new Thread(() -> javafx.application.Application.launch(SampleApplication.class)).start();
    }
}
