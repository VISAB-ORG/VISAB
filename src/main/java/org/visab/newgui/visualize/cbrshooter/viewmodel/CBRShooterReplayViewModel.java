package org.visab.newgui.visualize.cbrshooter.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.Rectangle;
import org.visab.globalmodel.Vector2;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.globalmodel.cbrshooter.PlayerInformation;
import org.visab.newgui.visualize.ReplayViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;
import org.visab.newgui.visualize.cbrshooter.model.CoordinateHelper;
import org.visab.newgui.visualize.cbrshooter.model.PlayerDataRow;
import org.visab.newgui.visualize.cbrshooter.model.PlayerVisuals;
import org.visab.newgui.visualize.cbrshooter.model.PlayerVisualsRow;
import org.visab.workspace.config.ConfigManager;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class CBRShooterReplayViewModel extends ReplayViewModelBase<CBRShooterFile> {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(CBRShooterReplayViewModel.class);

    @InjectScope
    VisualizeScope scope;

    // Controls that are responsible to handle the JavaFX control element actions
    private Command playData;
    private Command pauseData;
    private Command setUpdateInterval;
    private Command setSelectedFrame;

    // Thread necessary to control data updating in the background
    private Thread updateLoop;

    private SimpleIntegerProperty frameSliderMaxProperty = new SimpleIntegerProperty();
    private SimpleIntegerProperty frameSliderTickUnitProperty = new SimpleIntegerProperty();
    private SimpleDoubleProperty frameSliderValueProperty = new SimpleDoubleProperty();
    private ObservableList<PlayerDataRow> currentPlayerStats = FXCollections.observableArrayList();
    private ObservableList<PlayerVisualsRow> playerVisualsRows = FXCollections.observableArrayList();
    private ObservableMap<String, Node> mapElements = FXCollections.observableHashMap();

    private SimpleStringProperty totalTimeProperty = new SimpleStringProperty();
    private SimpleStringProperty roundTimeProperty = new SimpleStringProperty();
    private SimpleStringProperty roundProperty = new SimpleStringProperty();
    private SimpleStringProperty healthCoordsProperty = new SimpleStringProperty();
    private SimpleStringProperty weaponCoordsProperty = new SimpleStringProperty();
    private SimpleStringProperty ammuCoordsProperty = new SimpleStringProperty();

    // For dynamic player amount we need a flexible map to store visuals in
    private HashMap<String, PlayerVisuals> playerVisualsMap = new HashMap<String, PlayerVisuals>();

    // Used to control the speed in which the data is updated in the replay view
    private double updateInterval;

    // Global variable that indicates which index shall be referred for data
    // extraction of the loaded statistics
    private int selectedFrame;

    private List<CBRShooterStatistics> data = new ArrayList<CBRShooterStatistics>();

    // Viewmodel always has a reference to the statistics of the current stat
    private CBRShooterStatistics frameBasedStats;

    private Rectangle mapRectangle;

    // Used for static indication of the layout of the drawpane for the replay view
    private Vector2 panePositioning = new Vector2(41, 143);
    private Vector2 paneSize = new Vector2(550, 550);

    /**
     * Called after the instance was constructed by javafx/mvvmfx.
     */
    public void initialize() {

        // Default update interval of 1 second
        updateInterval = 1000;

        selectedFrame = 0;

        // Load data from the scopes file which is initialized after VISUALIZE
        CBRShooterFile file = (CBRShooterFile) scope.getFile();
        data = file.getStatistics();
        mapRectangle = file.getMapRectangle();
        updateCurrentGameStatsByFrame();

        // Dynamically map visuals for given player amount
        initializePlayerVisuals();

        initializeVisualsTable();

        updatePlayerTableByFrame();

        initializeMapElements();

        // Make the frame sliders values always reasonable according to shooter file
        frameSliderMaxProperty.set(data.size() - 1);

        var tickUnit = data.size() / 10;

        // Make sure it is at least 1,
        if (tickUnit == 0) {
            tickUnit = 1;
        }

        frameSliderTickUnitProperty.set(tickUnit);
    }

    private void initializeVisualsTable() {
        for (PlayerInformation playerInfo : frameBasedStats.getPlayers()) {

            // Create new visuals from to make them decoupled from the map view
            ImageView playerIcon = new ImageView(playerVisualsMap.get(playerInfo.getName()).getPlayerIcon().getImage());
            Color playerColor = playerVisualsMap.get(playerInfo.getName()).getPlayerColor();
            ImageView playerPlanChange = new ImageView(
                    playerVisualsMap.get(playerInfo.getName()).getPlayerPlanChange().getImage());
            ImageView playerDeath = new ImageView(
                    playerVisualsMap.get(playerInfo.getName()).getPlayerDeath().getImage());

            playerIcon.setFitHeight(16);
            playerIcon.setFitWidth(16);
            playerPlanChange.setFitHeight(16);
            playerPlanChange.setFitWidth(16);
            playerDeath.setFitHeight(16);
            playerDeath.setFitWidth(16);

            PlayerVisualsRow playerVisualsRow = new PlayerVisualsRow(playerInfo.getName(), playerIcon, playerPlanChange,
                    playerDeath, playerColor);

            // Custom event handler that is capable of mapping the checkbox correctly
            playerVisualsRow.getShowPlayerCheckBox().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    var box = (CheckBox) event.getSource();
                    var value = box.isSelected();

                    playerVisualsMap.get(playerInfo.getName()).showAll(value);

                    updateMapElements();
                }
            });
            playerVisualsRow.getShowPlayerIconCheckBox().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    var box = (CheckBox) event.getSource();
                    var value = box.isSelected();

                    playerVisualsMap.get(playerInfo.getName()).showIcon(value);

                    updateMapElements();
                }
            });
            playerVisualsRow.getShowPlayerPathCheckBox().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    var box = (CheckBox) event.getSource();
                    var value = box.isSelected();

                    playerVisualsMap.get(playerInfo.getName()).showPath(value);

                    updateMapElements();
                }
            });
            playerVisualsRow.getShowPlayerPlanChangeCheckBox().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    var box = (CheckBox) event.getSource();
                    var value = box.isSelected();

                    playerVisualsMap.get(playerInfo.getName()).showPlanChange(value);

                    updateMapElements();
                }
            });

            playerVisualsRow.getShowPlayerDeathCheckBox().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    var box = (CheckBox) event.getSource();
                    var value = box.isSelected();

                    playerVisualsMap.get(playerInfo.getName()).showDeath(value);

                    updateMapElements();
                }
            });

            playerVisualsRows.add(playerVisualsRow);
        }
    }

    private void updatePlayerTableByFrame() {
        // Update table with current stats based on the selected frame

        for (int i = 0; i < frameBasedStats.getPlayers().size(); i++) {
            PlayerDataRow row = new PlayerDataRow(frameBasedStats.getPlayers().get(i));
            currentPlayerStats.add(i, row);
        }

    }

    /**
     * This method initializes as hash map that can be globally used across the view
     * model which provides different categories of visuals for a dynamic amount of
     * players in general.
     * 
     */
    private void initializePlayerVisuals() {
        List<PlayerInformation> playerInfos = frameBasedStats.getPlayers();

        for (int i = 0; i < playerInfos.size(); i++) {

            // TODO: Currently static coding, but it proves the concept - Changes to
            // ConfigManager mandatory to make it really dynamic
            ImageView playerIcon = new ImageView(new Image(ConfigManager.IMAGE_PATH + "scriptBot.png"));
            ImageView playerPlanChange = new ImageView(new Image(ConfigManager.IMAGE_PATH + "changePlan.png"));
            ImageView playerDeath = new ImageView(new Image(ConfigManager.IMAGE_PATH + "deathScript.png"));

            // TODO: Add random color provision
            Color color = Color.GREEN;

            playerIcon.setFitHeight(16);
            playerIcon.setFitWidth(16);
            playerPlanChange.setFitHeight(16);
            playerPlanChange.setFitWidth(16);
            playerDeath.setFitHeight(16);
            playerDeath.setFitWidth(16);

            PlayerVisuals playerVisuals = new PlayerVisuals(playerIcon, playerDeath, playerPlanChange, color);
            playerVisualsMap.put(playerInfos.get(i).getName(), playerVisuals);

        }
    }

    /**
     * This method is responsible to update the given statistics according to the
     * global variable of the currently selected frame based on various types of
     * inputs or events.
     * 
     * The view model always refers to a specific index of the overall statistics
     * that have been loaded from the file to properly display the relevant
     * information on the underlying UI of the CBR Shooter visualizer.
     * 
     */
    public void updateCurrentGameStatsByFrame() {

        // This object holds all information that is available
        frameBasedStats = data.get(selectedFrame);

        // First, update data that is applicable for every player in the game
        totalTimeProperty.set(String.valueOf(frameBasedStats.getTotalTime()));
        roundTimeProperty.set(String.valueOf(frameBasedStats.getRoundTime()));
        roundProperty.set(String.valueOf(frameBasedStats.getRound()));
        healthCoordsProperty
                .set(frameBasedStats.getHealthPosition().getX() + ", " + frameBasedStats.getHealthPosition().getY());
        weaponCoordsProperty
                .set(frameBasedStats.getWeaponPosition().getX() + ", " + frameBasedStats.getWeaponPosition().getY());
        ammuCoordsProperty.set(
                frameBasedStats.getAmmunitionPosition().getX() + ", " + frameBasedStats.getAmmunitionPosition().getY());

        // Second, update the table that holds player-specific information

        // Cleaning up the table before new values are put into it
        if (currentPlayerStats.size() > 0) {
            var lastIndex = currentPlayerStats.size() - 1;

            // Remove from end to start to make sure that the list does not get messed up
            for (int i = lastIndex; i >= 0; i--) {
                currentPlayerStats.remove(i);
            }
        }
    }

    private void initializeMapElements() {

        // Map should always be contained in the elements exactly as it is
        ImageView map = new ImageView(new Image(ConfigManager.IMAGE_PATH + "fps_map.png"));
        map.setX(panePositioning.getX());
        map.setY(panePositioning.getY());
        map.setFitHeight(paneSize.getY());
        map.setFitWidth(paneSize.getX());

        // Putting one level higher, so it is always in the background
        map.setViewOrder(1);

        // First add the fixed items to the replay view
        ImageView healthItem = new ImageView(new Image(ConfigManager.IMAGE_PATH + "healthContainer.png"));
        healthItem.setX(170);
        healthItem.setY(170);
        healthItem.setFitHeight(16);
        healthItem.setFitWidth(16);

        ImageView ammuItem = new ImageView(new Image(ConfigManager.IMAGE_PATH + "ammuContainer.png"));
        ammuItem.setX(80);
        ammuItem.setY(300);
        ammuItem.setFitHeight(16);
        ammuItem.setFitWidth(16);

        ImageView weapon = new ImageView(new Image(ConfigManager.IMAGE_PATH + "weapon.png"));
        weapon.setX(60);
        weapon.setY(250);
        weapon.setFitHeight(16);
        weapon.setFitWidth(16);

        mapElements.put("map", map);
        mapElements.put("healthItem", healthItem);
        mapElements.put("weapon", weapon);
        mapElements.put("ammuItem", ammuItem);

        // Then add the players dynamically, because there can be any amount
        for (String playerKey : playerVisualsMap.keySet()) {
            mapElements.put(playerKey + "_playerIcon", playerVisualsMap.get(playerKey).getPlayerIcon());
            mapElements.put(playerKey + "_playerDeath", playerVisualsMap.get(playerKey).getPlayerDeath());
            mapElements.put(playerKey + "_playerPlanChange", playerVisualsMap.get(playerKey).getPlayerPlanChange());
            mapElements.put(playerKey + "_playerPath", playerVisualsMap.get(playerKey).getPlayerPath());
        }

    }

    /**
     * This method directly accesses the draw pane of the replay view and puts the
     * necessary elements on it to the correct places.
     * 
     */
    private void updateMapElements() {

        // Accessing the Path
        // cbrPath.getElements().add(new MoveTo(coordinatesCBRBotListPrep.get(i),
        // coordinatesCBRBotListPrep.get(i + 1)));

        ImageView weapon = (ImageView) mapElements.get("weapon");
        Vector2 newCoordinatesWeapon = CoordinateHelper.translateAccordingToMap(mapRectangle,
                frameBasedStats.getWeaponPosition(), panePositioning, paneSize.getY(), paneSize.getX());
        weapon.setX(newCoordinatesWeapon.getX());
        weapon.setY(newCoordinatesWeapon.getY());
        mapElements.put("weapon", weapon);

        ImageView healthItem = (ImageView) mapElements.get("healthItem");
        Vector2 newCoordinatesHealthItem = CoordinateHelper.translateAccordingToMap(mapRectangle,
                frameBasedStats.getHealthPosition(), panePositioning, paneSize.getY(), paneSize.getX());
        healthItem.setX(newCoordinatesHealthItem.getX());
        healthItem.setY(newCoordinatesHealthItem.getY());
        mapElements.put("healthItem", healthItem);

        ImageView ammuItem = (ImageView) mapElements.get("ammuItem");
        Vector2 newCoordinatesAmmuItem = CoordinateHelper.translateAccordingToMap(mapRectangle,
                frameBasedStats.getAmmunitionPosition(), panePositioning, paneSize.getY(), paneSize.getX());
        ammuItem.setX(newCoordinatesAmmuItem.getX());
        ammuItem.setY(newCoordinatesAmmuItem.getY());
        mapElements.put("ammuItem", ammuItem);

        for (PlayerInformation playerInfo : frameBasedStats.getPlayers()) {
            ImageView playerIcon = (ImageView) mapElements.get(playerInfo.getName() + "_playerIcon");
            Vector2 newCoordinatesPlayerIcon = CoordinateHelper.translateAccordingToMap(mapRectangle,
                    playerInfo.getPosition(), panePositioning, paneSize.getY(), paneSize.getX());
            playerIcon.setX(newCoordinatesPlayerIcon.getX());
            playerIcon.setY(newCoordinatesPlayerIcon.getY());
            mapElements.put(playerInfo.getName() + "_playerIcon", playerIcon);

            mapElements.put(playerInfo.getName() + "_playerIcon", playerIcon);

            Path playerPath = (Path) mapElements.get(playerInfo.getName() + "_playerPath");
            playerPath.getElements().add(new MoveTo(newCoordinatesPlayerIcon.getX(), newCoordinatesPlayerIcon.getY()));
            mapElements.put(playerInfo.getName() + "_playerPath", playerPath);
        }

    }

    // --- Command methods ---

    public Command playData() {
        playData = runnableCommand(() -> {
            logger.debug("Pressed play button.");
            // Start this as a thread to provide the possibility of interrupting it on pause
            updateLoop = new Thread() {
                @Override
                public void run() {
                    // Iterate over frames and constantly update data
                    for (int i = selectedFrame; i < data.size(); i++) {
                        if (!this.isInterrupted()) {
                            // Always hold the update UI information
                            // This way is necessary, because UI changes are not allowed from another thread
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    updateCurrentGameStatsByFrame();
                                    updatePlayerTableByFrame();
                                    updateMapElements();
                                    selectedFrame++;
                                    frameSliderValueProperty.set(selectedFrame);
                                }
                            });
                            // Sleeping time depends on the velocity sliders value
                            try {
                                sleep((long) updateInterval);
                            } catch (InterruptedException e) {
                                // Exception is thrown when the stop button interrupts this thread
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                }
            };

            updateLoop.start();
        });
        return playData;
    }

    public Command pauseData() {
        pauseData = runnableCommand(() -> {
            logger.debug("Interrupting match data update loop.");
            updateLoop.interrupt();
        });
        return pauseData;
    }

    public Command setUpdateInterval(double newInterval) {
        setUpdateInterval = runnableCommand(() -> {
            updateInterval = newInterval;
        });
        return setUpdateInterval;
    }

    public Command setSelectedFrame(int frame) {
        setSelectedFrame = runnableCommand(() -> {
            selectedFrame = frame;
            updateCurrentGameStatsByFrame();
            updatePlayerTableByFrame();

            // TODO: Also call the draw map function here
        });
        return setSelectedFrame;
    }

    // --- Property getters and setters ---

    public SimpleIntegerProperty getFrameSliderMaxProperty() {
        return frameSliderMaxProperty;
    }

    public int getSelectedFrame() {
        return selectedFrame;
    }

    public void setFrameSliderProperty(SimpleIntegerProperty frameSliderMaxProperty) {
        this.frameSliderMaxProperty = frameSliderMaxProperty;
    }

    public SimpleIntegerProperty getFrameSliderTickUnitProperty() {
        return frameSliderTickUnitProperty;
    }

    public void setFrameSliderTickUnitProperty(SimpleIntegerProperty frameSliderTickUnitProperty) {
        this.frameSliderTickUnitProperty = frameSliderTickUnitProperty;
    }

    public SimpleDoubleProperty getFrameSliderValueProperty() {
        return frameSliderValueProperty;
    }

    public void setFrameSliderValueProperty(SimpleDoubleProperty frameSliderValueProperty) {
        this.frameSliderValueProperty = frameSliderValueProperty;
    }

    public ObservableList<PlayerDataRow> getCurrentPlayerStats() {
        return currentPlayerStats;
    }

    public void setCurrentPlayerStats(ObservableList<PlayerDataRow> currentPlayerStats) {
        this.currentPlayerStats = currentPlayerStats;
    }

    public ObservableList<PlayerVisualsRow> getPlayerVisualsRows() {
        return playerVisualsRows;
    }

    public void setPlayerVisualsRows(ObservableList<PlayerVisualsRow> playerVisualsRows) {
        this.playerVisualsRows = playerVisualsRows;
    }

    public ObservableMap<String, Node> getMapElements() {
        return mapElements;
    }

    public void setMapElements(ObservableMap<String, Node> mapElements) {
        this.mapElements = mapElements;
    }

    public SimpleStringProperty getTotalTimeProperty() {
        return totalTimeProperty;
    }

    public void setTotalTimeProperty(SimpleStringProperty totalTimeProperty) {
        this.totalTimeProperty = totalTimeProperty;
    }

    public SimpleStringProperty getRoundTimeProperty() {
        return roundTimeProperty;
    }

    public void setRoundTimeProperty(SimpleStringProperty roundTimeProperty) {
        this.roundTimeProperty = roundTimeProperty;
    }

    public SimpleStringProperty getRoundProperty() {
        return roundProperty;
    }

    public void setRoundProperty(SimpleStringProperty roundProperty) {
        this.roundProperty = roundProperty;
    }

    public SimpleStringProperty getHealthCoordsProperty() {
        return healthCoordsProperty;
    }

    public void setHealthCoordsProperty(SimpleStringProperty healthCoordsProperty) {
        this.healthCoordsProperty = healthCoordsProperty;
    }

    public SimpleStringProperty getWeaponCoordsProperty() {
        return weaponCoordsProperty;
    }

    public void setWeaponCoordsProperty(SimpleStringProperty weaponCoordsProperty) {
        this.weaponCoordsProperty = weaponCoordsProperty;
    }

    public SimpleStringProperty getAmmuCoordsProperty() {
        return ammuCoordsProperty;
    }

    public void setAmmuCoordsProperty(SimpleStringProperty ammuCoordsProperty) {
        this.ammuCoordsProperty = ammuCoordsProperty;
    }

}
