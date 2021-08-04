package org.visab.newgui.visualize.cbrshooter.viewmodel;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.IStatistics;
import org.visab.globalmodel.IntVector2;
import org.visab.globalmodel.Rectangle;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.UiHelper;
import org.visab.newgui.visualize.ILiveViewModel;
import org.visab.newgui.visualize.ReplayViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;
import org.visab.newgui.visualize.cbrshooter.model.Player;
import org.visab.newgui.visualize.cbrshooter.model.PlayerDataRow;
import org.visab.newgui.visualize.cbrshooter.model.PlayerVisuals;
import org.visab.newgui.visualize.cbrshooter.model.PlayerVisualsRow;
import org.visab.processing.ILiveViewable;
import org.visab.workspace.config.ConfigManager;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
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
    private IntegerProperty frameSliderMaxProperty = new SimpleIntegerProperty();
    private IntegerProperty frameSliderTickUnitProperty = new SimpleIntegerProperty();

    private StringProperty totalTimeProperty = new SimpleStringProperty();
    private StringProperty roundTimeProperty = new SimpleStringProperty();
    private IntegerProperty roundProperty = new SimpleIntegerProperty();
    private ObjectProperty<IntVector2> healthCoordsProperty = new SimpleObjectProperty<IntVector2>();
    private ObjectProperty<IntVector2> weaponCoordsProperty = new SimpleObjectProperty<IntVector2>();
    private ObjectProperty<IntVector2> ammuCoordsProperty = new SimpleObjectProperty<IntVector2>();

    // Contains color-coded visuals for each player in the game
    private HashMap<String, PlayerVisuals> playerVisualsMap = new HashMap<String, PlayerVisuals>();
    private ObservableMap<String, Pair<Node, Boolean>> mapElements = FXCollections.observableHashMap();

    private ObservableMap<String, Player> playerObjects = FXCollections.observableHashMap();

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

    private IntegerProperty playFrameProperty = new SimpleIntegerProperty();

    public IntegerProperty playFrameProperty() {
        return playFrameProperty;
    }

    public Rectangle getMapRectangle() {
        return file.getMapRectangle();
    }

    public Image getMapImage() {
        return new Image(new ByteArrayInputStream(file.getImages().getMap()));
    }

    public List<String> getPlayerNames() {
        return file.getPlayerNames();
    }

    public CBRShooterStatistics getFrameBasedStats() {
        return data.get(playFrameProperty.get());
    }

    public IntegerProperty frameSliderMaxProperty() {
        return frameSliderMaxProperty;
    }

    public IntegerProperty frameSliderTickUnitProperty() {
        return frameSliderTickUnitProperty;
    }

    public StringProperty totalTimeProperty() {
        return totalTimeProperty;
    }

    public IntegerProperty roundProperty() {
        return roundProperty;
    }

    public StringProperty roundTimeProperty() {
        return roundTimeProperty;
    }

    public ObjectProperty<IntVector2> healthCoordsProperty() {
        return healthCoordsProperty;
    }

    public ObjectProperty<IntVector2> weaponCoordsProperty() {
        return weaponCoordsProperty;
    }

    public ObjectProperty<IntVector2> ammuCoordsProperty() {
        return ammuCoordsProperty;
    }

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
        mapRectangle = file.getMapRectangle();

        // Make the frame sliders values always reasonable according to shooter file
        frameSliderMaxProperty.set(data.size() - 1);

        var tickUnit = data.size() / 10;

        // Make sure it is at least 1,
        if (tickUnit == 0) {
            tickUnit = 1;
        }

        frameSliderTickUnitProperty.set(tickUnit);
    }

    public HashMap<String, Color> getPlayerColors() {
        HashMap<String, Color> playerColorMap = new HashMap<String, Color>();
        for (String playerName : file.getPlayerNames()) {
            playerColorMap.put(playerName, UiHelper.translateHexToRgbColor(file.getPlayerColors().get(playerName)));
        }
        return playerColorMap;
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
        System.out.println("Updating game stats in viewmodel.");
        // This object holds all information that is available
        frameBasedStats = data.get(playFrameProperty.get());

        // First, update data that is applicable for every player in the game
        totalTimeProperty.set(String.valueOf(frameBasedStats.getTotalTime()));
        roundTimeProperty.set(String.valueOf(frameBasedStats.getRoundTime()));
        roundProperty.set(frameBasedStats.getRound());
        healthCoordsProperty.set(frameBasedStats.getHealthPosition());
        weaponCoordsProperty.set(frameBasedStats.getWeaponPosition());
        ammuCoordsProperty.set(frameBasedStats.getAmmunitionPosition());
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
                                updateCurrentGameStatsByFrame();
                                playFrameProperty.set(Math.min((playFrameProperty.get() + 1), data.size() - 1));
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

    public int getSelectedFrame() {
        return selectedFrame;
    }

    public ObservableList<PlayerDataRow> getCurrentPlayerStats() {
        return currentPlayerStats;
    }

    public ObservableList<PlayerVisualsRow> getPlayerVisualsRows() {
        return playerVisualsRows;
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
