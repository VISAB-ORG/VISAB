package org.visab.newgui.visualize.settlers.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.IStatistics;
import org.visab.globalmodel.Rectangle;
import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.newgui.ResourceHelper;
import org.visab.newgui.UiHelper;
import org.visab.newgui.visualize.ILiveViewModel;
import org.visab.newgui.visualize.ReplayViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;
import org.visab.newgui.visualize.settlers.model.Player;
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

public class SettlersReplayViewModel extends ReplayViewModelBase<SettlersFile>
        implements ILiveViewModel<SettlersStatistics> {

    // Thread necessary to control data updating in the background
    private Thread updateLoop;

    @InjectScope
    VisualizeScope scope;

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(SettlersReplayViewModel.class);

    // Controls that are responsible to handle the JavaFX control element actions
    private Command playData;
    private Command pauseData;

    // Used to control the speed in which the data is updated in the replay view
    private double updateInterval;

    // Generic properties of the replay view to display information
    private IntegerProperty turnSliderMaxProperty = new SimpleIntegerProperty();
    private IntegerProperty turnSliderTickUnitProperty = new SimpleIntegerProperty();
    private IntegerProperty velocityProperty = new SimpleIntegerProperty(1);

    private IntegerProperty turnProperty = new SimpleIntegerProperty();
    private StringProperty turnTimeStampProperty = new SimpleStringProperty();
    private IntegerProperty diceNumberRolledProperty = new SimpleIntegerProperty();

    // Contains all information that changes based on the turn
    private List<SettlersStatistics> data = new ArrayList<SettlersStatistics>();

    private IntegerProperty currentturnProperty = new SimpleIntegerProperty();

    // Viewmodel always has a reference to the statistics of the current stat
    private ObjectProperty<SettlersStatistics> turnBasedStatsProperty = new SimpleObjectProperty<>();
    private List<Player> players = new ArrayList<>();

    public void initialize() {

        // Update loop eventually needs to be stopped on stage close
        scope.registerOnStageClosing(stage -> {
            if (updateLoop != null) {
                updateLoop.interrupt();
            }
        });

        // Default update interval of 1 second
        updateInterval = 1000;

        if (scope.isLive()) {
            initialize(scope.getSessionListener());
        } else {
            initialize(scope.getFile());
        }

        data = file.getStatistics();

        // Make the turn sliders values always reasonable according to shooter file
        turnSliderMaxProperty.set(data.size() - 1);

        var tickUnit = data.size() / 10;
        // Make sure it is at least 1,
        tickUnit = tickUnit < 1 ? 1 : tickUnit;
        turnSliderTickUnitProperty.set(tickUnit);

        for (var name : file.getPlayerNames())
            players.add(new Player(name));

        updateCurrentGameStatsByturn(0);

        // Add listener that will update the players and the general data
        currentturnProperty.addListener((o, oldValue, newValue) -> {
            updateCurrentGameStatsByturn(newValue.intValue());
        });

    }

    /**
     * This method is responsible to update the given statistics according to the
     * global variable of the currently selected turn based on various types of
     * inputs or events.
     * 
     * The view model always refers to a specific index of the overall statistics
     * that have been loaded from the file to properly display the relevant
     * information on the underlying UI of the CBR Shooter visualizer.
     * 
     */
    private void updateCurrentGameStatsByturn(int turn) {
        var statistics = data.get(turn);

        // This object holds all information that is available
        turnBasedStatsProperty.set(statistics);

        turnProperty.set(statistics.getTurn());
        turnTimeStampProperty.set(statistics.getTurnTimeStamp());
        diceNumberRolledProperty.set(statistics.getDiceNumberRolled());

        for (var player : players) {
            var globalmodelPlayer = StreamUtil.firstOrNull(data.get(turn).getPlayers(),
                    x -> x.getName().equals(player.getName()));
            player.updatePlayerData(globalmodelPlayer);
        }
    }

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
                                currentturnProperty.set(Math.min((currentturnProperty.get() + 1), data.size() - 1));
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

    public List<Player> getPlayers() {
        return players;
    }

    public Rectangle getMapRectangle() {
        return file.getMapRectangle();
    }

    public Image getMapImage() {
        return new Image(ResourceHelper.IMAGE_PATH + "fps_map.png");
        // Not in here yet
        // return new Image(new ByteArrayInputStream(file.getImages().getMap()));
    }

    public HashMap<String, Color> getPlayerColors() {
        HashMap<String, Color> playerColorMap = new HashMap<String, Color>();
        for (String playerName : file.getPlayerNames()) {
            playerColorMap.put(playerName, UiHelper.translateHexToRgbColor(file.getPlayerColors().get(playerName)));
        }
        return playerColorMap;
    }

    public HashMap<String, Image> getIconsForPlayer(String playerName) {
        // TODO: Later on replace with original visuals of the map --- only placeholders
        Color playerColor = UiHelper.translateHexToRgbColor(file.getPlayerColors().get(playerName));
        HashMap<String, Image> iconMap = new HashMap<String, Image>();
        iconMap.put("playerRoad",
                UiHelper.recolorImage(new Image(ResourceHelper.IMAGE_PATH + "/weapon.png"), playerColor));
        iconMap.put("playerVillage",
                UiHelper.recolorImage(new Image(ResourceHelper.IMAGE_PATH + "/playerPlanChange.png"), playerColor));
        iconMap.put("playerCity",
                UiHelper.recolorImage(new Image(ResourceHelper.IMAGE_PATH + "/playerDeath.png"), playerColor));
        return iconMap;
    }

    public ObjectProperty<SettlersStatistics> turnBasedStatsProperty() {
        return turnBasedStatsProperty;
    }

    public IntegerProperty turnSliderMaxProperty() {
        return turnSliderMaxProperty;
    }

    public IntegerProperty turnSliderTickUnitProperty() {
        return turnSliderTickUnitProperty;
    }

    public IntegerProperty diceNumberRolledProperty() {
        return diceNumberRolledProperty;
    }

    public IntegerProperty currentturnProperty() {
        return currentturnProperty;
    }

    public IntegerProperty velocityProperty() {
        return velocityProperty;
    }

    public IntegerProperty turnProperty() {
        return turnProperty;
    }

    public StringProperty turnTimeStampProperty() {
        return turnTimeStampProperty;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(ILiveViewable<? extends IStatistics> listener) {
        var concreteListener = (ILiveViewable<SettlersStatistics>) listener;
        concreteListener.addViewModel(this);

        initialize(concreteListener.getCurrentFile());

    }

    private int statisticsReceived = 0;

    @Override
    public void onStatisticsAdded(SettlersStatistics newStatistics, List<SettlersStatistics> statisticsCopy) {
        statisticsReceived += 1;
        data.add(newStatistics);

        if (statisticsReceived % 5 == 0)
            turnSliderMaxProperty.set(data.size() - 1);
        // TODO: If current turn is last, advance.

    }

    @Override
    public void onSessionClosed() {
        // TODO Auto-generated method stub

    }

}
