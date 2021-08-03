package org.visab.newgui.visualize.cbrshooter.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.newgui.visualize.cbrshooter.model.PlayerDataRow;
import org.visab.newgui.visualize.cbrshooter.model.PlayerVisualsRow;
import org.visab.newgui.visualize.cbrshooter.viewmodel.CBRShooterReplayViewModel;
import org.visab.workspace.config.ConfigManager;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * View that is associated with the respective fxml as a controller to represent
 * its data in terms of the chosen MVVM GUI pattern.
 * 
 * @author leonr
 *
 */
public class CBRShooterReplayView implements FxmlView<CBRShooterReplayViewModel>, Initializable {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(CBRShooterReplayView.class);

    @FXML
    private ImageView healthImage;
    @FXML
    private ImageView ammuImage;
    @FXML
    private ImageView weaponImage;
    @FXML
    private CheckBox checkBoxWeapon;
    @FXML
    private CheckBox checkBoxAmmuItem;
    @FXML
    private CheckBox checkBoxHealthItem;

    // ----- CONTROLS ----
    @FXML
    private Slider frameSlider;
    @FXML
    private Slider veloSlider;
    @FXML
    private ToggleButton playPauseButton;

    @FXML
    private Pane drawPane;

    @FXML
    private Label totalTimeValueLabel;
    @FXML
    private Label roundValueLabel;
    @FXML
    private Label roundTimeValueLabel;
    @FXML
    private Label healthItemCoordsValueLabel;
    @FXML
    private Label weaponCoordsValueLabel;
    @FXML
    private Label ammuCoordsValueLabel;

    @FXML
    private TableView<PlayerDataRow> playerDataTable;

    @FXML
    private TableView<PlayerVisualsRow> playerVisualsTable;

    // Images / Icons
    private Image pauseImage = new Image(ConfigManager.IMAGE_PATH + "pause.png");
    private Image playImage = new Image(ConfigManager.IMAGE_PATH + "play.png");

    @FXML
    private ImageView weaponIcon;

    @FXML
    private ImageView healthIcon;

    @FXML
    private ImageView ammuIcon;

    private ImageView playImageView = new ImageView(playImage);
    private ImageView pauseImageView = new ImageView(pauseImage);

    @InjectViewModel
    CBRShooterReplayViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        playerDataTable.setItems(viewModel.getCurrentPlayerStats());
        playerVisualsTable.setItems(viewModel.getPlayerVisualsRows());

        weaponIcon.setImage(viewModel.getWeaponIcon());
        ammuIcon.setImage(viewModel.getAmmuIcon());
        healthIcon.setImage(viewModel.getHealthIcon());

        totalTimeValueLabel.textProperty().bindBidirectional(viewModel.getTotalTimeProperty());
        roundValueLabel.textProperty().bindBidirectional(viewModel.getRoundProperty());
        roundTimeValueLabel.textProperty().bindBidirectional(viewModel.getRoundTimeProperty());
        healthItemCoordsValueLabel.textProperty().bindBidirectional(viewModel.getHealthCoordsProperty());
        weaponCoordsValueLabel.textProperty().bindBidirectional(viewModel.getWeaponCoordsProperty());
        ammuCoordsValueLabel.textProperty().bindBidirectional(viewModel.getAmmuCoordsProperty());

        playPauseButton.setGraphic(playImageView);
        frameSlider.maxProperty().bindBidirectional(viewModel.getFrameSliderMaxProperty());
        frameSlider.valueProperty().bindBidirectional(viewModel.getFrameSliderValueProperty());
        frameSlider.majorTickUnitProperty().bindBidirectional(viewModel.getFrameSliderTickUnitProperty());

        drawPane.getChildren().setAll(viewModel.getMapElements());
    }

    @FXML
    public void handleFrameSlider() {
        viewModel.setSelectedFrame((int) frameSlider.getValue()).execute();
    }

    @FXML
    public void handleVeloSlider() {
        viewModel.setUpdateInterval(100 / veloSlider.getValue()).execute();
    }

    @FXML
    public void visualizeWeapon() {
        viewModel.visualizeMapElement("weapon", this.checkBoxWeapon.isSelected()).execute();
    }

    @FXML
    public void visualizeAmmuItem() {
        viewModel.visualizeMapElement("ammuItem", this.checkBoxAmmuItem.isSelected()).execute();
    }

    @FXML
    public void visualizeHealthItem() {
        viewModel.visualizeMapElement("healthItem", this.checkBoxHealthItem.isSelected()).execute();
    }

    @FXML
    public void handlePlayPause(ActionEvent event) {
        if (playPauseButton.isSelected()) {
            playPauseButton.setGraphic(pauseImageView);
            viewModel.playData().execute();
        } else {
            playPauseButton.setGraphic(playImageView);
            viewModel.pauseData().execute();
        }
    }
}
