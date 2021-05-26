package org.visab.newgui.webapi;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.webapi.model.SessionInformation;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class WebApiView implements FxmlView<WebApiViewModel>, Initializable {

    @FXML
    private Button closeSessionButton;

    @FXML
    private TableView<SessionInformation> sessionTable;

    @InjectViewModel
    private WebApiViewModel viewModel;

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
        /*
         * closeSessionCommand = viewModel.closeSessionCommand();
         * 
         * // TODO: Understand this binding logic
         * closeSessionButton.disableProperty().bind(closeSessionCommand.
         * notExecutableProperty());
         */

        sessionTable.setItems(viewModel.getSessions());
        viewModel.selectedSessionProperty().bind(sessionTable.getSelectionModel().selectedItemProperty());

        // TODO:
        // initializeSessionTablePresentation();
    }

    private void initializeSessionTablePresentation() {
        // https://stackoverflow.com/questions/20350099/programmatically-change-the-tableview-row-appearance
        sessionTable.setRowFactory(new Callback<TableView<SessionInformation>, TableRow<SessionInformation>>() {

            @Override
            public TableRow<SessionInformation> call(TableView<SessionInformation> param) {
                var row = new TableRow<SessionInformation>() {

                    @Override
                    protected void updateItem(SessionInformation row, boolean empty) {
                        // TODO: CSS classes should be kept in a static file
                        if (empty || row == null)
                            return;

                        if (row.getIsActive()) {
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
