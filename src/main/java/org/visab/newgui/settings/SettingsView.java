package org.visab.newgui.settings;

import java.net.URL;
import java.util.ResourceBundle;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SettingsView implements FxmlView<SettingsViewModel>, Initializable {

    @FXML
    TextField webApiPortField;
    
    @FXML
    TextField webApiHostNameField;
    
    @FXML
    TextField sessionTimeoutField;
    
    @FXML
    TextField allowedGamesField;
    
    @FXML
    ListView<String> testListView;
    
    @InjectViewModel
    SettingsViewModel viewModel;
    
    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        viewModel.updateSettings(webApiPortField.getText(), webApiHostNameField.getText(), 
                sessionTimeoutField.getText(), allowedGamesField.getText());
    }
    
    @FXML
    private void handleReturnButtonAction(ActionEvent event) {
        // TODO: check if return button is needed or otherwise implemented
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webApiPortField.setText(viewModel.webApiPortProperty());
        webApiHostNameField.setText(viewModel.webApiHostNameProperty());
        sessionTimeoutField.setText(viewModel.sessionTimeoutProperty());
        testListView.getItems().add(viewModel.allowedGamesProperty());
    }

}
