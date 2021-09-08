package org.visab.gui.visualize.settlers.viewmodel;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.IStatistics;
import org.visab.globalmodel.Rectangle;
import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.gui.ResourceHelper;
import org.visab.gui.UiHelper;
import org.visab.gui.visualize.LiveVisualizeViewModelBase;
import org.visab.gui.visualize.settlers.model.Player;
import org.visab.processing.ILiveViewable;
import org.visab.util.StreamUtil;

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
import javafx.util.Pair;

public class SettlersReplayViewModel extends LiveVisualizeViewModelBase<SettlersFile, SettlersStatistics> {

    // Thread necessary to control data updating in the background
    private Thread updateLoop;

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

    private Map<String, Player> playersOfLastTurn = new HashMap<String, Player>();

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

        // Get reference of the last game stat for each player
        for (var name : file.getPlayerNames()) {
            Player visualizePlayer = new Player(name);
            visualizePlayer.initializeVisuals(getPlayerColors().get(name), getAnnotatedIconsForPlayer(name));
            var globalmodelPlayer = StreamUtil.firstOrNull(data.get(data.size() - 1).getPlayers(),
                    x -> x.getName().equals(name));
            visualizePlayer.updatePlayerData(globalmodelPlayer);
            playersOfLastTurn.put(name, visualizePlayer);
        }

        updateCurrentGameStatsByturn(0);

        // Add listener that will update the players and the general data
        currentturnProperty.addListener((o, oldValue, newValue) -> {
            updateCurrentGameStatsByturn(newValue.intValue());
            publish("DATA_UPDATED", new Object());
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
        playData = makeCommand(() -> {
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
        pauseData = makeCommand(() -> {
            if (updateLoop != null) {
                updateLoop.interrupt();
            }
        });
        return pauseData;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Map<String, Player> getPlayersOfLastTurn() {
        return playersOfLastTurn;
    }

    public Rectangle getMapRectangle() {
        return file.getMapRectangle();
    }

    public Image getMapImage() {
        return new Image(new ByteArrayInputStream(file.getImages().getMapImage()));
    }

    /**
     * This method returns the player color codes as JavaFX colors in a map.
     * 
     * @return the player color map.
     */
    public HashMap<String, Color> getPlayerColors() {
        HashMap<String, Color> playerColorMap = new HashMap<String, Color>();
        for (String playerName : file.getPlayerNames()) {
            playerColorMap.put(playerName, UiHelper.translateHexToRgbColor(file.getPlayerColors().get(playerName)));
        }
        return playerColorMap;
    }

    /**
     * This method returns all player-related icons correctly color coded and paired
     * with its annotation, because the raw visuals in Settlers of Catan are hardly
     * to differentiate in terms of shape.
     * 
     * @param playerName the name of the player the icons and annotations shall be
     *                   retrieved for.
     * @return the map of icons and annotations for a specific player.
     */
    public HashMap<String, Pair<Image, String>> getAnnotatedIconsForPlayer(String playerName) {
        Color playerColor = UiHelper.translateHexToRgbColor(file.getPlayerColors().get(playerName));
        HashMap<String, Pair<Image, String>> iconMap = new HashMap<String, Pair<Image, String>>();
        Image playerRoad = UiHelper.recolorImage(new Image(ResourceHelper.IMAGE_PATH + "settlersGenericVisual.png"),
                playerColor);
        Image playerVillage = UiHelper.recolorImage(new Image(ResourceHelper.IMAGE_PATH + "settlersGenericVisual.png"),
                playerColor);
        Image playerCity = UiHelper.recolorImage(new Image(ResourceHelper.IMAGE_PATH + "settlersGenericVisual.png"),
                playerColor);
        // Don't use because they scale very badly and are hard to distinguish from each
        // other
//        Image playerRoad = UiHelper.recolorImage(new Image(new ByteArrayInputStream(file.getImages().getStreetImage())),
//                playerColor);
//        Image playerVillage = UiHelper
//                .recolorImage(new Image(new ByteArrayInputStream(file.getImages().getVillageImage())), playerColor);
//
//        Image playerCity = UiHelper.recolorImage(new Image(new ByteArrayInputStream(file.getImages().getCityImage())),
//                playerColor);
        Pair<Image, String> roadPair = new Pair<Image, String>(playerRoad, file.getImages().getStreetAnnotation());
        Pair<Image, String> villagePair = new Pair<Image, String>(playerVillage,
                file.getImages().getVillageAnnotation());
        Pair<Image, String> cityPair = new Pair<Image, String>(playerCity, file.getImages().getCityAnnotation());

        iconMap.put("playerRoad", roadPair);
        iconMap.put("playerVillage", villagePair);
        iconMap.put("playerCity", cityPair);

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
    public void onStatisticsAdded(SettlersStatistics newStatistics) {
        statisticsReceived += 1;
        data.add(newStatistics);

        if (statisticsReceived % 5 == 0)
            turnSliderMaxProperty.set(data.size() - 1);
    }

}
