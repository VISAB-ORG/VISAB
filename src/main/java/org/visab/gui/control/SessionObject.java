package org.visab.gui.control;

import org.visab.api.WebAPI;
import org.visab.globalmodel.SessionStatus;
import org.visab.gui.DialogHelper;
import org.visab.gui.DynamicViewLoader;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
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
public class SessionObject extends GridPane {

    // "Static" content
    private static Image CLOSE_ICON = new Image("/img/closeIcon.png", 12, 12, false, false);
    private Label ipLabel = new Label("IP:");
    private Label sessionOpenedLabel = new Label("Session Opened:");
    private Label sessionClosedLabel = new Label("Session Closed:");
    private Label lastRequestLabel = new Label("Last Request:");
    private Label totalRequestlabel = new Label("Total requests:");

    // Dynamic content
    private Button closeSessionButton;
    private Label gameNameValue;
    private ImageView gameIconView;
    private Label sessionIdValue;
    private Label ipValue;
    private Label sessionOpenedValue;
    private Label lastRequestValue;
    private Label sessionClosedValue;
    private Button openLiveViewButton;
    private Label totalRequestValue;

    public SessionObject(SessionStatus sessionStatus, String gameIconPath, double fitWidth) {
        this.setMinWidth(fitWidth);
        this.setMaxWidth(fitWidth);

        this.gameNameValue = new Label(sessionStatus.getGame());
        this.gameIconView = new ImageView(new Image(gameIconPath, 24, 24, false, false));
        String viewDescription;

        if (sessionStatus.getStatusType().equals("active")) {
            viewDescription = "Open Live View";
        } else {
            viewDescription = "Visualize";
        }
        this.openLiveViewButton = new Button(viewDescription);
        this.openLiveViewButton.setOnAction(e -> {
            if (sessionStatus.getStatusType().equals("active")) {
                DialogHelper.showMessageDialog(AlertType.WARNING,
                        "Please be aware that the live view is not fully optimized for Settlers of Catan and using it might result in strange application behaviour.",
                        "LiveView not fully supported!");
            }
            DynamicViewLoader.loadVisualizerView(sessionStatus.getGame(), sessionStatus.getSessionId());
        });

        this.sessionIdValue = new Label(sessionStatus.getSessionId().toString());
        this.ipValue = new Label(sessionStatus.getIp());
        this.sessionOpenedValue = new Label(sessionStatus.getSessionOpened().toString());

        // Session closed can be null, put fitting text if applicable
        if (sessionStatus.getSessionClosed() == null) {
            this.sessionClosedValue = new Label("not closed yet");
        } else {
            this.sessionClosedValue = new Label(sessionStatus.getSessionClosed().toString());
        }

        this.lastRequestValue = new Label(sessionStatus.getLastRequest().toString());
        this.totalRequestValue = new Label(String.valueOf(sessionStatus.getTotalRequests()));

        this.setPadding(new Insets(10));
        this.setHgap(5);
        this.setVgap(5);
        this.getStyleClass().add("innerGrid");

        ImageView closeSessionView = new ImageView(CLOSE_ICON);
        this.closeSessionButton = new Button();
        this.closeSessionButton.setPrefSize(10, 10);
        this.closeSessionButton.setGraphic(closeSessionView);
        this.closeSessionButton.getStyleClass().add("delete-button");

        this.closeSessionButton.setOnAction(
                e -> WebAPI.getInstance().getSessionAdministration().closeSession(sessionStatus.getSessionId()));

        this.closeSessionButton.setAlignment(Pos.TOP_RIGHT);
        this.add(this.closeSessionButton, 2, 0);
        this.add(this.gameNameValue, 0, 1);
        this.add(this.gameIconView, 1, 1);
        this.add(this.sessionIdValue, 0, 0, 2, 1);
        this.add(ipLabel, 0, 3);
        this.add(this.ipValue, 1, 3);
        this.add(sessionOpenedLabel, 0, 4);
        this.add(this.sessionOpenedValue, 1, 4);
        this.add(lastRequestLabel, 0, 5);
        this.add(this.lastRequestValue, 1, 5);
        this.add(sessionClosedLabel, 0, 6);
        this.add(this.sessionClosedValue, 1, 6);
        this.add(totalRequestlabel, 0, 7);
        this.add(totalRequestValue, 1, 7);
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
