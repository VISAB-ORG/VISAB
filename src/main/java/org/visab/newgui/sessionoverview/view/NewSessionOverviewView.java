package org.visab.newgui.sessionoverview.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.sessionoverview.viewmodel.NewSessionOverviewViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class NewSessionOverviewView implements FxmlView<NewSessionOverviewViewModel>, Initializable {

    private Thread updateLoop;

    @FXML
    private Label webApiAdressLabel;

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

    @InjectViewModel
    private NewSessionOverviewViewModel viewModel;

    @FXML
    public void createDummySessions() {
        stopUpdateLoop();
        viewModel.createDummySessionsCommand(this.scrollPane).execute();
    }

    @FXML
    public void clearInactiveSessions() {
        // TODO: Add logic
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bind properties
        webApiAdressLabel.textProperty().bindBidirectional(viewModel.getWebApiAdressProperty());
        sessionsTotalLabel.textProperty().bindBidirectional(viewModel.getTotalSessionsProperty());
        sessionsActiveLabel.textProperty().bindBidirectional(viewModel.getActiveSessionsProperty());
        sessionsTimeoutedLabel.textProperty().bindBidirectional(viewModel.getTimeoutedSessionsProperty());
        sessionsCanceledLabel.textProperty().bindBidirectional(viewModel.getCanceledSessionsProperty());

        startUpdateLoop();
    }

    public void startUpdateLoop() {
        updateLoop = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (!this.isInterrupted()) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                viewModel.getSortedSessionGrid(scrollPane);
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        };
        updateLoop.start();
    }

    public void stopUpdateLoop() {
        updateLoop.interrupt();
    }

}
