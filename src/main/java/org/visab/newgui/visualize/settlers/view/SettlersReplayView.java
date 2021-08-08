package org.visab.newgui.visualize.settlers.view;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
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

public class SettlersReplayView implements FxmlView<SettlersReplayViewModel>, Initializable {

    @FXML
    private Slider turnSlider;
    @FXML
    private Slider veloSlider;

    @FXML
    private Pane drawPane;

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

    private static final Vector2 STANDARD_ICON_VECTOR = new Vector2(16, 16);

    private CoordinateHelper coordinateHelper;

    private Image pauseImage = new Image(ResourceHelper.IMAGE_PATH + "pause.png");
    private Image playImage = new Image(ResourceHelper.IMAGE_PATH + "play.png");

    private ImageView playImageView = UiHelper.resizeImage(new ImageView(playImage), new Vector2(32, 32));
    private ImageView pauseImageView = UiHelper.resizeImage(new ImageView(pauseImage), new Vector2(32, 32));

    private ObjectProperty<SettlersStatistics> turnBasedStats = new SimpleObjectProperty<>();

    private ObservableList<PlayerVisualsRow> playerVisualsRows = FXCollections.observableArrayList();
    private ObservableMap<String, Node> mapElements = FXCollections.observableHashMap();

    // Players whose values will always be up to date with the current turn
    private List<Player> players;

    @InjectViewModel
    SettlersReplayViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        players = viewModel.getPlayers();
        turnBasedStats.bind(viewModel.turnBasedStatsProperty());

        drawPane.setPrefWidth(DRAW_PANE_WIDTH);
        var drawPanePrefHeight = DRAW_PANE_WIDTH
                * ((double) viewModel.getMapRectangle().getHeight() / (double) viewModel.getMapRectangle().getWidth());
        drawPane.setPrefHeight(drawPanePrefHeight);
        coordinateHelper = new CoordinateHelper(viewModel.getMapRectangle(), drawPane.getPrefHeight(),
                drawPane.getPrefWidth(), STANDARD_ICON_VECTOR);
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

        ImageView mapImage = UiHelper.greyScaleImage(viewModel.getMapImage());
        mapImage.setViewOrder(1);
        mapImage.setFitWidth(DRAW_PANE_WIDTH);
        mapImage.setFitHeight(DRAW_PANE_WIDTH
                * ((double) viewModel.getMapRectangle().getHeight() / (double) viewModel.getMapRectangle().getWidth()));

        mapElements.put("map", mapImage);

        drawPane.getChildren().setAll(mapElements.values());
    }

    /**
     * This method initializes all the underlying player-specific information which
     * is used for proper handling across the replay view.
     */
    private void initializePlayersVisuals() {
        for (Player player : viewModel.getPlayers()) {
            var playerName = player.getName();
            HashMap<String, Image> iconMap = viewModel.getIconsForPlayer(playerName);
            player.initializeVisuals(viewModel.getPlayerColors().get(playerName), iconMap.get("playerRoad"),
                    iconMap.get("playerVillage"), iconMap.get("playerCity"));
            PlayerVisualsRow row = new PlayerVisualsRow(player.getName(),
                    UiHelper.resizeImage(new ImageView(player.getPlayerRoad()), STANDARD_ICON_VECTOR),
                    new ImageView(player.getPlayerVillage()), new ImageView(player.getPlayerCity()));
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
        // TODO: Optimize with more dynamic way - dont clear and simply put everything
        // again
        mapElements.values().clear();
        // We do not have any player-independent map items in here currently
        for (Player player : players) {

            var playerName = player.getName();

            for (int i = 0; i < player.streetPositionsProperty().get().size(); i++) {

                ImageView playerStreet = new ImageView(player.getPlayerRoad());
                UiHelper.adjustVisual(playerStreet, player.showRoadProperty().get(),
                        coordinateHelper.translateAccordingToMap(player.streetPositionsProperty().get().get(i), true));
                mapElements.put(playerName + "_street_" + i, playerStreet);
            }

            for (int i = 0; i < player.villagePositionsProperty().get().size(); i++) {

                ImageView playerVillage = new ImageView(player.getPlayerVillage());
                UiHelper.adjustVisual(playerVillage, player.showVillagesProperty().get(),
                        coordinateHelper.translateAccordingToMap(player.villagePositionsProperty().get().get(i), true));
                mapElements.put(playerName + "_village_" + i, playerVillage);
            }

            for (int i = 0; i < player.cityPositionsProperty().get().size(); i++) {

                ImageView playerCity = new ImageView(player.getPlayerRoad());
                UiHelper.adjustVisual(playerCity, player.showCitiesProperty().get(),
                        coordinateHelper.translateAccordingToMap(player.cityPositionsProperty().get().get(i), true));
                mapElements.put(playerName + "_city_" + i, playerCity);
            }
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
