package org.visab.newgui.visualize.cbrshooter.view;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.visab.globalmodel.Vector2;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.ResourceHelper;
import org.visab.newgui.UiHelper;
import org.visab.newgui.visualize.CoordinateHelper;
import org.visab.newgui.visualize.cbrshooter.model.DataUpdatedPayload;
import org.visab.newgui.visualize.cbrshooter.model.Player;
import org.visab.newgui.visualize.cbrshooter.model.PlayerVisualsRow;
import org.visab.newgui.visualize.cbrshooter.viewmodel.CBRShooterReplayViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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

    @FXML
    private Slider frameSlider;

    @FXML
    private Slider veloSlider;

    @FXML
    private ToggleButton playPauseButton;

    @FXML
    private Pane drawPane;

    @FXML
    private CheckBox showInBlackAndWhiteCheckBox;

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
    private TableView<org.visab.newgui.visualize.cbrshooter.model.Player> playerDataTable;

    @FXML
    private TableView<PlayerVisualsRow> playerVisualsTable;

    @FXML
    private ImageView weaponIcon;

    @FXML
    private ImageView healthIcon;

    @FXML
    private ImageView ammuIcon;

    private static final double DRAW_PANE_WIDTH = 550.0;

    private static final Vector2<Double> STANDARD_ICON_VECTOR = new Vector2<Double>(16.0, 16.0);

    private CoordinateHelper coordinateHelper;

    private Image pauseImage = new Image(ResourceHelper.IMAGE_PATH + "pause.png");
    private Image playImage = new Image(ResourceHelper.IMAGE_PATH + "play.png");

    private ImageView playImageView = UiHelper.resizeImage(new ImageView(playImage), new Vector2<Double>(32.0, 32.0));
    private ImageView pauseImageView = UiHelper.resizeImage(new ImageView(pauseImage), new Vector2<Double>(32.0, 32.0));

    private ObservableList<PlayerVisualsRow> playerVisualsRows = FXCollections.observableArrayList();
    private ObservableMap<String, Node> mapElements = FXCollections.observableHashMap();

    private ObjectProperty<CBRShooterStatistics> frameBasedStats = new SimpleObjectProperty<>();

    // Players whose values will always be up to date with the current frame
    private List<Player> players;

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
        showInBlackAndWhiteCheckBox.setSelected(false);
        showInBlackAndWhiteCheckBox.setOnAction(e -> {
            if (showInBlackAndWhiteCheckBox.isSelected()) {
                mapElements.get("coloredMap").setVisible(false);
                mapElements.get("blackAndWhiteMap").setVisible(true);
            } else {
                mapElements.get("coloredMap").setVisible(true);
                mapElements.get("blackAndWhiteMap").setVisible(false);
            }
        });
        players = viewModel.getPlayers();
        frameBasedStats.bind(viewModel.frameBasedStatsProperty());

        drawPane.setPrefWidth(DRAW_PANE_WIDTH);
        var drawPanePrefHeight = DRAW_PANE_WIDTH
                * ((double) viewModel.getMapRectangle().getHeight() / (double) viewModel.getMapRectangle().getWidth());
        drawPane.setPrefHeight(drawPanePrefHeight);
        coordinateHelper = new CoordinateHelper(viewModel.getMapRectangle(), drawPane.getPrefHeight(),
                drawPane.getPrefWidth(), STANDARD_ICON_VECTOR);
        initializePlayersVisuals();
        initializeMapElements();
        drawPane.getChildren().setAll(mapElements.values());

        playerDataTable.setItems(FXCollections.observableArrayList(players));
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
        frameSlider.valueProperty().bindBidirectional(viewModel.currentFrameProperty());
        frameSlider.majorTickUnitProperty().bind(viewModel.frameSliderTickUnitProperty());
        frameSlider.setBlockIncrement(1);
        frameSlider.setSnapToTicks(false);

        // Check boxes for static objects are always the same
        checkBoxAmmuItem.setOnAction(updateMapElementsHandler);
        checkBoxHealthItem.setOnAction(updateMapElementsHandler);
        checkBoxWeapon.setOnAction(updateMapElementsHandler);

        viewModel.subscribe("DATA_UPDATED", this::onDataUpdated);
    }

    private void onDataUpdated(String message, Object[] payloadObjects) {
        var payload = (DataUpdatedPayload) payloadObjects[0];

        var newRound = payload.getNewRound();
        var oldRound = payload.getOldRound();

        if (newRound != oldRound) {
            for (Player player : players)
                player.resetPath();
        }

        var oldFrame = payload.getOldFrame();
        var newFrame = payload.getNewFrame();
        if (newFrame < oldFrame) {
            while (newFrame < oldFrame) {
                var roundStartIndex = viewModel.getRoundStartIndex(newRound);
                for (Player player : players) {
                    var positionsForInterval = viewModel.getPlayerPositionsForInterval(player.getName(),
                            roundStartIndex, newFrame);
                    player.redrawPath(positionsForInterval, coordinateHelper);
                }
                updateMapElements();
                oldFrame--;
            }
        } else {
            while (newFrame > oldFrame) {
                updateMapElements();
                oldFrame++;
            }
        }

        updateMapElements();
    }

    /**
     * This method initializes all the underlying player-specific information which
     * is used for proper handling across the replay view.
     */
    private void initializePlayersVisuals() {
        for (Player player : viewModel.getPlayers()) {
            var playerName = player.getName();
            HashMap<String, Image> iconMap = viewModel.getIconsForPlayer(playerName);
            player.initializeVisuals(viewModel.getPlayerColors().get(playerName), iconMap.get("playerIcon"),
                    iconMap.get("playerPlanChange"), iconMap.get("playerDeath"), new Path());
            PlayerVisualsRow row = new PlayerVisualsRow(playerName,
                    UiHelper.resizeImage(new ImageView(player.getPlayerIcon()), STANDARD_ICON_VECTOR),
                    new ImageView(player.getPlayerPlanChange()), new ImageView(player.getPlayerDeath()),
                    player.playerColorProperty().get());
            initializeEventListenersForRow(row, player);
            playerVisualsRows.add(row);
            player.updatePlayerCoordinates(coordinateHelper);
        }
    }

    /**
     * This method initializes the map elements based on static content as well as
     * underlying player-specific visuals data.
     */
    private void initializeMapElements() {
        ImageView mapImageBlackAndWhite = UiHelper.greyScaleImage(viewModel.getMapImage(), 0.0);
        mapImageBlackAndWhite.setViewOrder(1);
        mapImageBlackAndWhite.setVisible(false);
        mapElements.put("blackAndWhiteMap", mapImageBlackAndWhite);

        ImageView mapImageColored = new ImageView(viewModel.getMapImage());
        mapImageColored.setViewOrder(1);
        mapImageColored.setVisible(true);
        mapElements.put("coloredMap", mapImageColored);

        ImageView ammuItem = new ImageView(viewModel.getAmmuIcon());
        ImageView weapon = new ImageView(viewModel.getWeaponIcon());
        ImageView healthItem = new ImageView(viewModel.getHealthIcon());

        UiHelper.adjustVisual(ammuItem, false, frameBasedStats.get().getAmmunitionPosition(), STANDARD_ICON_VECTOR);
        UiHelper.adjustVisual(weapon, false, frameBasedStats.get().getWeaponPosition(), STANDARD_ICON_VECTOR);
        UiHelper.adjustVisual(healthItem, false, frameBasedStats.get().getHealthPosition(), STANDARD_ICON_VECTOR);

        mapElements.put("ammuItem", ammuItem);
        mapElements.put("weapon", weapon);
        mapElements.put("healthItem", healthItem);

        for (Player player : players) {
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

    /**
     * This method initializes all event listeners that are necessary to show or
     * hide any player-specifc visuals.
     * 
     * @param row    the visuals row the event listeners shall be added to.
     * @param player the playerName used to set the property correctly.
     */
    private void initializeEventListenersForRow(PlayerVisualsRow row, Player player) {
        row.getShowPlayerCheckBox().setOnAction(event -> {
            var value = ((CheckBox) event.getSource()).isSelected();
            player.showIconProperty().set(value);
            player.showDeathProperty().set(value);
            player.showPlanChangeProperty().set(value);
            player.showPathProperty().set(value);

            row.getShowPlayerIconCheckBox().setSelected(value);
            row.getShowPlayerPlanChangeCheckBox().setSelected(value);
            row.getShowPlayerDeathCheckBox().setSelected(value);
            row.getShowPlayerPathCheckBox().setSelected(value);
            updateMapElements();
        });

        row.getShowPlayerIconCheckBox().setOnAction(event -> {
            var value = ((CheckBox) event.getSource()).isSelected();
            player.showIconProperty().set(value);
            updateMapElements();
        });

        row.getShowPlayerDeathCheckBox().setOnAction(event -> {
            var value = ((CheckBox) event.getSource()).isSelected();
            player.showDeathProperty().set(value);
            updateMapElements();
        });

        row.getShowPlayerPlanChangeCheckBox().setOnAction(event -> {
            var value = ((CheckBox) event.getSource()).isSelected();
            player.showPlanChangeProperty().set(value);
            updateMapElements();
        });

        row.getShowPlayerPathCheckBox().setOnAction(event -> {
            var value = ((CheckBox) event.getSource()).isSelected();
            player.showPathProperty().set(value);
            updateMapElements();
        });
    }

    /**
     * This method updates all visuals on the map accordingly. All initialized
     * visuals of the underlying HashMap as well as for static objects and players
     * are directly accessed and adjusted as necessary.
     */
    private void updateMapElements() {
        ImageView ammuItem = (ImageView) mapElements.get("ammuItem");
        var newAmmuPos = frameBasedStats.get().getAmmunitionPosition();
        if (checkBoxAmmuItem.isSelected() && !newAmmuPos.checkIfZero()) {
            UiHelper.adjustVisual(ammuItem, true, coordinateHelper.translateAccordingToMap(newAmmuPos, true));
        } else {
            ammuItem.setVisible(false);
        }

        ImageView weapon = (ImageView) mapElements.get("weapon");
        var newWeaponPos = frameBasedStats.get().getWeaponPosition();
        if (checkBoxWeapon.isSelected() && !newWeaponPos.checkIfZero()) {
            UiHelper.adjustVisual(weapon, true, coordinateHelper.translateAccordingToMap(newWeaponPos, true));
        } else {
            weapon.setVisible(false);
        }

        ImageView healthItem = (ImageView) mapElements.get("healthItem");
        var newHealthPos = frameBasedStats.get().getHealthPosition();
        if (checkBoxHealthItem.isSelected() && !newHealthPos.checkIfZero()) {
            UiHelper.adjustVisual(healthItem, true, coordinateHelper.translateAccordingToMap(newHealthPos, true));
        } else {
            healthItem.setVisible(false);
        }

        for (Player player : players) {
            // Update player object with information retrieved from the frame based stats
            player.updatePlayerCoordinates(coordinateHelper);

            ImageView playerIcon = (ImageView) mapElements.get(player.getName() + "_playerIcon");
            Vector2<Double> newPos = coordinateHelper.translateAccordingToMap(player.positionProperty().get(), true);
            ImageView playerPlanChange = (ImageView) mapElements.get(player.getName() + "_playerPlanChange");
            Vector2<Double> newPosPlanChange = viewModel.getLastPlanChangePositionForPlayer(player.getName(),
                    (int) frameSlider.getValue());
            ImageView playerDeath = (ImageView) mapElements.get(player.getName() + "_playerDeath");
            Vector2<Double> newPosDeath = viewModel.getLastDeathPositionForPlayer(player.getName(),
                    (int) frameSlider.getValue());

            UiHelper.adjustVisual(playerIcon, player.showIconProperty().get(), newPos);
            UiHelper.adjustVisual(playerPlanChange,
                    player.showPlanChangeProperty().get() && !newPosPlanChange.checkIfZero(),
                    coordinateHelper.translateAccordingToMap(newPosPlanChange, true));
            UiHelper.adjustVisual(playerDeath, player.showDeathProperty().get() && !newPosDeath.checkIfZero(),
                    coordinateHelper.translateAccordingToMap(newPosDeath, true));
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
