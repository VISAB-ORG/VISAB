package org.visab.newgui.settings.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.settings.viewmodel.SessionTimeoutEditViewModel;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SessionTimeoutEditView implements FxmlView<SessionTimeoutEditViewModel>, Initializable{

    @FXML
    ChoiceBox<String> selectedGameChoiceBox;
    
    @FXML 
    TextField sessionTimeoutField;
    
    @FXML
    Button saveButton;
    
    @InjectViewModel
    SessionTimeoutEditViewModel viewModel;
    
    /**
     * Saves the sessionTimout and closes the sessonTimeoutEditView.
     */
    @FXML
    private void handleSaveButtonAction() {
        viewModel.selectedGame(selectedGameChoiceBox.getValue());
        viewModel.updateSessionTimeoutCommand().execute();
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Changes the value of the choicebox to the new selected value.
     */
    @FXML
    private void handleChoiceBoxAction() {
        sessionTimeoutField.setText(String.valueOf(Workspace.getInstance().getConfigManager().getSessionTimeout().get(selectedGameChoiceBox.getValue())));
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedGameChoiceBox.setItems(viewModel.allowedGamesProperty());
        selectedGameChoiceBox.setValue("CBRShooter");
        sessionTimeoutField.textProperty().bindBidirectional(viewModel.timeoutProperty(new SimpleStringProperty(String.valueOf(Workspace.getInstance().getConfigManager().getSessionTimeout().get(selectedGameChoiceBox.getValue())))));
        
        // force the field to be numeric only
        sessionTimeoutField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    sessionTimeoutField.setText(newValue.replaceAll("[\\D]", ""));
                }
            }
        });
        
    }

}
