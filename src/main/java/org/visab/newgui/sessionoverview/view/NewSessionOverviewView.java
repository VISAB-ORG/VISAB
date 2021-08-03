package org.visab.newgui.sessionoverview.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.sessionoverview.viewmodel.NewSessionOverviewViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

public class NewSessionOverviewView implements FxmlView<NewSessionOverviewViewModel>, Initializable {

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

        webApiAdressLabel.setEditable(false);

        // Bind properties
        webApiAdressLabel.textProperty().bindBidirectional(viewModel.getWebApiAdressProperty());
        sessionsTotalLabel.textProperty().bindBidirectional(viewModel.getTotalSessionsProperty());
        sessionsActiveLabel.textProperty().bindBidirectional(viewModel.getActiveSessionsProperty());
        sessionsTimeoutedLabel.textProperty().bindBidirectional(viewModel.getTimeoutedSessionsProperty());
        sessionsCanceledLabel.textProperty().bindBidirectional(viewModel.getCanceledSessionsProperty());

        viewModel.startUpdateLoop(scrollPane);
    }

    public void stopUpdateLoop() {
        viewModel.stopUpdateLoop();
    }

}
