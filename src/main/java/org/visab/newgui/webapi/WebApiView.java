package org.visab.newgui.webapi;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.dynamic.DynamicViewLoader;
import org.visab.newgui.statistics.LiveStatisticsViewModelBase;
import org.visab.newgui.statistics.StatisticsViewModelBase;
import org.visab.newgui.webapi.model.SessionTableRow;
import org.visab.processing.ILiveViewable;
import org.visab.processing.SessionListenerAdministration;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class WebApiView implements FxmlView<WebApiViewModel>, Initializable {

    @FXML
    private Button closeSessionButton;

    private Command closeSessionCommand;

    @FXML
    private TableView<SessionTableRow> sessionTable;

    @FXML
    Button openLiveViewButton;

    @InjectViewModel
    private WebApiViewModel viewModel;

    // TODO: Why is this needed? Can't we just bind directly and thats it?
    @FXML
    public void closeSessionAction() {
        closeSessionCommand.execute();
    }

    // TODO: Make this a viewmodel command?
    @FXML
    public void openLiveView() {
        var selectedRow = sessionTable.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            var listener = SessionListenerAdministration.getSessionListener(selectedRow.getSessionId());

            var viewTupel = DynamicViewLoader.loadStatisticsView(selectedRow.getGame());
            var root = viewTupel.getView();
            var VM = (StatisticsViewModelBase<?>) viewTupel.getViewModel();

            if (VM.supportsLiveViewing() && listener instanceof ILiveViewable<?>) {
                var liveListener = (ILiveViewable<?>) listener;
                var liveVM = (LiveStatisticsViewModelBase<?, ?>) VM;

                liveVM.initialize(liveListener);

                // Set the Ui
                var stage = new Stage();
                stage.setTitle("Statistics Window test");
                stage.setScene(new Scene(root));
                stage.show();
            }
        }
    }

    // Called after the view is completely loaded
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        closeSessionCommand = viewModel.getCloseSessionCommand();

        // TODO: Understand this binding logic
        closeSessionButton.disableProperty().bind(closeSessionCommand.notExecutableProperty());

        sessionTable.setItems(viewModel.getSessions());
        viewModel.selectedSessionRowProperty().bind(sessionTable.getSelectionModel().selectedItemProperty());

        // TODO:
        // initializeSessionTablePresentation();
    }

    private void initializeSessionTablePresentation() {
        // https://stackoverflow.com/questions/20350099/programmatically-change-the-tableview-row-appearance
        sessionTable.setRowFactory(new Callback<TableView<SessionTableRow>, TableRow<SessionTableRow>>() {

            @Override
            public TableRow<SessionTableRow> call(TableView<SessionTableRow> param) {
                var row = new TableRow<SessionTableRow>() {

                    @Override
                    protected void updateItem(SessionTableRow row, boolean empty) {
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
