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


    @FXML
    private TableColumn<SessionTableRow, String> sessionId;

    @InjectViewModel
    private WebApiViewModel viewModel;

    // Called after the view is completely loaded
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sessionId.setCellValueFactory(cellData -> cellData.getValue().);
        var idCol = new TableColumn<>("")

        sampleLabel.textProperty().bind(viewModel.sampleMessageProperty());
        
        sessionTable.setItems(viewModel.sessionsProperty());
        // viewModel.selectedItemProperty().bind(sessionTable.getSelectionModel().selectedItemProperty());
    }
    
}
