package org.visab.newgui.settings;

import java.net.URL;
import java.util.ResourceBundle;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
        viewModel.updateSettingsCommand().execute();
        // TODO: should the settings view be closed when the new settings are saved?
    }
    
    /**
     * 
     * @param event Is triggered when the return button is pressed.
     */
    @FXML
    private void handleReturnButtonAction(ActionEvent event) {
        // TODO: check if return button is needed or otherwise implemented
    }
    
    /**
     * Makes a inputField to a numericalField.
     * @param inputField The inputField that should be a numericalField.
     */
    private void setInputFieldNumericOnly(TextField inputField) {
        // force the field to be numeric only
        inputField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    inputField.setText(newValue.replaceAll("[\\D]", ""));
                }
            }
        });
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webApiPortField.textProperty().bindBidirectional(viewModel.webApiPortProperty());
        sessionTimeoutField.textProperty().bindBidirectional(viewModel.sessionTimeoutProperty());
        allowedGamesField.textProperty().bindBidirectional(viewModel.allowedGamesProperty());
        
        // sets the inputField as numericalField
        setInputFieldNumericOnly(webApiPortField);
        setInputFieldNumericOnly(sessionTimeoutField);
    }

}
