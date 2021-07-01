package org.visab.newgui.settings.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.settings.SessionItem;
import org.visab.newgui.settings.viewmodel.SettingsViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;


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
    TableView<SessionItem> sessionTimeoutsTable;
    
    @FXML
    TableColumn<SessionItem, String> gamesColumn;
    
    @FXML
    TableColumn<SessionItem, String> timeoutColumn;
    
    @FXML
    ListView<String> allowedGamesList;
    
    @FXML
    Button saveButton;
    
    @FXML
    Button returnButton;
    
    @InjectViewModel
    SettingsViewModel viewModel;
    
    /**
     * Opens the SessionTimeoutEditView.
     */
    @FXML
    private void handleEditSessionTimeoutButtonAction() {
        viewModel.openSessionTimeoutEditViewCommand().execute();
    }
    
    @FXML
    private void handleEditAllowedGamesButtonAction() {
        viewModel.openAllowedGameEditViewCommand().execute();
    }
    
    /**
     * Saves the settings and closes the settingsView.
     */
    @FXML
    private void handleSaveButtonAction() {
        viewModel.updateSettingsCommand().execute();
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Closes the settingsView.
     */
    @FXML
    private void handleReturnButtonAction() {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Makes a inputField to a numericalField.
     * 
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

        sessionTimeoutsTable.setItems(viewModel.settingsItemProperty());
        
        allowedGamesList.getItems().addAll(viewModel.allowedGamesProperty());

        // sets the inputField as numericalField
        setInputFieldNumericOnly(webApiPortField);
    }
    
    private void initializeSessionTablePresentation() {
        sessionTimeoutsTable.setRowFactory(new Callback<TableView<SessionItem>, TableRow<SessionItem>>() {
            
            @Override
            public TableRow<SessionItem> call(TableView<SessionItem> param) {
                var row = new TableRow<SessionItem>() {
                    @Override
                    protected void updateItem(SessionItem row, boolean empty) {
                        if (empty || row == null) {
                            return;
                        }
                    }
                };
                return row;
            }
        });
    }

}
