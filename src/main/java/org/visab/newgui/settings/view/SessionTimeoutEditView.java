package org.visab.newgui.settings.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.settings.viewmodel.SettingsViewModel;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

/**
 * This class represents the view of the sessionTimeoutEdit.
 * 
 * @author tim
 *
 */
public class SessionTimeoutEditView implements FxmlView<SettingsViewModel>, Initializable {

    @FXML
    ChoiceBox<String> selectedGameChoiceBox;

    @FXML
    TextField sessionTimeoutField;

    @FXML
    Button saveButton;

    @InjectViewModel
    SettingsViewModel viewModel;

    /**
     * Saves the sessionTimout and closes the sessonTimeoutEditView.
     */
    @FXML
    private void handleSaveButtonAction() {
        viewModel.updateSessionTimeoutCommand().execute();
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Changes the value of the choicebox to the new selected value.
     */
    @FXML
    private void handleChoiceBoxAction() {
        sessionTimeoutField.setText(String.valueOf(
                Workspace.getInstance().getConfigManager().getSessionTimeout().get(selectedGameChoiceBox.getValue())));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedGameChoiceBox.setItems(viewModel.allowedGames());
        selectedGameChoiceBox.valueProperty().bindBidirectional(viewModel.editTimeoutsSelectedGameProperty());
        sessionTimeoutField.textProperty().bindBidirectional(viewModel.editTimeoutsTimeoutProperty(),
                new NumberStringConverter());

        // force the field to be numeric only
        sessionTimeoutField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(() -> {
                    if (!newValue.matches("\\d*")) {
                        sessionTimeoutField.setText(newValue.replaceAll("[\\D]", ""));
                    }
                });
            }
        });

    }

}
