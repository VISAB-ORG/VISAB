package org.visab.gui.sessionoverview.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.visab.globalmodel.SessionStatus;
import org.visab.gui.ResourceHelper;
import org.visab.gui.control.SessionObject;
import org.visab.gui.sessionoverview.viewmodel.SessionOverviewViewModel;

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

    private static final double SESSION_OBJECT_FIT_WIDTH = 300.0;

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
    public void clearInactiveSessions() {
        viewModel.clearInactiveSessionsCommand().execute();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel.getScope().registerOnStageClosing(Consumer -> {
            if (updateLoop != null) {
                updateLoop.interrupt();
            }
        });
        anchorPane = new AnchorPane();

        sessionGrid.setPadding(new Insets(10));
        sessionGrid.setHgap(5);
        sessionGrid.setVgap(5);

        webApiAdressLabel.setEditable(false);

        // Bind properties
        webApiAdressLabel.textProperty().bind(viewModel.webApiAdressProperty());
        sessionsTotalLabel.textProperty().bind(viewModel.totalSessionsProperty().asString());
        sessionsActiveLabel.textProperty().bind(viewModel.activeSessionsProperty().asString());
        sessionsTimeoutedLabel.textProperty().bind(viewModel.timeoutedSessionsProperty().asString());
        sessionsCanceledLabel.textProperty().bind(viewModel.canceledSessionsProperty().asString());

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

        var scrollPaneWidth = scrollPane.getWidth();

        for (SessionStatus sessionStatus : statuses) {
            var logoPath = ResourceHelper.getLogoPathByGame(sessionStatus.getGame());

            // Customized JavaFX Gridpane which displays relevant session information
            SessionObject sessionObject = new SessionObject(sessionStatus, logoPath, 300.0);

            sessionObject.setBackgroundColorByStatus(sessionStatus.getStatusType());

            sessionGrid.add(sessionObject, colIterator, rowIterator);

            colIterator++;

            var colDelimiter = (int) (scrollPaneWidth / SESSION_OBJECT_FIT_WIDTH);
            // Once there the col size is reached, move on to the next row
            if (colIterator == colDelimiter) {
                rowIterator++;
                colIterator = 0;
            }

        }

        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(sessionGrid);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setContent(anchorPane);
    }

}
