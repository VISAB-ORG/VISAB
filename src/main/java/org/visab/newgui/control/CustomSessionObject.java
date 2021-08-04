package org.visab.newgui.control;

import org.visab.api.WebAPI;
import org.visab.globalmodel.SessionStatus;
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

    public CustomSessionObject(SessionStatus sessionStatus, String gameIconPath) {

        this.gameNameValue = new Label(sessionStatus.getGame());
        this.gameIconView = new ImageView(new Image(gameIconPath, 24, 24, false, false));
        String viewDescription;

        if (sessionStatus.getStatusType().equals("active")) {
            viewDescription = "Open Live View";
        } else {
            viewDescription = "Visualize";
        }
        this.openLiveViewButton = new Button(viewDescription);
        this.openLiveViewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // TODO: Popup if live viewing not supported
                DynamicViewLoader.loadVisualizer(sessionStatus.getGame(), sessionStatus.getSessionId());
            }
        });
        this.sessionIdValue = new Label(sessionStatus.getSessionId().toString());
        this.hostNameValue = new Label("TODO");
        this.ipValue = new Label(sessionStatus.getIp());
        this.sessionOpenedValue = new Label(sessionStatus.getSessionOpened().toString());

        // Session closed can be null, put fitting text if applicable
        if (sessionStatus.getSessionClosed() == null) {
            this.sessionClosedValue = new Label("not closed yet");
        } else {
            this.sessionClosedValue = new Label(sessionStatus.getSessionClosed().toString());
        }

        this.lastRequestValue = new Label(sessionStatus.getLastRequest().toString());

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
                if (sessionStatus.isActive())
                    WebAPI.getInstance().getSessionAdministration().closeSession(sessionStatus.getSessionId());
            }
        });
        this.closeSessionButton.setAlignment(Pos.TOP_RIGHT);
        this.add(this.closeSessionButton, 2, 0);
        this.add(this.gameNameValue, 0, 1);
        this.add(this.gameIconView, 1, 1);
        // this.add(sessionIdLabel, 0, 2);
        this.add(this.sessionIdValue, 0, 0, 2, 1);
        // this.add(this.sessionIdValue, 0, 0);
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
        this.addRow(8, this.openLiveViewButton);

        this.setBackgroundColorByStatus(sessionStatus.getStatusType());
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
