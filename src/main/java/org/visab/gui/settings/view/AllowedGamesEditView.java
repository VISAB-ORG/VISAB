package org.visab.gui.settings.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.gui.settings.viewmodel.SettingsViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This class represents the view of the allowedGamesEdit.
 * 
 * @author tim
 *
 */
public class AllowedGamesEditView implements FxmlView<SettingsViewModel>, Initializable{

    @FXML
    TextField allowedGameField;
    
    @FXML
    ChoiceBox<String> allowedGameChoiceBox;
    
    @FXML
    Button addGameButton;
    
    @FXML
    Button removeGameButton;
    
    @FXML
    Button returnButton;
    
    @InjectViewModel
    SettingsViewModel viewModel;
    
    /**
     * Adds the new game.
     */
    @FXML
    private void handleAddGameButtonAction() {
        viewModel.addAllowedGameCommand().execute();
    }
    
    /**
     * Removes the selected game.
     */
    @FXML
    private void handleRemoveGameButtonAction() {
        viewModel.removeAllowedGameCommand().execute();
    }
    
    /**
     * Closes the view.
     */
    @FXML
    private void handleReturnButtonAction() {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allowedGameField.textProperty().bindBidirectional(viewModel.editAllowedNewGameProperty());
        allowedGameChoiceBox.setItems(viewModel.allowedGames());
        allowedGameChoiceBox.valueProperty().bindBidirectional(viewModel.editAllowedSelectedGameProperty());
    }

}