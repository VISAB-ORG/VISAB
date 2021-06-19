package org.visab.newgui.settings.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.settings.viewmodel.SessionTimeoutEditViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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
     * 
     * @param event Is triggered when the save button is pressed.
     */
    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        viewModel.updateSessionTimeoutCommand().execute();
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
        
        //TODO: Probably not working since view is not dynamically loaded 
        selectedGameChoiceBox.setValue("Test");
        selectedGameChoiceBox.setItems(viewModel.allowedGamesProperty());
        
        System.out.println(viewModel.allowedGamesProperty());
        
        
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
