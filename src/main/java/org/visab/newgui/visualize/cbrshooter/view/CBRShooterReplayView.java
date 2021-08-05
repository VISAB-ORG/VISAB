package org.visab.newgui.visualize.cbrshooter.view;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.Vector2;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.ResourceHelper;
import org.visab.newgui.UiHelper;
import org.visab.newgui.visualize.cbrshooter.model.CoordinateHelper;
import org.visab.newgui.visualize.cbrshooter.model.Player;
import org.visab.newgui.visualize.cbrshooter.model.PlayerDataRow;
import org.visab.newgui.visualize.cbrshooter.model.PlayerVisualsRow;
import org.visab.newgui.visualize.cbrshooter.viewmodel.CBRShooterReplayViewModel;

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
import javafx.event.EventHandler;
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
import javafx.scene.shape.Path;

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

    private static final Vector2 STANDARD_ICON_VECTOR = new Vector2(16, 16);

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
    private Image pauseImage = new Image(ResourceHelper.IMAGE_PATH + "pause.png");
    private Image playImage = new Image(ResourceHelper.IMAGE_PATH + "play.png");

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

    EventHandler<ActionEvent> updateMapElementsHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            updateMapElements();
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        frameBasedStats.bind(viewModel.frameBasedStatsProperty());
        drawPane.setPrefWidth(550);
        drawPane.setPrefHeight(drawPane.getPrefWidth()
                * ((double) viewModel.getMapRectangle().getHeight() / (double) viewModel.getMapRectangle().getWidth()));
        coordinateHelper = new CoordinateHelper(viewModel.getMapRectangle(), drawPane.getPrefHeight(),
                drawPane.getPrefWidth(), STANDARD_ICON_VECTOR);
        initializePlayers();
        initializeMapElements();
        drawPane.getChildren().setAll(mapElements.values());

        playerDataTable.setItems(playerDataRows);
        playerVisualsTable.setItems(playerVisualsRows);

        weaponIcon.setImage(viewModel.getWeaponIcon());
        ammuIcon.setImage(viewModel.getAmmuIcon());
        healthIcon.setImage(viewModel.getHealthIcon());

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
                // While loops necessary to ensure increments / decrements of one?
                updatePlayerDataRows();
                updateMapElements();
                viewModel.updateCurrentGameStatsByFrame();
            }
        });
        // Update visibility of player icon
        checkBoxAmmuItem.setOnAction(updateMapElementsHandler);
        checkBoxHealthItem.setOnAction(updateMapElementsHandler);
        checkBoxWeapon.setOnAction(updateMapElementsHandler);
    }

    /**
     * This method simply updates the data table for each player based on the bound
     * frame-based statistics object.
     * 
     */
    private void updatePlayerDataRows() {
        playerDataRows.clear();
        for (org.visab.globalmodel.cbrshooter.Player player : frameBasedStats.get().getPlayers()) {
            playerDataRows.add(new PlayerDataRow(player));
        }
    }

    private void initializeMapElements() {
        ImageView mapImage = UiHelper.greyScaleImage(viewModel.getMapImage());
        mapImage.setViewOrder(1);

        mapElements.put("map", mapImage);

        ImageView ammuItem = new ImageView(viewModel.getAmmuIcon());
        ImageView weapon = new ImageView(viewModel.getWeaponIcon());
        ImageView healthItem = new ImageView(viewModel.getHealthIcon());
        UiHelper.adjustVisual(ammuItem, false, frameBasedStats.get().getAmmunitionPosition(), STANDARD_ICON_VECTOR);
        UiHelper.adjustVisual(weapon, false, frameBasedStats.get().getWeaponPosition(), STANDARD_ICON_VECTOR);
        UiHelper.adjustVisual(healthItem, false, frameBasedStats.get().getHealthPosition(), STANDARD_ICON_VECTOR);
        mapElements.put("ammuItem", ammuItem);
        mapElements.put("weapon", weapon);
        mapElements.put("healthItem", healthItem);

        for (Player player : players.values()) {
            ImageView playerIcon = new ImageView(player.getPlayerIcon());
            ImageView playerPlanChange = new ImageView(player.getPlayerPlanChange());
            ImageView playerDeath = new ImageView(player.getPlayerDeath());
            Path playerPath = player.getPlayerPath();
            UiHelper.adjustVisual(playerIcon, true,
                    coordinateHelper.translateAccordingToMap(player.positionProperty().get(), true),
                    STANDARD_ICON_VECTOR);
            UiHelper.adjustVisual(playerPlanChange, false, 0, 0);
            UiHelper.adjustVisual(playerDeath, false, 0, 0);
            mapElements.put(player.getName() + "_playerIcon", playerIcon);
            mapElements.put(player.getName() + "_playerPlanChange", playerPlanChange);
            mapElements.put(player.getName() + "_playerDeath", playerDeath);
            mapElements.put(player.getName() + "_playerPath", playerPath);
        }
        drawPane.getChildren().setAll(mapElements.values());
    }

    private void initializePlayers() {
        for (String playerName : viewModel.getPlayerNames()) {
            HashMap<String, Image> iconMap = viewModel.getIconsForPlayer(playerName);
            Player player = new Player(playerName, viewModel.getPlayerColors().get(playerName),
                    iconMap.get("playerIcon"), iconMap.get("playerPlanChange"), iconMap.get("playerDeath"), new Path());
            playerVisualsRows.add(new PlayerVisualsRow(playerName,
                    UiHelper.resizeImage(new ImageView(player.getPlayerIcon()), STANDARD_ICON_VECTOR),
                    new ImageView(player.getPlayerPlanChange()), new ImageView(player.getPlayerDeath()),
                    player.playerColorProperty().get()));
            player.updatePlayerData(frameBasedStats.get().getInfoByPlayerName(playerName), coordinateHelper);
            players.put(playerName, player);

            updatePlayerDataRows();
        }
    }

    private void updateMapElements() {
        // Non-player-related map items
        ImageView ammuItem = (ImageView) mapElements.get("ammuItem");
        var newAmmuPos = frameBasedStats.get().getAmmunitionPosition();
        if (checkBoxAmmuItem.isSelected() && !newAmmuPos.isZero()) {
            UiHelper.adjustVisual(ammuItem, true, coordinateHelper.translateAccordingToMap(newAmmuPos, true));
        } else {
            ammuItem.setVisible(false);
        }

        ImageView weapon = (ImageView) mapElements.get("weapon");
        var newWeaponPos = frameBasedStats.get().getWeaponPosition();
        if (checkBoxWeapon.isSelected() && !newWeaponPos.isZero()) {
            UiHelper.adjustVisual(weapon, true, coordinateHelper.translateAccordingToMap(newWeaponPos, true));
        } else {
            weapon.setVisible(false);
        }

        ImageView healthItem = (ImageView) mapElements.get("healthItem");
        var newHealthPos = frameBasedStats.get().getHealthPosition();
        if (checkBoxHealthItem.isSelected() && !newHealthPos.isZero()) {
            UiHelper.adjustVisual(healthItem, true, coordinateHelper.translateAccordingToMap(newHealthPos, true));
        } else {
            healthItem.setVisible(false);
        }

        // Iterate over players
        for (Player player : players.values()) {
            ImageView playerIcon = (ImageView) mapElements.get(player.getName() + "_playerIcon");
            Vector2 newPos = coordinateHelper.translateAccordingToMap(
                    frameBasedStats.get().getInfoByPlayerName(player.getName()).getPosition(), true);
            UiHelper.adjustVisual(playerIcon, player.showIconProperty().get(), newPos);
            player.updatePlayerData(frameBasedStats.get().getInfoByPlayerName(player.getName()), coordinateHelper);
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
