package org.visab.newgui.sessionoverview.view;

import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import org.visab.globalmodel.SessionStatus;
import org.visab.newgui.ResourceHelper;
import org.visab.newgui.control.CustomSessionObject;
import org.visab.newgui.sessionoverview.viewmodel.SessionOverviewViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class SessionOverviewView implements FxmlView<SessionOverviewViewModel>, Initializable {

    private static final int GRID_COL_SIZE = 3;

    private Thread updateLoop;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField webApiAdressLabel;

    @FXML
    private Label sessionsTotalLabel;

    @FXML
    private Label sessionsActiveLabel;

    @FXML
    private Label sessionsTimeoutedLabel;

    @FXML
    private Label sessionsCanceledLabel;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private GridPane sessionGrid;

    private AnchorPane anchorPane;

    @InjectViewModel
    private SessionOverviewViewModel viewModel;

    @FXML
    public void createDummySessions() {
        stopUpdateLoop();
        sessionGrid.getChildren().clear();
        anchorPane.getChildren().clear();
        var activeSessionsCount = 0;
        var timeoutedSessionsCount = 0;
        var canceledSessionsCount = 0;

        // Used to calculate coordinates on which the session objects should be placed
        var rowIterator = 0;
        var colIterator = 0;

        String status;

        for (int i = 0; i < 8; i++) {

            if (i % 2 == 0) {
                status = "active";
                activeSessionsCount++;
            } else if (i % 3 == 0) {
                status = "timeouted";
                timeoutedSessionsCount++;
            } else {
                status = "canceled";
                canceledSessionsCount++;
            }

            var logoPath = ResourceHelper.getLogoPathByGame("Settlers");

            // Customized JavaFX Gridpane which displays relevant session information
            SessionStatus dummyStatus = new SessionStatus(new UUID(0, 10), "DummyGame", true, LocalTime.now(),
                    LocalTime.now(), LocalTime.now(), 3, 1, 10, "127.0.0.1", status);
            CustomSessionObject sessionObject = new CustomSessionObject(dummyStatus, logoPath);

            sessionObject.setBackgroundColorByStatus(status);

            sessionGrid.add(sessionObject, colIterator, rowIterator);

            colIterator++;

            // Once there the col size is reached, move on to the next row
            if (colIterator == GRID_COL_SIZE) {
                rowIterator++;
                colIterator = 0;
            }
        }

        anchorPane.getChildren().add(sessionGrid);

        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setContent(anchorPane);

        sessionsTotalLabel.setText("8");
        sessionsActiveLabel.setText(String.valueOf(activeSessionsCount));
        sessionsTimeoutedLabel.setText(String.valueOf(timeoutedSessionsCount));
        sessionsCanceledLabel.setText(String.valueOf(canceledSessionsCount));
    }

    @FXML
    public void clearInactiveSessions() {
        viewModel.clearInactiveSessionsCommand().execute();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // TODO: Check if there is another way that does not violate MVVM rules
        viewModel.getScope().registerOnStageClosing(Consumer -> {
            if (updateLoop != null) {
                stopUpdateLoop();
            }
        });
        anchorPane = new AnchorPane();

        sessionGrid.setPadding(new Insets(10));
        sessionGrid.setHgap(5);
        sessionGrid.setVgap(5);

        webApiAdressLabel.setEditable(false);

        // Bind properties
        webApiAdressLabel.textProperty().bindBidirectional(viewModel.getWebApiAdressProperty());
        sessionsTotalLabel.textProperty().bindBidirectional(viewModel.getTotalSessionsProperty());
        sessionsActiveLabel.textProperty().bindBidirectional(viewModel.getActiveSessionsProperty());
        sessionsTimeoutedLabel.textProperty().bindBidirectional(viewModel.getTimeoutedSessionsProperty());
        sessionsCanceledLabel.textProperty().bindBidirectional(viewModel.getCanceledSessionsProperty());

        startUpdateLoop();
    }

    private void startUpdateLoop() {
        updateLoop = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (!this.isInterrupted()) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                updateSessionGrid();
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    } else {
                        break;
                    }
                }
            }
        };
        updateLoop.start();
    }

    private void updateSessionGrid() {
        sessionGrid.getChildren().clear();
        List<SessionStatus> statuses = viewModel.querySessionStatusesSorted();

        var rowIterator = 0;
        var colIterator = 0;

        for (SessionStatus sessionStatus : statuses) {
            var logoPath = ResourceHelper.getLogoPathByGame(sessionStatus.getGame());

            // Customized JavaFX Gridpane which displays relevant session information
            CustomSessionObject sessionObject = new CustomSessionObject(sessionStatus, logoPath);

            sessionObject.setBackgroundColorByStatus(sessionStatus.getStatusType());

            sessionGrid.add(sessionObject, colIterator, rowIterator);

            colIterator++;

            // Once there the col size is reached, move on to the next row
            if (colIterator == GRID_COL_SIZE) {
                rowIterator++;
                colIterator = 0;
            }

        }

        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(sessionGrid);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setContent(anchorPane);

    }

    public void stopUpdateLoop() {
        if (updateLoop != null) {
            updateLoop.interrupt();
        }
    }

}
