package org.visab.newgui.settings;

import java.net.URL;
import java.util.ResourceBundle;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * This class represents the view of the settings.
 * 
 * @author tim
 *
 */
public class SettingsView implements FxmlView<SettingsViewModel>, Initializable {

    @FXML
    TextField webApiPortField;
    
    @FXML
    TextField webApiHostNameField;
    
    @FXML
    TextField sessionTimeoutField;
    
    @FXML
    TextField allowedGamesField;
    
    @InjectViewModel
    SettingsViewModel viewModel;
    
    /**
     * 
     * @param event Is triggered when the save button is pressed.
     */
    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        viewModel.updateSettings(webApiPortField.getText(), webApiHostNameField.getText(), 
                sessionTimeoutField.getText(), allowedGamesField.getText());
        // TODO: should the settings view be closed when hte new settings are saved?
    }
    
    /**
     * 
     * @param event Is triggered when the return button is pressed.
     */
    @FXML
    private void handleReturnButtonAction(ActionEvent event) {
        // TODO: check if return button is needed or otherwise implemented
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webApiPortField.setText(viewModel.webApiPortProperty());
        webApiHostNameField.setText(viewModel.webApiHostNameProperty());
        sessionTimeoutField.setText(viewModel.sessionTimeoutProperty());
        allowedGamesField.setText(viewModel.allowedGamesProperty());
    }

}
