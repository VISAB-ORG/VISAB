package org.visab.newgui.visualize.settlers.view;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.visab.globalmodel.Vector2;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.newgui.ResourceHelper;
import org.visab.newgui.UiHelper;
import org.visab.newgui.visualize.cbrshooter.model.CoordinateHelper;
import org.visab.newgui.visualize.settlers.model.Player;
import org.visab.newgui.visualize.settlers.model.PlayerVisualsRow;
import org.visab.newgui.visualize.settlers.viewmodel.SettlersReplayViewModel;

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
    private TableView<org.visab.newgui.visualize.settlers.model.Player> playerDataTable;
    @FXML
    private TableView<PlayerVisualsRow> playerVisualsTable;

    private static final double DRAW_PANE_WIDTH = 550.0;

    private static final Vector2<Double> STANDARD_ICON_VECTOR = new Vector2<Double>(16.0, 16.0);
    private static final Vector2<Double> BIG_ICON_VECTOR = new Vector2<Double>(32.0, 32.0);

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

    private Map<String, Player> playersOfLastTurn;

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
        playersOfLastTurn = viewModel.getPlayersOfLastTurn();

        players = viewModel.getPlayers();
        turnBasedStats.bind(viewModel.turnBasedStatsProperty());

        drawPane.setPrefWidth(DRAW_PANE_WIDTH);
        var drawPanePrefHeight = DRAW_PANE_WIDTH
                * ((double) viewModel.getMapRectangle().getHeight() / (double) viewModel.getMapRectangle().getWidth());
        drawPane.setPrefHeight(drawPanePrefHeight);
        coordinateHelper = new CoordinateHelper(viewModel.getMapRectangle(), drawPane.getPrefHeight(),
                drawPane.getPrefWidth(), BIG_ICON_VECTOR);
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
                    UiHelper.resizeImage(new ImageView(player.getPlayerRoad()), STANDARD_ICON_VECTOR),
                    UiHelper.resizeImage(new ImageView(player.getPlayerVillage()), STANDARD_ICON_VECTOR),
                    UiHelper.resizeImage(new ImageView(player.getPlayerCity()), STANDARD_ICON_VECTOR),
                    player.playerColorProperty().get());
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
        // Additional check for village to city transition is needed
        for (Player player : players) {
            var playerName = player.getName();
            for (String key : mapElements.keySet()) {
                if (key.contains(playerName + "_street")) {
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
                } else if (key.contains(playerName + "_village")) {
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
                } else if (key.contains(playerName + "_city")) {
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
                }
            }
        }

        drawPane.getChildren().setAll(mapElements.values());
    }

    private void initializeMapVisuals() {
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
        mapImageColored.setVisible(true);
        mapElements.put("coloredMap", mapImageColored);

        for (Player player : viewModel.getPlayersOfLastTurn().values()) {
            var playerName = player.getName();

            for (int i = 0; i < player.streetPositionsProperty().get().size(); i++) {

                ImageView playerStreet = new ImageView(
                        UiHelper.recolorImage(player.getPlayerRoad(), player.playerColorProperty().get()));
                UiHelper.adjustVisual(playerStreet, player.showRoadProperty().get(),
                        coordinateHelper.translateAccordingToMap(player.streetPositionsProperty().get().get(i), true),
                        BIG_ICON_VECTOR);
                Label streetAnnotation = new Label(player.getRoadAnnotation());
                streetAnnotation.setTextFill(player.playerColorProperty().get());
                streetAnnotation.getStyleClass().add("boldLabel");
                UiHelper.adjustVisual(streetAnnotation, playerStreet.getX() + (STANDARD_ICON_VECTOR.getX()),
                        playerStreet.getY() - (STANDARD_ICON_VECTOR.getY() / 2));
                mapElements.put(playerName + "_street_" + i, playerStreet);
                mapElements.put(playerName + "_streetAnnotation_" + i, streetAnnotation);

            }

            for (int i = 0; i < player.villagePositionsProperty().get().size(); i++) {

                ImageView playerVillage = new ImageView(
                        UiHelper.recolorImage(player.getPlayerVillage(), player.playerColorProperty().get()));
                UiHelper.adjustVisual(playerVillage, player.showVillagesProperty().get(),
                        coordinateHelper.translateAccordingToMap(player.villagePositionsProperty().get().get(i), true),
                        BIG_ICON_VECTOR);

                Label villageAnnotation = new Label(player.getVillageAnnotation());
                villageAnnotation.setTextFill(player.playerColorProperty().get());
                villageAnnotation.getStyleClass().add("boldLabel");
                UiHelper.adjustVisual(villageAnnotation, playerVillage.getX() + (STANDARD_ICON_VECTOR.getX()),
                        playerVillage.getY() - (STANDARD_ICON_VECTOR.getY() / 2));
                mapElements.putIfAbsent(playerName + "_village_" + i, playerVillage);
                mapElements.putIfAbsent(playerName + "_villageAnnotation_" + i, villageAnnotation);

            }

            for (int i = 0; i < player.cityPositionsProperty().get().size(); i++) {

                if (mapElements.get(playerName + "_city_" + i) == null) {
                    ImageView playerCity = new ImageView(
                            UiHelper.recolorImage(player.getPlayerCity(), player.playerColorProperty().get()));
                    UiHelper.adjustVisual(playerCity, player.showCitiesProperty().get(),
                            coordinateHelper.translateAccordingToMap(player.cityPositionsProperty().get().get(i), true),
                            BIG_ICON_VECTOR);
                    Label cityAnnotation = new Label(player.getCityAnnotation());
                    cityAnnotation.setTextFill(player.playerColorProperty().get());
                    cityAnnotation.getStyleClass().add("boldLabel");
                    UiHelper.adjustVisual(cityAnnotation, playerCity.getX() + (STANDARD_ICON_VECTOR.getX()),
                            playerCity.getY() - (STANDARD_ICON_VECTOR.getY() / 2));
                    mapElements.put(playerName + "_city_" + i, playerCity);
                    mapElements.put(playerName + "_cityAnnotation_" + i, cityAnnotation);
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
