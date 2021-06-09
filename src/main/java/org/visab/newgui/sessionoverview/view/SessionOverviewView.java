package org.visab.newgui.sessionoverview.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.globalmodel.SessionStatus;
import org.visab.newgui.sessionoverview.viewmodel.SessionOverviewViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class SessionOverviewView implements FxmlView<SessionOverviewViewModel>, Initializable {

    @FXML
    private Button closeSessionButton;

    @FXML
    private TableView<SessionStatus> sessionTable;

    @InjectViewModel
    private SessionOverviewViewModel viewModel;

    @FXML
    public void closeSessionAction() {
        viewModel.closeSessionCommand().execute();
    }

    @FXML
    public void openLiveViewAction() {
        viewModel.openLiveViewCommand().execute();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sessionTable.setItems(viewModel.getSessionList());
        viewModel.selectedSessionProperty().bind(sessionTable.getSelectionModel().selectedItemProperty());

        // TODO:
        // initializeSessionTablePresentation();
    }

    private void initializeSessionTablePresentation() {
        // https://stackoverflow.com/questions/20350099/programmatically-change-the-tableview-row-appearance
        sessionTable.setRowFactory(new Callback<TableView<SessionStatus>, TableRow<SessionStatus>>() {

            @Override
            public TableRow<SessionStatus> call(TableView<SessionStatus> param) {
                var row = new TableRow<SessionStatus>() {

                    @Override
                    protected void updateItem(SessionStatus row, boolean empty) {
                        // TODO: CSS classes should be kept in a static file
                        if (empty || row == null)
                            return;

                        if (row.isActive()) {
                            getStyleClass().remove("sessionRowInactive");
                            getStyleClass().add("sessionRowActive");
                            // Add some style class for active row.
                        } else {
                            // Add some style class for inactive row.
                            getStyleClass().remove("sessionRowActive");
                            getStyleClass().add("sessionRowInactive");
                        }
                    }
                };

                return row;
            }
        });
    }
}
