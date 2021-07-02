package org.visab.newgui.control;

import java.util.UUID;

import javafx.geometry.Insets;
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

    // closeIcon can be a static class variable because it is always the same

    // Static content
    private static final Image CLOSE_ICON = new Image("/img/closeIcon.png", 12, 12, false, false);
    private static final Label SESSION_ID_LABEL = new Label("Session ID:");
    private static final Label HOST_NAME_LABEL = new Label("Host Name:");
    private static final Label IP_LABEL = new Label("IP:");
    private static final Label SESSION_OPENED_LABEL = new Label("Session Opened:");
    private static final Label SESSION_CLOSED_LABEL = new Label("Session Closed:");
    private static final Label LAST_REQUEST_LABEL = new Label("Last Request:");

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
            String sessionOpened, String sessionStatus) {

        this.setBackgroundColor(sessionStatus);
        this.gameNameValue = new Label(gameName);
        this.gameIconView = new ImageView(new Image(gameIconPath, 24, 24, false, false));
        this.openLiveViewButton = new Button("Open Live View");
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

        this.add(this.closeSessionButton, 1, 0);
        this.add(this.gameNameValue, 0, 1);
        this.add(this.gameIconView, 1, 1);
        this.add(SESSION_ID_LABEL, 0, 2);
        this.add(this.sessionIdValue, 1, 2);
        this.add(HOST_NAME_LABEL, 0, 3);
        this.add(this.hostNameValue, 1, 3);
        this.add(IP_LABEL, 0, 4);
        this.add(this.ipValue, 1, 4);
        this.add(SESSION_OPENED_LABEL, 0, 5);
        this.add(this.sessionOpenedValue, 1, 5);
        this.add(LAST_REQUEST_LABEL, 0, 6);
        this.add(this.lastRequestValue, 1, 6);
        this.add(SESSION_CLOSED_LABEL, 0, 7);
        this.add(this.sessionClosedValue, 1, 7);
        this.add(this.openLiveViewButton, 0, 8, 2, 1);
    }

    /**
     * Simple method that gets used to color-code the respective session object in
     * the GUI based on the current status.
     * 
     * @param sessionStatus the status of the session that is referred to.
     */
    public void setBackgroundColor(String sessionStatus) {
        switch (sessionStatus) {
        case "active":
            this.setBackgroundColor("green");
            break;
        case "canceled":
            this.setBackgroundColor("red");
            break;
        case "timeouted":
            this.setBackgroundColor("grey");
            break;
        }
    }
}
