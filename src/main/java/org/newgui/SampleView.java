package org.newgui;

import java.net.URL;
import java.util.ResourceBundle;

import org.newgui.model.PersonTableRow;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class SampleView implements FxmlView<SampleViewModel>, Initializable {

    @FXML
    private Label sampleLabel;

    @FXML
    private TableView<PersonTableRow> table;


    @InjectViewModel
    private SampleViewModel viewModel;

    // Called after the view is completely loaded
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sampleLabel.textProperty().bind(viewModel.sampleMessageProperty());
        
        table.setItems(viewModel.personsProperty());
        viewModel.selectedItemProperty().bind(table.getSelectionModel().selectedItemProperty());

        viewModel.addTableRow();
        viewModel.addTableRow();
        viewModel.addTableRow();
        viewModel.addTableRow();
    }
    
}
