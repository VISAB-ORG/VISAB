package org.newgui;

import java.net.URL;
import java.util.ResourceBundle;

import org.newgui.model.SessionTableRow;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class WebApiView implements FxmlView<WebApiViewModel>, Initializable {

    @FXML
    private Label sampleLabel;

    @FXML
    private TableView<SessionTableRow> sessionTable;

    @InjectViewModel
    private WebApiViewModel viewModel;

    // Called after the view is completely loaded
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sessionTable.setItems(viewModel.getSessions());

		viewModel.selectedSessionRowProperty().bind(sessionTable.getSelectionModel().selectedItemProperty());

		// When the selectedTableRowProperty changes in the viewModel we need to update the table
		// viewModel.setOnSelect(vm -> contactTable.getSelectionModel().select(vm));


        /*
        sampleLabel.textProperty().bind(viewModel.sampleMessageProperty());
        
        sessionTable.setItems(viewModel.sessionsProperty());
        // viewModel.selectedItemProperty().bind(sessionTable.getSelectionModel().selectedItemProperty());
        */
    }
    
}
