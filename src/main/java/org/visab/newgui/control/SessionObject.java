package org.visab.newgui.control;

import java.util.UUID;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SessionObject extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // TODO Auto-generated method stub
        GridPane innerGrid = new GridPane();
        innerGrid.setPadding(new Insets(10));
        innerGrid.setHgap(5);
        innerGrid.setVgap(5);
        // Setting the style CSS

        CustomSessionObject cso1 = new CustomSessionObject("Settlers", "/img/settlersLogo.png", new UUID(0, 0),
                "localhost", "127.0.0.1", "12:15", "active");
        CustomSessionObject cso2 = new CustomSessionObject("Settlers", "/img/settlersLogo.png", new UUID(0, 0),
                "localhost", "127.0.0.1", "12:15", "active");
        CustomSessionObject cso3 = new CustomSessionObject("Settlers", "/img/settlersLogo.png", new UUID(0, 0),
                "localhost", "127.0.0.1", "12:15", "active");
        CustomSessionObject cso4 = new CustomSessionObject("Settlers", "/img/settlersLogo.png", new UUID(0, 0),
                "localhost", "127.0.0.1", "12:15", "active");

        innerGrid.add(cso1, 0, 0);
        innerGrid.add(cso2, 1, 0);
        innerGrid.add(cso3, 0, 1);
        innerGrid.add(cso4, 1, 1);

        Scene scene = new Scene(innerGrid);
        scene.getStylesheets().add("/template_style.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("GridPane Beispiel");
        primaryStage.show();

    }
}
