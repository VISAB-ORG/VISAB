package org.visab.newgui.control;

import java.util.UUID;

import org.visab.api.WebApi;
import org.visab.newgui.DynamicViewLoader;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * This class represents a container that holds any information for a game
 * session in VISAB. Furthermore it contains functionality directly tied to the
 * respective session.
 * 
 * @author vanessa
 *
 */
public class CustomSessionObject extends GridPane {

    // "Static" content
    private static Image CLOSE_ICON = new Image("/img/closeIcon.png", 12, 12, false, false);
    private Label sessionIdLabel = new Label("Session ID:");
    private Label hostNameLabel = new Label("Host Name:");
    private Label ipLabel = new Label("IP:");
    private Label sessionOpenedLabel = new Label("Session Opened:");
    private Label sessionClosedLabel = new Label("Session Closed:");
    private Label lastRequestLabel = new Label("Last Request:");

    // Dynamic content
    private Button closeSessionButton;
    private Label gameNameValue;
    private ImageView gameIconView;
    private Label sessionIdValue;
    private Label hostNameValue;
    private Label ipValue;
    private Label sessionOpenedValue;
    private Label lastRequestValue;
    private Label sessionClosedValue;
    private Button openLiveViewButton;

    public CustomSessionObject(String gameName, String gameIconPath, UUID sessionId, String hostName, String ip,
            String sessionOpened, String sessionStatusType) {

        this.gameNameValue = new Label(gameName);
        this.gameIconView = new ImageView(new Image(gameIconPath, 24, 24, false, false));
        String viewDescription;
        
        if (sessionStatusType.equals("active")) {
        	viewDescription="Open Live View";
        } else {
        	viewDescription="Visualize";
        }
        this.openLiveViewButton = new Button(viewDescription);
        this.openLiveViewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                DynamicViewLoader.loadVisualizer(gameName, sessionId);
            }
        });
        this.sessionIdValue = new Label(sessionId.toString());
        this.hostNameValue = new Label(hostName);
        this.ipValue = new Label(ip);
        this.sessionOpenedValue = new Label(sessionOpened);
        this.sessionClosedValue = new Label("");
        this.lastRequestValue = new Label("");

        this.setPadding(new Insets(10));
        this.setHgap(5);
        this.setVgap(5);
        this.getStyleClass().add("innerGrid");

        ImageView closeSessionView = new ImageView(CLOSE_ICON);
        this.closeSessionButton = new Button();
        this.closeSessionButton.setPrefSize(10, 10);
        this.closeSessionButton.setGraphic(closeSessionView);
        this.closeSessionButton.getStyleClass().add("delete-button");

        this.closeSessionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                WebApi.getInstance().getSessionAdministration().closeSession(sessionId);
            }
        });
        this.closeSessionButton.setAlignment(Pos.CENTER_RIGHT);
        this.add(this.closeSessionButton, 1, 0);
        this.add(this.gameNameValue, 0, 1);
        this.add(this.gameIconView, 1, 1);
        this.add(sessionIdLabel, 0, 2);
        this.add(this.sessionIdValue, 1, 2);
        this.add(hostNameLabel, 0, 3);
        this.add(this.hostNameValue, 1, 3);
        this.add(ipLabel, 0, 4);
        this.add(this.ipValue, 1, 4);
        this.add(sessionOpenedLabel, 0, 5);
        this.add(this.sessionOpenedValue, 1, 5);
        this.add(lastRequestLabel, 0, 6);
        this.add(this.lastRequestValue, 1, 6);
        this.add(sessionClosedLabel, 0, 7);
        this.add(this.sessionClosedValue, 1, 7);
        this.add(this.openLiveViewButton, 0, 8, 2, 1);

        this.setBackgroundColorByStatus(sessionStatusType);
    }

    /**
     * Simple method that gets used to color-code the respective session object in
     * the GUI based on the current status.
     * 
     * @param sessionStatus the status of the session that is referred to.
     */
    public void setBackgroundColorByStatus(String sessionStatusType) {
        switch (sessionStatusType) {
        case "active":
            this.setStyle(
                    "-fx-border-style: solid hidden hidden hidden; -fx-border-color: green; -fx-border-width: 10px; -fx-border-radius: 0px;");

            break;
        case "canceled":
            this.setStyle(
                    "-fx-border-style: solid hidden hidden hidden; -fx-border-color: red; -fx-border-width: 10px; -fx-border-radius: 0px;");
            break;
        case "timeouted":
            this.setStyle(
                    "-fx-border-style: solid hidden hidden hidden; -fx-border-color: grey; -fx-border-width: 10px; -fx-border-radius: 0px;");
            break;
        }
    }   
    
}
