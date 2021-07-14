package org.visab.newgui.visualize.cbrshooter.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.globalmodel.cbrshooter.PlayerInformation;
import org.visab.newgui.visualize.ReplayViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;
import org.visab.newgui.visualize.cbrshooter.model.PlayerDataRow;
import org.visab.newgui.visualize.cbrshooter.model.PlayerVisuals;
import org.visab.workspace.config.ConfigManager;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class CBRShooterReplayViewModel extends ReplayViewModelBase<CBRShooterFile> {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(CBRShooterMainViewModel.class);

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
    private ObservableList<Node> mapElements = FXCollections.observableArrayList();

    private SimpleStringProperty totalTimeProperty = new SimpleStringProperty();
    private SimpleStringProperty roundTimeProperty = new SimpleStringProperty();
    private SimpleStringProperty roundProperty = new SimpleStringProperty();
    private SimpleStringProperty healthCoordsProperty = new SimpleStringProperty();
    private SimpleStringProperty weaponCoordsProperty = new SimpleStringProperty();
    private SimpleStringProperty ammuCoordsProperty = new SimpleStringProperty();

    // Simply used to denote whether a player shall be visible on the map or not
    private HashMap<String, Boolean> showPlayers = new HashMap<String, Boolean>();

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
        updateCurrentGameStatsByFrame();

        // Dynamically map visuals for given player amount
        initializePlayerVisuals();

        initializeMapView();

        initializeShowPlayers();

        // Make the frame sliders values always reasonable according to shooter file
        frameSliderMaxProperty.set(data.size() - 1);

        var tickUnit = data.size() / 10;

        // Make sure it is at least 1,
        if (tickUnit == 0) {
            tickUnit = 1;
        }

        frameSliderTickUnitProperty.set(tickUnit);

        updateMapElements();
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
     * This method simply initializes the hash map that is used to decide whether
     * player visuals should be shown or hidden on the replay view.
     * 
     * The default case for each player is of course true.
     * 
     */
    private void initializeShowPlayers() {
        for (int i = 0; i < frameBasedStats.getPlayers().size(); i++) {
            showPlayers.put(frameBasedStats.getPlayers().get(i).getName(), true);
        }
    }

    /**
     * This method is used to update the visibility hash map value accordingly.
     * 
     * @param playerName the player name for which the visibility has changed.
     * @param show       indicates whether the player visuals should be seen or not.
     */
    public void updateShowPlayersMap(String playerName, boolean show) {
        showPlayers.put(playerName, show);
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

        // Update table with current stats based on the selected frame
        for (int i = 0; i < frameBasedStats.getPlayers().size(); i++) {
            PlayerDataRow row = new PlayerDataRow(frameBasedStats.getPlayers().get(i));
            row.setPlayerVisual(playerVisualsMap.get(row.getName()).getPlayerIcon());
            row.getShowCheckBox().setSelected(showPlayers.get(row.getName()));

            // Custom event handler that is capable of mapping the checkbox correctly
            row.getShowCheckBox().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    var box = (CheckBox) event.getSource();
                    var value = box.isSelected();
                    updateShowPlayersMap(row.getName(), value);
                    updateVisibilityForPlayer(row.getName());
                }
            });
            currentPlayerStats.add(i, row);
        }
    }

    /**
     * This method shows or hides player specific UI elements in the replay view
     * based on the information whether it should be visible or not.
     * 
     * @param playerName
     */
    private void updateVisibilityForPlayer(String playerName) {
        // TODO: All player related UI elements for a specific player shall be updated
        var visible = showPlayers.get(playerName);

        if (visible) {
            /*
             * Make all items visible -> Player icon -> Player Plan Change -> Player Path ->
             * Player Death
             */
        } else {
            // Make all items invisible
        }
    }

    private void initializeMapView() {

        // Map should always be contained in the elements exactly as it is
        ImageView map = new ImageView(new Image(ConfigManager.IMAGE_PATH + "fps_map.png"));
        map.setX(41);
        map.setY(143);
        map.setFitHeight(550);
        map.setFitWidth(550);

        // Putting one level higher, so it is always in the background
        map.setViewOrder(1);

        // First add the fixed items to the replay view
        ImageView health = new ImageView(new Image(ConfigManager.IMAGE_PATH + "healthContainer.png"));
        health.setX(170);
        health.setY(170);
        health.setFitHeight(16);
        health.setFitWidth(16);

        ImageView ammu = new ImageView(new Image(ConfigManager.IMAGE_PATH + "ammuContainer.png"));
        ammu.setX(80);
        ammu.setY(300);
        ammu.setFitHeight(16);
        ammu.setFitWidth(16);

        ImageView weapon = new ImageView(new Image(ConfigManager.IMAGE_PATH + "weapon.png"));
        weapon.setX(60);
        weapon.setY(250);
        weapon.setFitHeight(16);
        weapon.setFitWidth(16);

        mapElements.add(map);
        mapElements.add(health);
        mapElements.add(weapon);
        mapElements.add(ammu);

        // Then add the players dynamically, because there can be any amount
        for (String playerKey : playerVisualsMap.keySet()) {
            playerVisualsMap.get(playerKey).getPlayerIcon();
            mapElements.add(playerVisualsMap.get(playerKey).getPlayerIcon());
            mapElements.add(playerVisualsMap.get(playerKey).getPlayerDeath());
            mapElements.add(playerVisualsMap.get(playerKey).getPlayerPlanChange());
        }

    }

    /**
     * This method directly accesses the draw pane of the replay view and puts the
     * necessary elements on it to the correct places.
     * 
     */
    private void updateMapElements() {

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

    public ObservableList<Node> getMapElements() {
        return mapElements;
    }

    public void setMapElements(ObservableList<Node> mapElements) {
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
