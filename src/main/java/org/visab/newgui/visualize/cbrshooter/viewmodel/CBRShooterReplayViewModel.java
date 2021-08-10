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
import org.visab.newgui.ResourceHelper;
import org.visab.newgui.UiHelper;
import org.visab.newgui.visualize.ILiveViewModel;
import org.visab.newgui.visualize.ReplayViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;
import org.visab.newgui.visualize.cbrshooter.model.DataUpdatedPayload;
import org.visab.newgui.visualize.cbrshooter.model.Player;
import org.visab.processing.ILiveViewable;
import org.visab.util.StreamUtil;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class CBRShooterReplayViewModel extends ReplayViewModelBase<CBRShooterFile>
        implements ILiveViewModel<CBRShooterStatistics> {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(CBRShooterReplayViewModel.class);

    @InjectScope
    VisualizeScope scope;

    // Controls that are responsible to handle the JavaFX control element actions
    private Command playData;
    private Command pauseData;

    // Thread necessary to control data updating in the background
    private Thread updateLoop;

    // Generic properties of the replay view to display information
    private IntegerProperty frameSliderMaxProperty = new SimpleIntegerProperty();
    private IntegerProperty frameSliderTickUnitProperty = new SimpleIntegerProperty();
    private IntegerProperty velocityProperty = new SimpleIntegerProperty(1);

    private StringProperty totalTimeProperty = new SimpleStringProperty();
    private StringProperty roundTimeProperty = new SimpleStringProperty();
    private IntegerProperty roundProperty = new SimpleIntegerProperty();
    private ObjectProperty<Vector2<Double>> healthCoordsProperty = new SimpleObjectProperty<Vector2<Double>>();
    private ObjectProperty<Vector2<Double>> weaponCoordsProperty = new SimpleObjectProperty<Vector2<Double>>();
    private ObjectProperty<Vector2<Double>> ammuCoordsProperty = new SimpleObjectProperty<Vector2<Double>>();

    // Used to control the speed in which the data is updated in the replay view
    private double updateInterval;

    // Contains all information that changes based on the frame
    private List<CBRShooterStatistics> data = new ArrayList<CBRShooterStatistics>();

    private IntegerProperty currentFrameProperty = new SimpleIntegerProperty();

    // Viewmodel always has a reference to the statistics of the current stat
    private ObjectProperty<CBRShooterStatistics> frameBasedStatsProperty = new SimpleObjectProperty<>();
    private List<Player> players = new ArrayList<>();

    public IntegerProperty currentFrameProperty() {
        return currentFrameProperty;
    }

    public Rectangle getMapRectangle() {
        return file.getMapRectangle();
    }

    public Image getMapImage() {
        return new Image(new ByteArrayInputStream(file.getImages().getMap()));
    }

    public Image getPlayerIcon() {
        // return new Image(ResourceHelper.IMAGE_PATH + "cbrBot.png");
        return new Image(new ByteArrayInputStream(file.getImages().getStaticObjects().get("Player")));
    }

    public List<String> getPlayerNames() {
        return file.getPlayerNames();
    }

    public IntegerProperty frameSliderMaxProperty() {
        return frameSliderMaxProperty;
    }

    public IntegerProperty frameSliderTickUnitProperty() {
        return frameSliderTickUnitProperty;
    }

    public IntegerProperty velocityProperty() {
        return velocityProperty;
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

    public ObjectProperty<Vector2<Double>> healthCoordsProperty() {
        return healthCoordsProperty;
    }

    public ObjectProperty<Vector2<Double>> weaponCoordsProperty() {
        return weaponCoordsProperty;
    }

    public ObjectProperty<Vector2<Double>> ammuCoordsProperty() {
        return ammuCoordsProperty;
    }

    public ObjectProperty<CBRShooterStatistics> frameBasedStatsProperty() {
        return frameBasedStatsProperty;
    }

    public List<Player> getPlayers() {
        return players;
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

        if (scope.isLive()) {
            initialize(scope.getSessionListener());
        } else {
            initialize(scope.getFile());
        }

        data = file.getStatistics();

        // Make the frame sliders values always reasonable according to shooter file
        frameSliderMaxProperty.set(data.size() - 1);

        var tickUnit = data.size() / 10;
        // Make sure it is at least 1,
        tickUnit = tickUnit < 1 ? 1 : tickUnit;
        frameSliderTickUnitProperty.set(tickUnit);

        for (var name : file.getPlayerNames())
            players.add(new Player(name));

        updateCurrentGameStatsByFrame(0);

        // Add listener that will update the players and the general data
        currentFrameProperty.addListener((o, oldValue, newValue) -> {
            var oldFrame = oldValue.intValue();
            var newFrame = newValue.intValue();

            var oldRound = data.get(oldFrame).getRound();
            var newRound = data.get(newFrame).getRound();

            updateCurrentGameStatsByFrame(newFrame);
            publish("DATA_UPDATED", new DataUpdatedPayload(oldFrame, newFrame, oldRound, newRound));
        });
    }

    public HashMap<String, Color> getPlayerColors() {
        HashMap<String, Color> playerColorMap = new HashMap<String, Color>();
        for (String playerName : file.getPlayerNames()) {
            playerColorMap.put(playerName, UiHelper.translateHexToRgbColor(file.getPlayerColors().get(playerName)));
        }
        return playerColorMap;
    }

    public List<Vector2<Double>> getPlayerPositionsForInterval(String playerName, int start, int end) {
        List<Vector2<Double>> positionList = new ArrayList<Vector2<Double>>();

        for (int i = start; i <= end; i++) {
            positionList.add(data.get(i).getInfoByPlayerName(playerName).getPosition());
        }

        return positionList;
    }

    public int getRoundStartIndex(int round) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getRound() == round) {
                return i;
            }
        }
        return 0;
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
    private void updateCurrentGameStatsByFrame(int frame) {
        var statistics = data.get(frame);

        // This object holds all information that is available
        frameBasedStatsProperty.set(statistics);

        totalTimeProperty.set(String.valueOf(statistics.getTotalTime()));
        roundTimeProperty.set(String.valueOf(statistics.getRoundTime()));
        roundProperty.set(statistics.getRound());
        healthCoordsProperty.set(statistics.getHealthPosition());
        weaponCoordsProperty.set(statistics.getWeaponPosition());
        ammuCoordsProperty.set(statistics.getAmmunitionPosition());

        for (var player : players) {
            var globalmodelPlayer = StreamUtil.firstOrNull(data.get(frame).getPlayers(),
                    x -> x.getName().equals(player.getName()));
            player.updatePlayerData(globalmodelPlayer);
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
                                currentFrameProperty.set(Math.min((currentFrameProperty.get() + 1), data.size() - 1));
                            }
                        });
                        // Sleeping time depends on the velocity sliders value
                        try {
                            sleep((long) updateInterval / velocityProperty.get());
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

    public HashMap<String, Image> getIconsForPlayer(String playerName) {
        Color playerColor = UiHelper.translateHexToRgbColor(file.getPlayerColors().get(playerName));
        HashMap<String, Image> iconMap = new HashMap<String, Image>();
        iconMap.put("playerIcon", UiHelper.recolorImage(getPlayerIcon(), playerColor));
        iconMap.put("playerPlanChange",
                UiHelper.recolorImage(new Image(ResourceHelper.IMAGE_PATH + "/playerPlanChange.png"), playerColor));
        iconMap.put("playerDeath",
                UiHelper.recolorImage(new Image(ResourceHelper.IMAGE_PATH + "/playerDeath.png"), playerColor));
        return iconMap;
    }

    public Image getWeaponIcon() {
        // return new Image(ResourceHelper.IMAGE_PATH + "/weapon.png");
        // Analogous to map visuals does not work yet
        return new Image(new ByteArrayInputStream(file.getImages().getStaticObjects().get("M4a1")));
    }

    public Image getAmmuIcon() {
        return new Image(new ByteArrayInputStream(file.getImages().getStaticObjects().get("WeaponCrate")));
    }

    public Image getHealthIcon() {
        return new Image(new ByteArrayInputStream(file.getImages().getStaticObjects().get("Health")));
    }

    public Vector2<Double> getLastPlanChangePositionForPlayer(String playerName, int frameIndex) {
        Vector2<Double> pos = new Vector2<Double>(0.0, 0.0);
        String planAtFrameIndex = data.get(frameIndex).getInfoByPlayerName(playerName).getPlan();
        for (int i = frameIndex; i >= 0; i--) {
            if (!data.get(i).getInfoByPlayerName(playerName).getPlan().equals(planAtFrameIndex)) {
                return data.get(i + 1).getInfoByPlayerName(playerName).getPosition();
            }
        }
        return pos;
    }

    public Vector2<Double> getLastDeathPositionForPlayer(String playerName, int frameIndex) {
        Vector2<Double> pos = new Vector2<Double>(0.0, 0.0);
        var deathsAtFrameIndex = data.get(frameIndex).getInfoByPlayerName(playerName).getStatistics().getDeaths();
        for (int i = frameIndex; i >= 0; i--) {
            if (data.get(i).getInfoByPlayerName(playerName).getStatistics().getDeaths() < deathsAtFrameIndex) {
                return data.get(i).getInfoByPlayerName(playerName).getPosition();
            }
        }
        return pos;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(ILiveViewable<? extends IStatistics> listener) {
        var concreteListener = (ILiveViewable<CBRShooterStatistics>) listener;
        concreteListener.addViewModel(this);

        initialize(concreteListener.getCurrentFile());
    }

    private int statisticsReceived = 0;

    @Override
    public void onStatisticsAdded(CBRShooterStatistics newStatistics) {
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
