package org.visab.newgui.visualize.cbrshooter.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.visualize.ReplayViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;
import org.visab.newgui.visualize.cbrshooter.model.PlayerDataRow;

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
import javafx.scene.control.CheckBox;

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

    private SimpleStringProperty totalTimeProperty = new SimpleStringProperty();
    private SimpleStringProperty roundTimeProperty = new SimpleStringProperty();
    private SimpleStringProperty roundProperty = new SimpleStringProperty();
    private SimpleStringProperty healthCoordsProperty = new SimpleStringProperty();
    private SimpleStringProperty weaponCoordsProperty = new SimpleStringProperty();
    private SimpleStringProperty ammuCoordsProperty = new SimpleStringProperty();

    private HashMap<String, Boolean> showPlayers = new HashMap<String, Boolean>();

    // Used to control the speed in which the data is updated in the replay view
    private double updateInterval;
    private int selectedFrame;

    private List<CBRShooterStatistics> data = new ArrayList<CBRShooterStatistics>();

    /**
     * Called after the instance was constructed by javafx/mvvmfx.
     */
    public void initialize() {
        // Default update interval of 1 second
        updateInterval = 1000;

        selectedFrame = 1;

        // Load data from the scopes file which is initialized after VISUALIZE
        CBRShooterFile file = (CBRShooterFile) scope.getFile();
        data = file.getStatistics();

        initializeShowPlayers();

        setCurrentOverallStatsByFrame();

        setCurrentPlayerStatsByFrame();

        // Make the frame sliders values always reasonable according to shooter file
        frameSliderMaxProperty.set(data.size());
        frameSliderTickUnitProperty.set(data.size() / 10);
    }

    private void initializeShowPlayers() {
        for (int i = 0; i < data.get(1).getPlayers().size(); i++) {
            System.out.println(
                    "Putting visibility entry into map: " + data.get(1).getPlayers().get(i).getName() + " ," + true);
            showPlayers.put(data.get(1).getPlayers().get(i).getName(), true);
        }
    }

    public void updateShowPlayers(String playerName, boolean show) {
        showPlayers.put(playerName, show);
    }

    public void setCurrentOverallStatsByFrame() {
        totalTimeProperty.set(String.valueOf(data.get(selectedFrame).getTotalTime()));
        roundTimeProperty.set(String.valueOf(data.get(selectedFrame).getRoundTime()));
        roundProperty.set(String.valueOf(data.get(selectedFrame).getRound()));
        healthCoordsProperty.set(data.get(selectedFrame).getHealthPosition().getX() + ", "
                + data.get(selectedFrame).getHealthPosition().getY());
        weaponCoordsProperty.set(data.get(selectedFrame).getWeaponPosition().getX() + ", "
                + data.get(selectedFrame).getWeaponPosition().getY());
        ammuCoordsProperty.set(data.get(selectedFrame).getAmmunitionPosition().getX() + ", "
                + data.get(selectedFrame).getAmmunitionPosition().getY());

    }

    public void setCurrentPlayerStatsByFrame() {

        // Cleaning up the table before new values are put into it
        if (currentPlayerStats.size() > 0) {
            var lastIndex = currentPlayerStats.size() - 1;
            for (int i = lastIndex; i >= 0; i--) {
                currentPlayerStats.remove(i);
            }
        }

        // Update table with current stats based on the selected frame
        for (int i = 0; i < data.get(selectedFrame).getPlayers().size(); i++) {
            PlayerDataRow row = new PlayerDataRow(data.get(selectedFrame).getPlayers().get(i));
            row.getShowCheckBox().setSelected(showPlayers.get(row.getName()));
            row.getShowCheckBox().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Checkbox clicked for player: " + row.getName());
                    var box = (CheckBox) event.getSource();
                    var id = box.getId();
                    var value = box.isSelected();
                    updateShowPlayers(row.getName(), value);

                }
            });
            currentPlayerStats.add(i, new PlayerDataRow(data.get(selectedFrame).getPlayers().get(i)));
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
                                    setCurrentOverallStatsByFrame();
                                }
                            });
                            setCurrentPlayerStatsByFrame();
                            frameSliderValueProperty.set(selectedFrame);
                            selectedFrame++;

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
            setCurrentPlayerStatsByFrame();

            // TODO: Also call the draw map function here
        });
        return setSelectedFrame;
    }

    // --- Property getters and setters ---

    public SimpleIntegerProperty getFrameSliderMaxProperty() {
        return frameSliderMaxProperty;
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
