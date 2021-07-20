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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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

    // ----- IMAGE VIEWS -------

    @FXML
    private ImageView healthImage;
    @FXML
    private ImageView ammuImage;
    @FXML
    private ImageView weaponImage;

    // ----- VISIBILITY CHECKS -----
    @FXML
    private CheckBox checkBoxScriptBotPath;
    @FXML
    private CheckBox checkBoxScriptBotDeaths;
    @FXML
    private CheckBox checkBoxScriptBotPlayer;
    @FXML
    private CheckBox checkBoxCBRBotPath;
    @FXML
    private CheckBox checkBoxCBRBotDeaths;
    @FXML
    private CheckBox checkBoxCBRBotPlayer;
    @FXML
    private CheckBox checkBoxWeapons;
    @FXML
    private CheckBox checkBoxAmmu;
    @FXML
    private CheckBox checkBoxHealth;

    // ----- CONTROLS ----
    @FXML
    private Slider frameSlider;
    @FXML
    private Slider veloSlider;
    @FXML
    private ToggleButton playPauseButton;

    // ----- PANES / BOXES -----
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Pane drawPane;
    @FXML
    private VBox vBoxView;
    @FXML
    private Pane panePlan;
    @FXML
    private HBox hBoxScriptPath;
    @FXML
    private HBox hBoxScriptDeaths;
    @FXML
    private HBox hBoxScriptPlayer;

    // ----- LABELS ------
    @FXML
    private Label frameLabel;
    @FXML
    private Label labelCurrentPlanCBR;
    @FXML
    private Label labelCurrentPlanScript;
    @FXML
    private Label veloLabel;

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

    // --- FXML IMAGES ----

    @FXML
    private TableView<PlayerDataRow> playerDataTable;

    @FXML
    private TableView<PlayerVisualsRow> playerVisualsTable;

    // Images / Icons
    private Image imageScriptBot = new Image(ConfigManager.IMAGE_PATH + "scriptBot.png");
    private Image deathImage = new Image(ConfigManager.IMAGE_PATH + "deathScript.png");
    private Image deathImageCBR = new Image(ConfigManager.IMAGE_PATH + "deadCBR.png");
    private Image imageCbrBot = new Image(ConfigManager.IMAGE_PATH + "cbrBot.png");
    private Image changePlanCBRImage = new Image(ConfigManager.IMAGE_PATH + "changePlan.png");
    private Image changePlanScriptImage = new Image(ConfigManager.IMAGE_PATH + "changePlan.png");
    private Image pauseImage = new Image(ConfigManager.IMAGE_PATH + "pause.png");
    private Image playImage = new Image(ConfigManager.IMAGE_PATH + "play.png");

    private ImageView cbrbotImageView = new ImageView(imageCbrBot);
    private ImageView deathImageView = new ImageView(deathImage);
    private ImageView deathImageViewCBR = new ImageView(deathImageCBR);
    private ImageView scriptbotImageView = new ImageView(imageScriptBot);
    private ImageView playImageView = new ImageView(playImage);
    private ImageView pauseImageView = new ImageView(pauseImage);

    // Helper variables
    public static int masterIndex;
    public static int sleepTimer;

    @InjectViewModel
    CBRShooterReplayViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        playerDataTable.setItems(viewModel.getCurrentPlayerStats());

        System.out.println("VIEW: Setting the visuals rows content to table");

        playerVisualsTable.setItems(viewModel.getPlayerVisualsRows());

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

        drawPane.getChildren().setAll(viewModel.getMapElements().values());
    }

    // Dummy Handle Method for frame slider
    @FXML
    public void handleFrameSlider() {
        viewModel.setSelectedFrame((int) frameSlider.getValue()).execute();
        playerDataTable.refresh();
    }

    public void handleVeloSlider() {
        viewModel.setUpdateInterval(1000 / veloSlider.getValue()).execute();
    }

    // Handle Method for user Selection regarding visability of the Script Bot
    // Player Icon
    @FXML
    public void handleCheckBoxScriptPlayer(ActionEvent event) {
        if (checkBoxScriptBotPlayer.isSelected()) {
            scriptbotImageView.setVisible(true);
        } else if (checkBoxScriptBotPlayer.isSelected() == false) {
            scriptbotImageView.setVisible(false);
        }
    }

    // Handle Method for user Selection regarding visability of the CBR Bot Player
    // Icon
    @FXML
    public void handleCheckBoxCBRPlayer(ActionEvent event) {
        if (checkBoxCBRBotPlayer.isSelected()) {
            cbrbotImageView.setVisible(true);
        } else if (checkBoxCBRBotPlayer.isSelected() == false) {
            cbrbotImageView.setVisible(false);
        }
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
