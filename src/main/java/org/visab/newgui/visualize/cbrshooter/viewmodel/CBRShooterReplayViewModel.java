package org.visab.newgui.visualize.cbrshooter.viewmodel;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.IStatistics;
import org.visab.globalmodel.Rectangle;
import org.visab.globalmodel.Vector2;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.globalmodel.cbrshooter.PlayerInformation;
import org.visab.newgui.UiHelper;
import org.visab.newgui.visualize.ILiveViewModel;
import org.visab.newgui.visualize.ReplayViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;
import org.visab.newgui.visualize.cbrshooter.model.CoordinateHelper;
import org.visab.newgui.visualize.cbrshooter.model.PlayerDataRow;
import org.visab.newgui.visualize.cbrshooter.model.PlayerVisuals;
import org.visab.newgui.visualize.cbrshooter.model.PlayerVisualsRow;
import org.visab.processing.ILiveViewable;
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

public class CBRShooterReplayViewModel extends ReplayViewModelBase<CBRShooterFile>
        implements ILiveViewModel<CBRShooterStatistics> {

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
    private SimpleDoubleProperty drawPanePositionXProperty = new SimpleDoubleProperty();
    private SimpleDoubleProperty drawPanePositionYProperty = new SimpleDoubleProperty();

    // Contains color-coded visuals for each player in the game
    private HashMap<String, PlayerVisuals> playerVisualsMap = new HashMap<String, PlayerVisuals>();
    private ObservableMap<String, Pair<Node, Boolean>> mapElements = FXCollections.observableHashMap();

    private HashMap<String, String> latestPlansOfPlayers = new HashMap<String, String>();
    private HashMap<String, Integer> latestDeathsOfPlayers = new HashMap<String, Integer>();

    // Used to control the speed in which the data is updated in the replay view
    private double updateInterval;

    // Global variable that indicates which index shall be referred for data
    // extraction of the loaded statistics
    private int selectedFrame;

    // Necessary to decide which elements shall be "resetted" for each round
    private int roundCounter;

    // Contains all information that changes based on the frame
    private List<CBRShooterStatistics> data = new ArrayList<CBRShooterStatistics>();

    // Viewmodel always has a reference to the statistics of the current stat
    private CBRShooterStatistics frameBasedStats;

    // A model rectangle that is used to calculate map positioning
    private Rectangle mapRectangle;

    // Used for static indication of the layout of the drawpane for the replay view
    private Vector2 panePositioning = new Vector2();
    private Vector2 paneSize = new Vector2();
    private static final Vector2 STANDARD_ICON_VECTOR = new Vector2(16, 16);

    private CoordinateHelper coordinateHelper;

    /**
     * Called after the instance was constructed by javafx/mvvmfx.
     */
    public void initialize() {

        // Update loop eventually needs to be stopped on stage close
        scope.registerOnStageClosing(stage -> {
            if (updateLoop != null) {
                updateLoop.interrupt();
            }
        });

        // Default update interval of 0.1 seconds
        updateInterval = 100;
        selectedFrame = 0;

        // TODO: Might not be to hard to have this work live aswell
        // Load data from the scopes file which is initialized after VISUALIZE
        if (scope.isLive()) {
            initialize(scope.getSessionListener());
        } else {
            initialize(scope.getFile());
        }

        data = file.getStatistics();

        panePositioning.setX(drawPanePositionXProperty.intValue());
        panePositioning.setY(drawPanePositionYProperty.intValue());

        mapRectangle = file.getMapRectangle();

        // Ensure that the pane has the same relative format as the game
        paneSize.setX(550);
        double computedPaneHeight = ((double) mapRectangle.getHeight() / (double) mapRectangle.getWidth())
                * (double) paneSize.getX();
        paneSize.setY((int) computedPaneHeight);

        // Coordinate helper used to compute positioning on the replay view
        coordinateHelper = new CoordinateHelper(mapRectangle, paneSize.getY(), paneSize.getX(), panePositioning);

        // Initialize all relevant data, order is important
        updateCurrentGameStatsByFrame();
        initializePlayerVisuals();
        initializeVisualsTable();
        initializeMapElements();
        updatePlayerTableByFrame();

        roundCounter = frameBasedStats.getRound();

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
            playerIcon.setFitHeight(STANDARD_ICON_VECTOR.getY());
            playerIcon.setFitWidth(STANDARD_ICON_VECTOR.getX());
            playerPlanChange.setFitHeight(STANDARD_ICON_VECTOR.getY());
            playerPlanChange.setFitWidth(STANDARD_ICON_VECTOR.getX());
            playerDeath.setFitHeight(STANDARD_ICON_VECTOR.getY());
            playerDeath.setFitWidth(STANDARD_ICON_VECTOR.getX());

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

                    playerVisualsRow.getShowPlayerIconCheckBox().setSelected(value);
                    playerVisualsRow.getShowPlayerPathCheckBox().setSelected(value);
                    playerVisualsRow.getShowPlayerDeathCheckBox().setSelected(value);
                    playerVisualsRow.getShowPlayerPlanChangeCheckBox().setSelected(value);

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
            Color playerColor = UiHelper.translateHexToRgbColor(file.getPlayerColors().get(playerInfo.getName()));

            // Get base icons from the game manager which need to be recolored
            // @TODO: Use sent player images by unity
            // Currently only "hacked" workaround because images sent by the game dont work
            Image playerIcon;
            if (playerInfo.getName().equals("John Doe")) {
                playerIcon = new Image(ConfigManager.IMAGE_PATH + "cbrBot.png");
            } else {
                playerIcon = new Image(ConfigManager.IMAGE_PATH + "scriptBot.png");
            }

            Image playerPlanChange = new Image(
                    Workspace.getInstance().getConfigManager().getShooterBaseIconById("playerPlanChange"));
            Image playerDeath = new Image(
                    Workspace.getInstance().getConfigManager().getShooterBaseIconById("playerDeath"));

            // Recolor images
            playerIcon = UiHelper.recolorImage(playerIcon, playerColor);
            playerPlanChange = UiHelper.recolorImage(playerPlanChange, playerColor);
            playerDeath = UiHelper.recolorImage(playerDeath, playerColor);

            PlayerVisuals playerVisuals = new PlayerVisuals(playerIcon, playerDeath, playerPlanChange, playerColor);
            playerVisualsMap.put(playerInfo.getName(), playerVisuals);

        }
    }

    /**
     * Simple method that encapsulates various updates into one call.
     */
    private void updateEverythingByFrame() {
        updateCurrentGameStatsByFrame();
        updatePlayerTableByFrame();
        updateMapElements();
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

        var healthItemPosition = frameBasedStats.getHealthPosition();
        var ammuItemPosition = frameBasedStats.getAmmunitionPosition();
        var weaponPosition = frameBasedStats.getWeaponPosition();

        // If the respective vector is (0,0) the item did not spawn yet
        if (healthItemPosition.getX() != 0 && healthItemPosition.getY() != 0) {
            var healthItem = (ImageView) mapElements.get("healthItem").getKey();
            var translatedHealthItemPosition = coordinateHelper.translateAccordingToMap(healthItemPosition);
            var shallBeVisible = mapElements.get("healthItem").getValue();
            UiHelper.adjustVisual(healthItem, shallBeVisible, translatedHealthItemPosition.getX(),
                    translatedHealthItemPosition.getY());
            mapElements.put("healthItem", new Pair<Node, Boolean>(healthItem, shallBeVisible));
        } else {
            mapElements.get("healthItem").getKey().setVisible(false);
        }

        if (ammuItemPosition.getX() != 0 && ammuItemPosition.getY() != 0) {
            var ammuItem = (ImageView) mapElements.get("ammuItem").getKey();
            var translatedAmmuItemPosition = coordinateHelper.translateAccordingToMap(ammuItemPosition);
            var shallBeVisible = mapElements.get("ammuItem").getValue();
            UiHelper.adjustVisual(ammuItem, shallBeVisible, translatedAmmuItemPosition.getX(),
                    translatedAmmuItemPosition.getY());
            mapElements.put("ammuItem", new Pair<Node, Boolean>(ammuItem, shallBeVisible));

        } else {
            mapElements.get("ammuItem").getKey().setVisible(false);
        }

        if (weaponPosition.getX() != 0 && weaponPosition.getY() != 0) {
            var weapon = (ImageView) mapElements.get("weapon").getKey();
            var translatedWeaponPosition = coordinateHelper.translateAccordingToMap(weaponPosition);
            var shallBeVisible = mapElements.get("weapon").getValue();
            UiHelper.adjustVisual(weapon, shallBeVisible, translatedWeaponPosition.getX(),
                    translatedWeaponPosition.getY());
            mapElements.put("weapon", new Pair<Node, Boolean>(weapon, shallBeVisible));
        } else {
            mapElements.get("weapon").getKey().setVisible(false);
        }

        // Iterate to draw player specific visuals
        for (int i = 0; i < frameBasedStats.getPlayers().size(); i++) {
            var playerInfo = frameBasedStats.getPlayers().get(i);

            // Player icon
            var playerIcon = (ImageView) mapElements.get(playerInfo.getName() + "_playerIcon").getKey();
            var playerPosition = coordinateHelper.translateAccordingToMap(playerInfo.getPosition());
            var iconShallBeVisible = mapElements.get(playerInfo.getName() + "_playerIcon").getValue();
            UiHelper.adjustVisual(playerIcon, iconShallBeVisible, playerPosition.getX(), playerPosition.getY());

            // Player path
            var playerPath = (Path) mapElements.get(playerInfo.getName() + "_playerPath").getKey();
            if (roundCounter == frameBasedStats.getRound()) {
                playerPath.getElements().add(new LineTo(playerPosition.getX() + (playerIcon.getFitWidth() / 2),
                        playerPosition.getY() + (playerIcon.getFitHeight() / 2)));
            } else {
                playerPath.getElements().clear();
                playerPath.getElements().add(new MoveTo(playerPosition.getX() + (playerIcon.getFitWidth() / 2),
                        playerPosition.getY() + (playerIcon.getFitHeight() / 2)));
            }
            var pathShallBeVisible = mapElements.get(playerInfo.getName() + "_playerPath").getValue();
            playerPath.setVisible(pathShallBeVisible);

            if (latestDeathsOfPlayers.get(playerInfo.getName()) != null) {
                var playerDeath = (ImageView) mapElements.get(playerInfo.getName() + "_playerDeath").getKey();
                if (latestDeathsOfPlayers.get(playerInfo.getName()) < playerInfo.getStatistics().getDeaths()) {
                    if (data.get(selectedFrame - 1) != null) {
                        var playerDeathPosition = coordinateHelper
                                .translateAccordingToMap(data.get(selectedFrame - 1).getPlayers().get(i).getPosition());
                        UiHelper.adjustVisual(playerDeath, playerDeathPosition);
                    }
                }

                var deathShallBeVisible = mapElements.get(playerInfo.getName() + "_playerDeath").getValue();
                if (!(playerDeath.getX() == panePositioning.getX() && playerDeath.getY() == panePositioning.getY())) {
                    playerDeath.setVisible(deathShallBeVisible);
                }

                mapElements.put(playerInfo.getName() + "_playerDeath",
                        new Pair<Node, Boolean>(playerDeath, deathShallBeVisible));
            }
            latestDeathsOfPlayers.put(playerInfo.getName(), playerInfo.getStatistics().getDeaths());

            // Player plan changes
            if (latestPlansOfPlayers.get(playerInfo.getName()) != null) {
                var playerPlanChange = (ImageView) mapElements.get(playerInfo.getName() + "_playerPlanChange").getKey();
                if (!latestPlansOfPlayers.get(playerInfo.getName()).equals(playerInfo.getPlan())) {

                    if (playerPlanChange.getX() != playerPosition.getX()
                            && playerPlanChange.getY() != playerPosition.getY()) {
                    	UiHelper.adjustVisual(playerPlanChange, playerPosition);
                    }
                }
                var planChangeShallBeVisible = mapElements.get(playerInfo.getName() + "_playerPlanChange").getValue();
                playerPlanChange.setVisible(planChangeShallBeVisible);
                mapElements.put(playerInfo.getName() + "_playerPlanChange",
                        new Pair<Node, Boolean>(playerPlanChange, planChangeShallBeVisible));
            }
            latestPlansOfPlayers.put(playerInfo.getName(), playerInfo.getPlan());

            mapElements.put(playerInfo.getName() + "_playerIcon",
                    new Pair<Node, Boolean>(playerIcon, iconShallBeVisible));
            mapElements.put(playerInfo.getName() + "_playerPath",
                    new Pair<Node, Boolean>(playerPath, pathShallBeVisible));

        }
        roundCounter = frameBasedStats.getRound();
    }

    /**
     * Initializes all map elements once the replay view is loaded.
     */
    private void initializeMapElements() {

        // Map should always be contained in the elements exactly as it is
        ImageView map = UiHelper.greyScaleImage(new Image(new ByteArrayInputStream(file.getImages().getMap())));
        UiHelper.adjustVisual(map, panePositioning, paneSize);

        // Putting one level higher, so it is always in the background
        map.setViewOrder(1);

        // First add the fixed items to the replay view
        ImageView healthItem = new ImageView(
                new Image(new ByteArrayInputStream(file.getImages().getStaticObjects().get("Health"))));
        UiHelper.adjustVisual(healthItem, false, panePositioning, STANDARD_ICON_VECTOR);

        ImageView ammuItem = new ImageView(
                new Image(new ByteArrayInputStream(file.getImages().getStaticObjects().get("WeaponCrate"))));
        UiHelper.adjustVisual(ammuItem, false, panePositioning, STANDARD_ICON_VECTOR);

        ImageView weapon = new ImageView(new Image(ConfigManager.IMAGE_PATH + "/weapon.png"));
        // --- This somehow does not work yet ---
        // ImageView weapon = new ImageView(
        // new Image(new
        // ByteArrayInputStream(file.getImages().getStaticObjects().get("M4a1"))));
        UiHelper.adjustVisual(weapon, false, panePositioning, STANDARD_ICON_VECTOR);

        // True because items should be visible as soon as they appear
        mapElements.put("map", new Pair<Node, Boolean>(map, true));
        mapElements.put("healthItem", new Pair<Node, Boolean>(healthItem, true));
        mapElements.put("weapon", new Pair<Node, Boolean>(weapon, true));
        mapElements.put("ammuItem", new Pair<Node, Boolean>(ammuItem, true));

        for (PlayerInformation playerInfo : frameBasedStats.getPlayers()) {
            ImageView playerIcon = new ImageView(playerVisualsMap.get(playerInfo.getName()).getPlayerIcon());
            Vector2 playerPosition = coordinateHelper.translateAccordingToMap(playerInfo.getPosition());
            UiHelper.adjustVisual(playerIcon, playerPosition, STANDARD_ICON_VECTOR);

            ImageView playerDeath = new ImageView(playerVisualsMap.get(playerInfo.getName()).getPlayerDeath());
            UiHelper.adjustVisual(playerDeath, false, panePositioning, STANDARD_ICON_VECTOR);

            ImageView playerPlanChange = new ImageView(
                    playerVisualsMap.get(playerInfo.getName()).getPlayerPlanChange());
            UiHelper.adjustVisual(playerPlanChange, false, panePositioning, STANDARD_ICON_VECTOR);

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
                    while (!this.isInterrupted()) {
                        // Always hold the update UI information
                        // This way is necessary, because UI changes are not allowed from another thread
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                updateEverythingByFrame();
                                setSelectedFrame(selectedFrame + 1).execute();
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
            };
            updateLoop.start();
        });
        return playData;
    }

    public Command pauseData() {
        pauseData = runnableCommand(() -> {
            if (updateLoop != null) {
                updateLoop.interrupt();
            }
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
            // Make sure the selectedFrame cannot be out of bounds
            if (frame > data.size() - 1) {
                selectedFrame = data.size() - 1;
                updateLoop.interrupt();
            } else {
                // While loops necessary to ensure increments / decrements of one
                if (frame > selectedFrame) {
                    while (frame > selectedFrame) {
                        selectedFrame = Math.min(selectedFrame + 1, data.size() - 1);
                        updateEverythingByFrame();
                    }
                } else {
                    while (frame < selectedFrame) {
                        selectedFrame = Math.max(selectedFrame - 1, 0);
                        updateEverythingByFrame();
                    }
                }
            }

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

    public int getSelectedFrame() {
        return selectedFrame;
    }

    public ObservableList<Node> getMapElements() {
        ObservableList<Node> mapElementsList = FXCollections.observableArrayList();

        for (Pair<Node, Boolean> mapPair : mapElements.values()) {
            mapElementsList.add(mapPair.getKey());
        }
        return mapElementsList;
    }

    public SimpleIntegerProperty getFrameSliderMaxProperty() {
        return frameSliderMaxProperty;
    }

    public SimpleIntegerProperty getFrameSliderTickUnitProperty() {
        return frameSliderTickUnitProperty;
    }

    public SimpleDoubleProperty getFrameSliderValueProperty() {
        return frameSliderValueProperty;
    }

    public ObservableList<PlayerDataRow> getCurrentPlayerStats() {
        return currentPlayerStats;
    }

    public ObservableList<PlayerVisualsRow> getPlayerVisualsRows() {
        return playerVisualsRows;
    }

    public SimpleStringProperty getTotalTimeProperty() {
        return totalTimeProperty;
    }

    public SimpleStringProperty getRoundTimeProperty() {
        return roundTimeProperty;
    }

    public SimpleStringProperty getRoundProperty() {
        return roundProperty;
    }

    public SimpleStringProperty getHealthCoordsProperty() {
        return healthCoordsProperty;
    }

    public SimpleStringProperty getWeaponCoordsProperty() {
        return weaponCoordsProperty;
    }

    public SimpleStringProperty getAmmuCoordsProperty() {
        return ammuCoordsProperty;
    }

    public void setAmmuCoordsProperty(SimpleStringProperty ammuCoordsProperty) {
        this.ammuCoordsProperty = ammuCoordsProperty;
    }

    public SimpleDoubleProperty getDrawPanePositionXProperty() {
        return drawPanePositionXProperty;
    }

    public SimpleDoubleProperty getDrawPanePositionYProperty() {
        return drawPanePositionYProperty;
    }

    public Image getWeaponIcon() {
        return new Image(ConfigManager.IMAGE_PATH + "/weapon.png");
        // Analogous to map visuals does not work yet
        // return new Image(new
        // ByteArrayInputStream(file.getImages().getStaticObjects().get("M4a1")));
    }

    public Image getAmmuIcon() {
        return new Image(new ByteArrayInputStream(file.getImages().getStaticObjects().get("WeaponCrate")));
    }

    public Image getHealthIcon() {
        return new Image(new ByteArrayInputStream(file.getImages().getStaticObjects().get("Health")));
    }

    @Override
    public void initialize(ILiveViewable<? extends IStatistics> listener) {
        var concreteListener = (ILiveViewable<CBRShooterStatistics>) listener;
        concreteListener.addViewModel(this);

        initialize(concreteListener.getCurrentFile());
    }

    private int statisticsReceived = 0;

    @Override
    public void onStatisticsAdded(CBRShooterStatistics newStatistics, List<CBRShooterStatistics> statisticsCopy) {
        statisticsReceived += 1;
        data.add(newStatistics);

        if (statisticsReceived % 20 == 0)
            frameSliderMaxProperty.set(data.size() - 1);
        // TODO: If current frame is last, advance.
    }

    @Override
    public void onSessionClosed() {
    }

}
