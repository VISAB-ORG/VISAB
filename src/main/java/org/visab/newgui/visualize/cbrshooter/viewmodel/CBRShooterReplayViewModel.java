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
import org.visab.util.VISABUtil;
import org.visab.workspace.Workspace;
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
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Pair;

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
    private Command visualizeMapElement;

    // Thread necessary to control data updating in the background
    private Thread updateLoop;

    // Custom row lists for the table views
    private ObservableList<PlayerDataRow> currentPlayerStats = FXCollections.observableArrayList();
    private ObservableList<PlayerVisualsRow> playerVisualsRows = FXCollections.observableArrayList();

    // Generic properties of the replay view to display information
    private SimpleIntegerProperty frameSliderMaxProperty = new SimpleIntegerProperty();
    private SimpleIntegerProperty frameSliderTickUnitProperty = new SimpleIntegerProperty();
    private SimpleDoubleProperty frameSliderValueProperty = new SimpleDoubleProperty();
    private SimpleStringProperty totalTimeProperty = new SimpleStringProperty();
    private SimpleStringProperty roundTimeProperty = new SimpleStringProperty();
    private SimpleStringProperty roundProperty = new SimpleStringProperty();
    private SimpleStringProperty healthCoordsProperty = new SimpleStringProperty();
    private SimpleStringProperty weaponCoordsProperty = new SimpleStringProperty();
    private SimpleStringProperty ammuCoordsProperty = new SimpleStringProperty();

    // Contains color-coded visuals for each player in the game
    private HashMap<String, PlayerVisuals> playerVisualsMap = new HashMap<String, PlayerVisuals>();
    private ObservableMap<String, Pair<Node, Boolean>> mapElements = FXCollections.observableHashMap();

    private HashMap<String, String> latestPlansOfPlayers = new HashMap<String, String>();

    // Used to control the speed in which the data is updated in the replay view
    private double updateInterval;

    // Global variable that indicates which index shall be referred for data
    // extraction of the loaded statistics
    private int selectedFrame;

    // Contains all information that changes based on the frame
    private List<CBRShooterStatistics> data = new ArrayList<CBRShooterStatistics>();

    // Viewmodel always has a reference to the statistics of the current stat
    private CBRShooterStatistics frameBasedStats;

    // The underlying file of the opened view
    private CBRShooterFile file;

    // A model rectangle that is used to calculate map positioning
    private Rectangle mapRectangle;

    // Used for static indication of the layout of the drawpane for the replay view
    private Vector2 panePositioning = new Vector2(41, 143);
    private Vector2 paneSize = new Vector2();

    private CoordinateHelper coordinateHelper;

    /**
     * Called after the instance was constructed by javafx/mvvmfx.
     */
    public void initialize() {

        // Defaults
        updateInterval = 1000;
        selectedFrame = 0;

        // Load data from the scopes file which is initialized after VISUALIZE
        file = (CBRShooterFile) scope.getFile();
        data = file.getStatistics();
        mapRectangle = file.getMapRectangle();

        // Ensure that the pane has the same relative format as the game map
        paneSize.setX(550);
        double computedPaneHeight = ((double) mapRectangle.getHeight() / (double) mapRectangle.getWidth())
                * (double) paneSize.getX();
        paneSize.setY((int) computedPaneHeight);

        // Coordinate helper used to compute positioning on the replay view
        coordinateHelper = new CoordinateHelper(mapRectangle, paneSize.getY(), paneSize.getX(), panePositioning);

        // Initialize all relevant data
        updateCurrentGameStatsByFrame();
        initializePlayerVisuals();
        initializeVisualsTable();
        initializeMapElements();
        updatePlayerTableByFrame();

        // Make the frame sliders values always reasonable according to shooter file
        frameSliderMaxProperty.set(data.size() - 1);

        var tickUnit = data.size() / 10;

        // Make sure it is at least 1,
        if (tickUnit == 0) {
            tickUnit = 1;
        }

        frameSliderTickUnitProperty.set(tickUnit);
    }

    /**
     * Initializes the table that contains all visuals for each player and
     * checkboxes that are capable of showing and hiding specific elements for each
     * player.
     * 
     */
    private void initializeVisualsTable() {
        for (PlayerInformation playerInfo : frameBasedStats.getPlayers()) {

            // Create new visuals from to make them decoupled from the map view
            ImageView playerIcon = new ImageView(playerVisualsMap.get(playerInfo.getName()).getPlayerIcon());
            Color playerColor = playerVisualsMap.get(playerInfo.getName()).getPlayerColor();
            ImageView playerPlanChange = new ImageView(
                    playerVisualsMap.get(playerInfo.getName()).getPlayerPlanChange());
            ImageView playerDeath = new ImageView(playerVisualsMap.get(playerInfo.getName()).getPlayerDeath());

            // Sizing to default icon size
            playerIcon.setFitHeight(16);
            playerIcon.setFitWidth(16);
            playerPlanChange.setFitHeight(16);
            playerPlanChange.setFitWidth(16);
            playerDeath.setFitHeight(16);
            playerDeath.setFitWidth(16);

            PlayerVisualsRow playerVisualsRow = new PlayerVisualsRow(playerInfo.getName(), playerIcon, playerPlanChange,
                    playerDeath, playerColor);

            // Update visibility every player specific element
            playerVisualsRow.getShowPlayerCheckBox().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    var box = (CheckBox) event.getSource();
                    var value = box.isSelected();

                    mapElements.put(playerInfo.getName() + "_playerIcon", new Pair<Node, Boolean>(
                            mapElements.get(playerInfo.getName() + "_playerIcon").getKey(), value));
                    mapElements.put(playerInfo.getName() + "_playerPlanChange", new Pair<Node, Boolean>(
                            mapElements.get(playerInfo.getName() + "_playerPlanChange").getKey(), value));
                    mapElements.put(playerInfo.getName() + "_playerDeath", new Pair<Node, Boolean>(
                            mapElements.get(playerInfo.getName() + "_playerDeath").getKey(), value));
                    mapElements.put(playerInfo.getName() + "_playerPath", new Pair<Node, Boolean>(
                            mapElements.get(playerInfo.getName() + "_playerPath").getKey(), value));

                    updateMapElements();
                }
            });

            // Update visibility of player icon
            playerVisualsRow.getShowPlayerIconCheckBox().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    var box = (CheckBox) event.getSource();
                    var value = box.isSelected();

                    mapElements.put(playerInfo.getName() + "_playerIcon", new Pair<Node, Boolean>(
                            mapElements.get(playerInfo.getName() + "_playerIcon").getKey(), value));

                    updateMapElements();
                }
            });

            // Update visibility of player path
            playerVisualsRow.getShowPlayerPathCheckBox().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    var box = (CheckBox) event.getSource();
                    var value = box.isSelected();

                    mapElements.put(playerInfo.getName() + "_playerPath", new Pair<Node, Boolean>(
                            mapElements.get(playerInfo.getName() + "_playerPath").getKey(), value));

                    updateMapElements();
                }
            });

            // Update visibility of player plan change
            playerVisualsRow.getShowPlayerPlanChangeCheckBox().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    var box = (CheckBox) event.getSource();
                    var value = box.isSelected();

                    mapElements.put(playerInfo.getName() + "_playerPlanChange", new Pair<Node, Boolean>(
                            mapElements.get(playerInfo.getName() + "_playerPlanChange").getKey(), value));

                    updateMapElements();
                }
            });

            // Update visibility of player death
            playerVisualsRow.getShowPlayerDeathCheckBox().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    var box = (CheckBox) event.getSource();
                    var value = box.isSelected();

                    mapElements.put(playerInfo.getName() + "_playerDeath", new Pair<Node, Boolean>(
                            mapElements.get(playerInfo.getName() + "_playerDeath").getKey(), value));

                    updateMapElements();
                }
            });

            playerVisualsRows.add(playerVisualsRow);
        }
    }

    /**
     * This methods fills the player data table with custom rows containing all
     * relevant player specific information for the selected frame.
     * 
     */
    private void updatePlayerTableByFrame() {
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

        for (PlayerInformation playerInfo : playerInfos) {
            // Unity Game provides Hex code which has to be translated to a JavaFx color
            Color playerColor = VISABUtil.translateHexToRgbColor(file.getPlayerColors().get(playerInfo.getName()));

            // Get base icons from the game manager which need to be recolored
            // @TODO: Use sent player images by unity
            Image playerIcon = new Image(ConfigManager.IMAGE_PATH + "cbrBot.png");
            Image playerPlanChange = new Image(
                    Workspace.getInstance().getConfigManager().getShooterBaseIconById("playerPlanChange"));
            Image playerDeath = new Image(
                    Workspace.getInstance().getConfigManager().getShooterBaseIconById("playerDeath"));

            // Recolor images
            playerPlanChange = VISABUtil.recolorImage(playerPlanChange, playerColor);
            playerDeath = VISABUtil.recolorImage(playerDeath, playerColor);

            PlayerVisuals playerVisuals = new PlayerVisuals(playerIcon, playerDeath, playerPlanChange, playerColor);
            playerVisualsMap.put(playerInfo.getName(), playerVisuals);

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

    /**
     * This method directly accesses the draw pane of the replay view and puts the
     * necessary elements on it to the correct places.
     * 
     * On every call it checks whether or not any element should be visible
     * according to the respective check box selection and sets it accordingly.
     * 
     */
    private void updateMapElements() {

        Vector2 healthItemPosition = frameBasedStats.getHealthPosition();
        Vector2 ammuItemPosition = frameBasedStats.getAmmunitionPosition();
        Vector2 weaponPosition = frameBasedStats.getWeaponPosition();

        // If the respective vector is (0,0) the item did not spawn yet
        if (healthItemPosition.getX() != 0 && healthItemPosition.getY() != 0) {
            ImageView healthItem = (ImageView) mapElements.get("healthItem").getKey();
            Vector2 translatedHealthItemPosition = coordinateHelper.translateAccordingToMap(healthItemPosition);
            healthItem.setX(translatedHealthItemPosition.getX());
            healthItem.setY(translatedHealthItemPosition.getY());
            boolean shallBeVisible = mapElements.get("healthItem").getValue();
            healthItem.setVisible(shallBeVisible);
            mapElements.put("healthItem", new Pair<Node, Boolean>(healthItem, shallBeVisible));
        } else {
            mapElements.get("healthItem").getKey().setVisible(false);
        }

        if (ammuItemPosition.getX() != 0 && ammuItemPosition.getY() != 0) {
            ImageView ammuItem = (ImageView) mapElements.get("ammuItem").getKey();
            Vector2 translatedAmmuItemPosition = coordinateHelper.translateAccordingToMap(ammuItemPosition);
            ammuItem.setX(translatedAmmuItemPosition.getX());
            ammuItem.setY(translatedAmmuItemPosition.getY());
            boolean shallBeVisible = mapElements.get("ammuItem").getValue();
            ammuItem.setVisible(shallBeVisible);
            mapElements.put("ammuItem", new Pair<Node, Boolean>(ammuItem, shallBeVisible));

        } else {
            mapElements.get("ammuItem").getKey().setVisible(false);
        }

        if (weaponPosition.getX() != 0 && weaponPosition.getY() != 0) {
            ImageView weapon = (ImageView) mapElements.get("weapon").getKey();
            Vector2 translatedWeaponPosition = coordinateHelper.translateAccordingToMap(weaponPosition);
            weapon.setX(translatedWeaponPosition.getX());
            weapon.setY(translatedWeaponPosition.getY());
            boolean shallBeVisible = mapElements.get("weapon").getValue();
            weapon.setVisible(shallBeVisible);
            mapElements.put("weapon", new Pair<Node, Boolean>(weapon, shallBeVisible));
        } else {
            mapElements.get("weapon").getKey().setVisible(false);
        }

        for (PlayerInformation playerInfo : frameBasedStats.getPlayers()) {
            ImageView playerIcon = (ImageView) mapElements.get(playerInfo.getName() + "_playerIcon").getKey();
            Vector2 playerPosition = coordinateHelper.translateAccordingToMap(playerInfo.getPosition());
            playerIcon.setX(playerPosition.getX());
            playerIcon.setY(playerPosition.getY());
            boolean iconShallBeVisible = mapElements.get(playerInfo.getName() + "_playerIcon").getValue();
            playerIcon.setVisible(iconShallBeVisible);

            Path playerPath = (Path) mapElements.get(playerInfo.getName() + "_playerPath").getKey();

            System.out.println("Selected frame: " + selectedFrame + ", path size: " + playerPath.getElements().size());
            if (playerPath.getElements().size() - 1 > selectedFrame) {
                for (int i = playerPath.getElements().size() - 1; i >= selectedFrame; i--) {
                    System.out.println("removing index of list: " + i);
                    playerPath.getElements().remove(i);
                }
            }

            playerPath.getElements().add(new LineTo(playerPosition.getX() + (playerIcon.getFitWidth() / 2),
                    playerPosition.getY() + (playerIcon.getFitHeight() / 2)));
            boolean pathShallBeVisible = mapElements.get(playerInfo.getName() + "_playerPath").getValue();
            playerPath.setVisible(pathShallBeVisible);

            // Decide if a plan change must be visualized on the map
            if (latestPlansOfPlayers.get(playerInfo.getName()) != null) {
                if (!latestPlansOfPlayers.get(playerInfo.getName()).equals(playerInfo.getPlan())) {
                    System.out.println("Plan change triggered");
                    ImageView playerPlanChange = (ImageView) mapElements.get(playerInfo.getName() + "_playerPlanChange")
                            .getKey();
                    playerPlanChange.setX(playerPosition.getX());
                    playerPlanChange.setY(playerPosition.getY());
                    boolean planChangeShallBeVisible = mapElements.get(playerInfo.getName() + "_playerPlanChange")
                            .getValue();
                    System.out.println("And shall be visible: " + planChangeShallBeVisible);
                    playerPlanChange.setVisible(planChangeShallBeVisible);
                    mapElements.put(playerInfo.getName() + "_playerPlanChange",
                            new Pair<Node, Boolean>(playerPlanChange, planChangeShallBeVisible));
                }
            }
            latestPlansOfPlayers.put(playerInfo.getName(), playerInfo.getPlan());

            mapElements.put(playerInfo.getName() + "_playerIcon",
                    new Pair<Node, Boolean>(playerIcon, iconShallBeVisible));
            mapElements.put(playerInfo.getName() + "_playerPath",
                    new Pair<Node, Boolean>(playerPath, pathShallBeVisible));
        }
    }

    /**
     * Initializes all map elements once the replay view is loaded.
     */
    private void initializeMapElements() {

        // Map should always be contained in the elements exactly as it is
        ImageView map = VISABUtil.greyScaleImage(new Image(ConfigManager.IMAGE_PATH + "fps_map.png"));
        map.setX(panePositioning.getX());
        map.setY(panePositioning.getY());
        map.setFitHeight(paneSize.getY());
        map.setFitWidth(paneSize.getX());

        // Putting one level higher, so it is always in the background
        map.setViewOrder(1);

        // First add the fixed items to the replay view
        ImageView healthItem = new ImageView(new Image(ConfigManager.IMAGE_PATH + "healthContainer.png"));
        healthItem.setX(panePositioning.getX());
        healthItem.setY(panePositioning.getY());
        healthItem.setFitHeight(16);
        healthItem.setFitWidth(16);
        healthItem.setVisible(false);

        ImageView ammuItem = new ImageView(new Image(ConfigManager.IMAGE_PATH + "ammuContainer.png"));
        ammuItem.setX(panePositioning.getX());
        ammuItem.setY(panePositioning.getY());
        ammuItem.setFitHeight(16);
        ammuItem.setFitWidth(16);
        ammuItem.setVisible(false);

        ImageView weapon = new ImageView(new Image(ConfigManager.IMAGE_PATH + "weapon.png"));
        weapon.setX(panePositioning.getX());
        weapon.setY(panePositioning.getY());
        weapon.setFitHeight(16);
        weapon.setFitWidth(16);
        weapon.setVisible(false);

        // True because items should be visible as soon as they appear
        mapElements.put("map", new Pair<Node, Boolean>(map, true));
        mapElements.put("healthItem", new Pair<Node, Boolean>(healthItem, true));
        mapElements.put("weapon", new Pair<Node, Boolean>(weapon, true));
        mapElements.put("ammuItem", new Pair<Node, Boolean>(ammuItem, true));

        for (PlayerInformation playerInfo : frameBasedStats.getPlayers()) {
            ImageView playerIcon = new ImageView(playerVisualsMap.get(playerInfo.getName()).getPlayerIcon());
            Vector2 playerPosition = coordinateHelper.translateAccordingToMap(playerInfo.getPosition());
            playerIcon.setX(playerPosition.getX());
            playerIcon.setY(playerPosition.getY());
            playerIcon.setFitHeight(16);
            playerIcon.setFitWidth(16);

            ImageView playerDeath = new ImageView(playerVisualsMap.get(playerInfo.getName()).getPlayerDeath());
            playerDeath.setX(panePositioning.getX());
            playerDeath.setY(panePositioning.getY());
            playerDeath.setFitHeight(16);
            playerDeath.setFitWidth(16);
            playerDeath.setVisible(false);

            ImageView playerPlanChange = new ImageView(
                    playerVisualsMap.get(playerInfo.getName()).getPlayerPlanChange());
            playerPlanChange.setX(panePositioning.getX());
            playerPlanChange.setY(panePositioning.getY());
            playerPlanChange.setFitHeight(16);
            playerPlanChange.setFitWidth(16);
            playerPlanChange.setVisible(false);

            Path playerPath = new Path();
            playerPath.setStroke(playerVisualsMap.get(playerInfo.getName()).getPlayerColor());
            playerPath.setStrokeWidth(2);
            playerPath.getElements().add(new MoveTo(playerPosition.getX(), playerPosition.getY()));
            playerPath.setVisible(true);

            mapElements.put(playerInfo.getName() + "_playerIcon", new Pair<Node, Boolean>(playerIcon, true));
            mapElements.put(playerInfo.getName() + "_playerDeath", new Pair<Node, Boolean>(playerDeath, true));
            mapElements.put(playerInfo.getName() + "_playerPlanChange",
                    new Pair<Node, Boolean>(playerPlanChange, true));
            mapElements.put(playerInfo.getName() + "_playerPath", new Pair<Node, Boolean>(playerPath, true));
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
            // Prevent selected frame to be out of bound when play is running and the
            // frameslider gets shifted unfortunately
            if (frame > data.size()) {
                selectedFrame = frame - 1;
            } else {
                selectedFrame = frame;
            }
            updateCurrentGameStatsByFrame();
            updatePlayerTableByFrame();
            updateMapElements();
        });
        return setSelectedFrame;
    }

    public Command visualizeMapElement(String key, boolean value) {
        visualizeMapElement = runnableCommand(() -> {
            mapElements.put(key, new Pair<Node, Boolean>(mapElements.get(key).getKey(), value));
            updateMapElements();
        });
        return visualizeMapElement;
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

    public ObservableList<Node> getMapElements() {
        ObservableList<Node> mapElementsList = FXCollections.observableArrayList();

        for (Pair<Node, Boolean> mapPair : mapElements.values()) {
            mapElementsList.add(mapPair.getKey());
        }
        return mapElementsList;
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
