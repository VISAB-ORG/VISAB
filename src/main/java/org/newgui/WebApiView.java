package org.newgui;

import java.net.URL;
import java.util.ResourceBundle;

import org.newgui.model.SessionTableRow;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class WebApiView implements FxmlView<WebApiViewModel>, Initializable {

    @FXML
    private TableView<SessionTableRow> sessionTable;

    @FXML
    private Button closeSessionButton;

    private Command closeSessionCommand;

    @InjectViewModel
    private WebApiViewModel viewModel;

    // Called after the view is completely loaded
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        closeSessionCommand = viewModel.getCloseSessionCommand();

        // TODO: Understand this binding logic
        closeSessionButton.disableProperty().bind(closeSessionCommand.notExecutableProperty());

        sessionTable.setItems(viewModel.getSessions());
		viewModel.selectedSessionRowProperty().bind(sessionTable.getSelectionModel().selectedItemProperty());

		// When the selectedTableRowProperty changes in the viewModel we need to update the table
		// viewModel.setOnSelect(vm -> contactTable.getSelectionModel().select(vm));
    }

    // TODO: Why is this needed? Can't we just bind directly and thats it?
    @FXML
	public void closeSessionAction() {
		closeSessionCommand.execute();
	}
    
}
