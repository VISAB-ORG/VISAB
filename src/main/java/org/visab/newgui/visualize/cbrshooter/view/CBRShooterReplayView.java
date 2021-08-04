package org.visab.newgui.visualize.cbrshooter.view;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.DoubleVector2;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.globalmodel.cbrshooter.PlayerInformation;
import org.visab.newgui.UiHelper;
import org.visab.newgui.visualize.cbrshooter.model.CoordinateHelper;
import org.visab.newgui.visualize.cbrshooter.model.Player;
import org.visab.newgui.visualize.cbrshooter.model.PlayerDataRow;
import org.visab.newgui.visualize.cbrshooter.model.PlayerVisualsRow;
import org.visab.newgui.visualize.cbrshooter.viewmodel.CBRShooterReplayViewModel;
import org.visab.workspace.config.ConfigManager;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

    private static final DoubleVector2 STANDARD_ICON_VECTOR = new DoubleVector2(16, 16);

    private CoordinateHelper coordinateHelper;

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

    private ObservableList<PlayerVisualsRow> playerVisualsRows = FXCollections.observableArrayList();
    private ObservableList<PlayerDataRow> playerDataRows = FXCollections.observableArrayList();
    private ObservableMap<String, Node> mapElements = FXCollections.observableHashMap();
    private ObservableMap<String, Player> players = FXCollections.observableHashMap();

    private ObjectProperty<CBRShooterStatistics> frameBasedStats = new SimpleObjectProperty<>();

    @InjectViewModel
    CBRShooterReplayViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializePlayers();

        frameBasedStats.bind(viewModel.frameBasedStatsProperty());

        coordinateHelper = new CoordinateHelper(viewModel.getMapRectangle(), drawPane.getHeight(), drawPane.getWidth(),
                new DoubleVector2(drawPane.getLayoutX(), drawPane.getLayoutY()));

        playerDataTable.setItems(playerDataRows);
        playerVisualsTable.setItems(playerVisualsRows);

        weaponIcon.setImage(viewModel.getWeaponIcon());
        ammuIcon.setImage(viewModel.getAmmuIcon());
        healthIcon.setImage(viewModel.getHealthIcon());

        drawPane.getChildren().add(UiHelper.greyScaleImage(viewModel.getMapImage()));
        drawPane.getChildren().addAll(mapElements.values());

        totalTimeValueLabel.textProperty().bind(viewModel.totalTimeProperty());
        roundValueLabel.textProperty().bind(viewModel.roundProperty().asString());
        roundTimeValueLabel.textProperty().bind(viewModel.roundTimeProperty());
        healthItemCoordsValueLabel.textProperty().bind(viewModel.healthCoordsProperty().asString());
        weaponCoordsValueLabel.textProperty().bind(viewModel.weaponCoordsProperty().asString());
        ammuCoordsValueLabel.textProperty().bind(viewModel.ammuCoordsProperty().asString());

        playPauseButton.setGraphic(playImageView);
        veloSlider.valueProperty().bindBidirectional(viewModel.velocityProperty());
        frameSlider.maxProperty().bind(viewModel.frameSliderMaxProperty());
        frameSlider.valueProperty().bindBidirectional(viewModel.playFrameProperty());
        frameSlider.majorTickUnitProperty().bind(viewModel.frameSliderTickUnitProperty());
        frameSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Make sure the selectedFrame cannot be out of bounds
                // While loops necessary to ensure increments / decrements of one
                updatePlayerDataRows();
            }
        });
    }

    /**
     * This method simply updates the data table for each player based on the bound
     * frame-based statistics object.
     * 
     */
    private void updatePlayerDataRows() {
        playerDataRows.clear();
        for (PlayerInformation playerInfo : frameBasedStats.get().getPlayers()) {
            playerDataRows.add(new PlayerDataRow(playerInfo));
        }
    }

//    private void initializeReplayView() {
//        ImageView ammuItem = new ImageView(viewModel.getAmmuIcon());
//        ImageView weapon = new ImageView(viewModel.getWeaponIcon());
//        ImageView healthItem = new ImageView(viewModel.getHealthIcon());
//        UiHelper.adjustVisual(ammuItem, false, drawPane.getLayoutX(), drawPane.getLayoutY());
//        UiHelper.adjustVisual(weapon, false, drawPane.getLayoutX(), drawPane.getLayoutY());
//        UiHelper.adjustVisual(healthItem, false, drawPane.getLayoutX(), drawPane.getLayoutY());
//        mapElements.put("ammuItem", ammuItem);
//        mapElements.put("weapon", weapon);
//        mapElements.put("healthItem", healthItem);
//
//        for (String playerName : viewModel.getPlayerNames()) {
//            ImageView playerIcon = new ImageView(UiHelper.recolorImage(viewModel.getPlayerIconByName(playerName),
//                    players.get(playerName).playerColorProperty().get()));
//            ImageView playerPlanChange = new ImageView(viewModel.getWeaponIcon());
//            ImageView playerDeath = new ImageView(viewModel.getHealthIcon());
//        }
//    }

    private void initializePlayers() {
        for (String playerName : viewModel.getPlayerNames()) {
            Player player = new Player(playerName, viewModel.getPlayerColors().get(playerName));
            HashMap<String, Image> iconMap = viewModel.getIconsForPlayer(playerName);
            playerVisualsRows.add(new PlayerVisualsRow(playerName,
                    UiHelper.resizeImage(new ImageView(iconMap.get("playerIcon")), STANDARD_ICON_VECTOR),
                    new ImageView(iconMap.get("playerPlanChange")), new ImageView(iconMap.get("playerDeath")),
                    player.playerColorProperty().get()));
            players.put(playerName, player);
        }
    }

    /**
     * This method initializes as hash map that can be globally used across the view
     * model which provides different categories of visuals for a dynamic amount of
     * players in general.
     * 
     */
    private void initializePlayerVisuals() {
        // TODO: do stuff
    }

    /**
     * Initializes all map elements once the replay view is loaded.
     */
    private void initializeMapElements() {
        // TODO: do stuff
    }

    /**
     * Method is used to redraw the paths accordingly if the frame slider is moved
     * backwards.
     * 
     */
    private void redrawPathsForRound() {
        // TODO: do stuff
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
