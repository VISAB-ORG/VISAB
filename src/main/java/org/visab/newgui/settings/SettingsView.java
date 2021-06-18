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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;


/**
 * This class represents the view of the settings.
 * 
 * @author tim
 *
 */
public class SettingsView implements FxmlView<SettingsViewModel>, Initializable {

    @FXML
    TextField webApiPortField;
    
//    @FXML
//    TextField sessionTimeoutField;
    @FXML
    TableView<SessionItem> sessionTimeoutsTable;
    
    @FXML
    TableColumn<SessionItem, String> gamesColumn;
    
    @FXML
    TableColumn<SessionItem, String> timeoutColumn;
    
    @FXML
    TextField allowedGamesField;
    
    @FXML
    Button saveButton;
    
    @FXML
    Button returnButton;
    
    @InjectViewModel
    SettingsViewModel viewModel;
    
    /**
     * Saves the settings and closes the settingsView.
     * 
     * @param event Is triggered when the save button is pressed.
     */
    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        viewModel.updateSettingsCommand().execute();
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Closes the settingsView.
     * 
     * @param event Is triggered when the return button is pressed.
     */
    @FXML
    private void handleReturnButtonAction(ActionEvent event) {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
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
        allowedGamesField.textProperty().bindBidirectional(viewModel.allowedGamesProperty());

        gamesColumn.setCellValueFactory(cellData -> cellData.getValue().getGame());
        timeoutColumn.setCellValueFactory(c -> c.getValue().getTimeout());
        timeoutColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        
        
        sessionTimeoutsTable.setItems(viewModel.settingsItemProperty());

        // sets the inputField as numericalField
        setInputFieldNumericOnly(webApiPortField);
//        setInputFieldNumericOnly(sessionTimeoutField);
    }

}
