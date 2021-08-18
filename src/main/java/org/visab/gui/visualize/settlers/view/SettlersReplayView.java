package org.visab.gui.visualize.settlers.view;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.visab.globalmodel.Vector2;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.gui.ResourceHelper;
import org.visab.gui.UiHelper;
import org.visab.gui.visualize.CoordinateHelper;
import org.visab.gui.visualize.settlers.model.Player;
import org.visab.gui.visualize.settlers.model.PlayerVisualsRow;
import org.visab.gui.visualize.settlers.viewmodel.SettlersReplayViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class SettlersReplayView implements FxmlView<SettlersReplayViewModel>, Initializable {

    @FXML
    private Slider turnSlider;
    @FXML
    private Slider veloSlider;

    @FXML
    private Pane drawPane;
    @FXML
    private CheckBox showInBlackAndWhiteCheckBox;

    @FXML
    private ToggleButton playPauseButton;

    @FXML
    private Label turnValueLabel;

    @FXML
    private Label turnTimeStampValueLabel;

    @FXML
    private Label diceRollsValueLabel;

    @FXML
    private TableView<org.visab.gui.visualize.settlers.model.Player> playerDataTable;
    @FXML
    private TableView<PlayerVisualsRow> playerVisualsTable;

    private static final double DRAW_PANE_WIDTH = 550.0;

    private static final Vector2<Double> STANDARD_ICON_VECTOR = new Vector2<Double>(24.0, 24.0);
    private static final Vector2<Double> SMALL_ICON_VECTOR = new Vector2<Double>(16.0, 16.0);
    private static final Vector2<Double> OFFSET_VECTOR = new Vector2<Double>(0.0, 40.0);

    private CoordinateHelper coordinateHelper;

    private Image pauseImage = new Image(ResourceHelper.IMAGE_PATH + "pause.png");
    private Image playImage = new Image(ResourceHelper.IMAGE_PATH + "play.png");

    private ImageView playImageView = UiHelper.resizeImage(new ImageView(playImage), new Vector2<Double>(32.0, 32.0));
    private ImageView pauseImageView = UiHelper.resizeImage(new ImageView(pauseImage), new Vector2<Double>(32.0, 32.0));

    private ObjectProperty<SettlersStatistics> turnBasedStats = new SimpleObjectProperty<>();

    private ObservableList<PlayerVisualsRow> playerVisualsRows = FXCollections.observableArrayList();
    private ObservableMap<String, Node> mapElements = FXCollections.observableHashMap();

    // Players whose values will always be up to date with the current turn
    private List<Player> players;

    @InjectViewModel
    SettlersReplayViewModel viewModel;

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
        turnBasedStats.bind(viewModel.turnBasedStatsProperty());

        drawPane.setMinWidth(DRAW_PANE_WIDTH);
        drawPane.setMaxWidth(DRAW_PANE_WIDTH);
        var drawPaneFitHeight = DRAW_PANE_WIDTH
                * ((double) viewModel.getMapRectangle().getHeight() / (double) viewModel.getMapRectangle().getWidth());
        drawPane.setMinHeight(drawPaneFitHeight);
        drawPane.setMaxHeight(drawPaneFitHeight);
        coordinateHelper = new CoordinateHelper(viewModel.getMapRectangle(), drawPane.getMinHeight(),
                drawPane.getMinWidth(), STANDARD_ICON_VECTOR, OFFSET_VECTOR);
        initializePlayersVisuals();

        playerDataTable.setItems(FXCollections.observableArrayList(players));
        playerVisualsTable.setItems(playerVisualsRows);

        turnValueLabel.textProperty().bind(viewModel.turnProperty().asString());
        turnTimeStampValueLabel.textProperty().bind(viewModel.turnTimeStampProperty());
        diceRollsValueLabel.textProperty().bind(viewModel.diceNumberRolledProperty().asString());

        playPauseButton.setGraphic(playImageView);
        veloSlider.valueProperty().bindBidirectional(viewModel.velocityProperty());
        turnSlider.maxProperty().bind(viewModel.turnSliderMaxProperty());
        turnSlider.valueProperty().bindBidirectional(viewModel.currentturnProperty());
        turnSlider.majorTickUnitProperty().bind(viewModel.turnSliderTickUnitProperty());
        turnSlider.setBlockIncrement(1);
        turnSlider.setSnapToTicks(false);

        initializeMapVisuals();

        viewModel.subscribe("DATA_UPDATED", this::onDataUpdated);
    }

    private void onDataUpdated(String message, Object[] payloadObjects) {
        updateMapElements();
    }

    /**
     * This method initializes all the underlying player-specific information which
     * is used for proper handling across the replay view.
     */
    private void initializePlayersVisuals() {
        for (Player player : viewModel.getPlayers()) {
            var playerName = player.getName();
            HashMap<String, Pair<Image, String>> iconMap = viewModel.getAnnotatedIconsForPlayer(playerName);
            player.initializeVisuals(viewModel.getPlayerColors().get(playerName), iconMap);
            PlayerVisualsRow row = new PlayerVisualsRow(player.getName(),
                    UiHelper.resizeImage(new ImageView(player.getPlayerRoad()), SMALL_ICON_VECTOR),
                    player.getRoadAnnotation(),
                    UiHelper.resizeImage(new ImageView(player.getPlayerVillage()), SMALL_ICON_VECTOR),
                    player.getVillageAnnotation(),
                    UiHelper.resizeImage(new ImageView(player.getPlayerCity()), SMALL_ICON_VECTOR),
                    player.getCityAnnotation(), player.playerColorProperty().get());
            initializeEventListenersForRow(row, player);
            playerVisualsRows.add(row);
        }
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
            player.showRoadProperty().set(value);
            player.showVillagesProperty().set(value);
            player.showCitiesProperty().set(value);

            row.getShowPlayerRoadCheckBox().setSelected(value);
            row.getShowPlayerVillagesCheckBox().setSelected(value);
            row.getShowPlayerCitiesCheckBox().setSelected(value);
            updateMapElements();
        });

        row.getShowPlayerRoadCheckBox().setOnAction(event -> {
            var value = ((CheckBox) event.getSource()).isSelected();
            player.showRoadProperty().set(value);
            updateMapElements();
        });

        row.getShowPlayerVillagesCheckBox().setOnAction(event -> {
            var value = ((CheckBox) event.getSource()).isSelected();
            player.showVillagesProperty().set(value);
            updateMapElements();
        });

        row.getShowPlayerCitiesCheckBox().setOnAction(event -> {
            var value = ((CheckBox) event.getSource()).isSelected();
            player.showCitiesProperty().set(value);
            updateMapElements();
        });
    }

    /**
     * This method updates all visuals on the map accordingly. All initialized
     * visuals of the underlying HashMap as well as for static objects and players
     * are directly accessed and adjusted as necessary.
     */
    private void updateMapElements() {
        // Only decides the visibility for each players items
        for (Player player : players) {
            var playerName = player.getName();
            for (String key : mapElements.keySet()) {
                if (key.contains(playerName + "_street_")) {
                    var roadIndex = Integer.parseInt(key.substring(key.length() - 1));
                    if (player.showRoadProperty().get() == false) {
                        mapElements.get(key).setVisible(false);
                    } else {
                        if (roadIndex > player.streetCountProperty().get() - 1) {
                            mapElements.get(key).setVisible(false);
                        } else {
                            mapElements.get(key).setVisible(true);
                        }
                    }
                } else if (key.contains(playerName + "_village_final_")) {
                    var villageIndex = Integer.parseInt(key.substring(key.length() - 1));
                    if (player.showVillagesProperty().get() == false) {
                        mapElements.get(key).setVisible(false);
                    } else {
                        if (villageIndex > player.villageCountProperty().get() - 1) {
                            mapElements.get(key).setVisible(false);
                        } else {
                            mapElements.get(key).setVisible(true);
                        }
                    }
                } else if (key.contains(playerName + "_city_")) {
                    var cityIndex = Integer.parseInt(key.substring(key.length() - 1));
                    if (player.showCitiesProperty().get() == false) {
                        mapElements.get(key).setVisible(false);
                    } else {
                        if (cityIndex > player.cityCountProperty().get() - 1) {
                            mapElements.get(key).setVisible(false);
                        } else {
                            mapElements.get(key).setVisible(true);
                        }
                    }
                } else if (key.contains(playerName + "_village_pre_")) {
                    // Indexes for cities and pre-villages are identical
                    var preVillageIndex = Integer.parseInt(key.substring(key.length() - 1));

                    // If the associated city is already visible, the pre-village shall be hidden
                    if (mapElements.get(playerName + "_city_" + preVillageIndex).isVisible()) {
                        mapElements.get(key).setVisible(false);
                    } else {
                        mapElements.get(key).setVisible(true);
                    }
                }
            }
        }

        drawPane.getChildren().setAll(mapElements.values());
    }

    /**
     * This method initializes all map visuals on the basis of the end of the game
     * to optimize performance whilst updating the map. Replacing the items in a
     * frame based manner appeared to be too costly at runtime.
     */
    private void initializeMapVisuals() {
        // Have the map in grey scale as well as in colored
        ImageView mapImageBlackAndWhite = UiHelper.greyScaleImage(viewModel.getMapImage(), -0.3);
        mapImageBlackAndWhite.setFitWidth(DRAW_PANE_WIDTH);
        mapImageBlackAndWhite.setFitHeight(DRAW_PANE_WIDTH
                * ((double) viewModel.getMapRectangle().getHeight() / (double) viewModel.getMapRectangle().getWidth()));
        mapImageBlackAndWhite.setViewOrder(1);
        mapImageBlackAndWhite.setVisible(false);
        mapElements.put("blackAndWhiteMap", mapImageBlackAndWhite);

        ImageView mapImageColored = new ImageView(viewModel.getMapImage());
        mapImageColored.setFitWidth(DRAW_PANE_WIDTH);
        mapImageColored.setFitHeight(DRAW_PANE_WIDTH
                * ((double) viewModel.getMapRectangle().getHeight() / (double) viewModel.getMapRectangle().getWidth()));
        mapImageColored.setViewOrder(1);
        mapImageColored.setVisible(true);
        mapElements.put("coloredMap", mapImageColored);

        // Initialize streets, (pre-) villages and cities for each player
        for (Player player : viewModel.getPlayersOfLastTurn().values()) {
            var playerName = player.getName();

            for (int i = 0; i < player.streetPositionsProperty().get().size(); i++) {

                ImageView playerStreet = new ImageView(
                        UiHelper.recolorImage(player.getPlayerRoad(), player.playerColorProperty().get()));
                Label streetAnnotation = new Label(player.getRoadAnnotation());
                streetAnnotation.setTextFill(Color.WHITE);
                streetAnnotation.getStyleClass().add("boldLabel");

                StackPane annotatedStreet = new StackPane();
                annotatedStreet.getChildren().addAll(playerStreet, streetAnnotation);
                annotatedStreet.setLayoutX(coordinateHelper
                        .translateAccordingToMap(player.streetPositionsProperty().get().get(i), true).getX());
                annotatedStreet.setLayoutY(coordinateHelper
                        .translateAccordingToMap(player.streetPositionsProperty().get().get(i), true).getY());

                mapElements.put(playerName + "_street_" + i, annotatedStreet);
            }

            for (int i = 0; i < player.villagePositionsProperty().get().size(); i++) {

                ImageView playerVillage = new ImageView(
                        UiHelper.recolorImage(player.getPlayerVillage(), player.playerColorProperty().get()));
                Label villageAnnotation = new Label(player.getVillageAnnotation());
                villageAnnotation.setTextFill(Color.WHITE);
                villageAnnotation.getStyleClass().add("boldLabel");

                StackPane annotatedVillage = new StackPane();
                annotatedVillage.getChildren().addAll(playerVillage, villageAnnotation);
                annotatedVillage.setLayoutX(coordinateHelper
                        .translateAccordingToMap(player.villagePositionsProperty().get().get(i), true).getX());
                annotatedVillage.setLayoutY(coordinateHelper
                        .translateAccordingToMap(player.villagePositionsProperty().get().get(i), true).getY());

                mapElements.put(playerName + "_village_final_" + i, annotatedVillage);

            }

            // For each city, a "pre-village" also needs to be put on the map
            for (int i = 0; i < player.cityPositionsProperty().get().size(); i++) {

                if (mapElements.get(playerName + "_city_" + i) == null) {
                    // Cities
                    ImageView playerCity = new ImageView(
                            UiHelper.recolorImage(player.getPlayerCity(), player.playerColorProperty().get()));
                    Label cityAnnotation = new Label(player.getCityAnnotation());
                    cityAnnotation.setTextFill(Color.WHITE);
                    cityAnnotation.getStyleClass().add("boldLabel");

                    StackPane annotatedCity = new StackPane();
                    annotatedCity.getChildren().addAll(playerCity, cityAnnotation);
                    annotatedCity.setLayoutX(coordinateHelper
                            .translateAccordingToMap(player.cityPositionsProperty().get().get(i), true).getX());
                    annotatedCity.setLayoutY(coordinateHelper
                            .translateAccordingToMap(player.cityPositionsProperty().get().get(i), true).getY());

                    mapElements.put(playerName + "_city_" + i, annotatedCity);

                    // "Pre-" Villages
                    ImageView playerPreVillage = new ImageView(
                            UiHelper.recolorImage(player.getPlayerVillage(), player.playerColorProperty().get()));
                    Label preVillageAnnotation = new Label(player.getVillageAnnotation());
                    preVillageAnnotation.setTextFill(Color.WHITE);
                    preVillageAnnotation.getStyleClass().add("boldLabel");

                    StackPane annotatedPreVillage = new StackPane();
                    annotatedPreVillage.getChildren().addAll(playerPreVillage, preVillageAnnotation);
                    annotatedPreVillage.setLayoutX(coordinateHelper
                            .translateAccordingToMap(player.cityPositionsProperty().get().get(i), true).getX());
                    annotatedPreVillage.setLayoutY(coordinateHelper
                            .translateAccordingToMap(player.cityPositionsProperty().get().get(i), true).getY());

                    mapElements.put(playerName + "_village_pre_" + i, annotatedPreVillage);
                }
            }

        }

        drawPane.getChildren().setAll(mapElements.values());
        updateMapElements();
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
