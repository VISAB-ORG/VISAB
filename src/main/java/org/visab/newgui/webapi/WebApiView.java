package org.visab.newgui.webapi;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.statistics.cbrshooter.CBRShooterStatisticsView;
import org.visab.newgui.webapi.model.SessionTableRow;
import org.visab.processing.ILiveViewable;
import org.visab.processing.SessionListenerAdministration;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

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
            // TODO: Get these views dynamically via DynamicViewLoad or sth
            var viewTupel = FluentViewLoader.fxmlView(CBRShooterStatisticsView.class).load();
            var root = viewTupel.getView();

            var vM = viewTupel.getViewModel();
            if (vM.supportsLiveViewing()) {
                var listener = SessionListenerAdministration.getSessionListener(selectedRow.getSessionId());

                if (listener instanceof ILiveViewable<?>) {
                    vM.initiateLiveView((ILiveViewable<CBRShooterStatistics>) listener);
                }
            }
            var stage = new Stage();
            stage.setTitle("Statistics Window test");
            stage.setScene(new Scene(root));
            stage.show();
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

        // When the selectedTableRowProperty changes in the viewModel we need to update
        // the table
        // viewModel.setOnSelect(vm -> contactTable.getSelectionModel().select(vm));
    }
}
