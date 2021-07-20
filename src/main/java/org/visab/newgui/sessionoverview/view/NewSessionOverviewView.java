package org.visab.newgui.sessionoverview.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.sessionoverview.viewmodel.NewSessionOverviewViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

public class NewSessionOverviewView implements FxmlView<NewSessionOverviewViewModel>, Initializable {

    private Thread updateLoop;

    @FXML
    private Button closeSessionButton;

    @FXML
    private ScrollPane scrollPane;

    @InjectViewModel
    private NewSessionOverviewViewModel viewModel;

    @FXML
    public void closeSessionAction() {
        viewModel.closeSessionCommand().execute();
    }

    @FXML
    public void openLiveViewAction() {
        viewModel.openLiveViewCommand().execute();
    }

    @FXML
    public void createDummySessions() {
        viewModel.createDummySessionsCommand(this.scrollPane).execute();
        updateLoop.interrupt();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startUpdateLoop();
    }

    public void startUpdateLoop() {
        updateLoop = new Thread() {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            viewModel.getSortedSessionGrid(scrollPane);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        updateLoop.start();
    }

    private void stopUpdateLoop() {
        updateLoop.interrupt();
    }

}
