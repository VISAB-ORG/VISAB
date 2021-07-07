package org.visab.newgui.visualize.cbrshooter.viewmodel;

import java.util.ArrayList;
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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CBRShooterReplayViewModel extends ReplayViewModelBase<CBRShooterFile> {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(CBRShooterMainViewModel.class);

    @InjectScope
    VisualizeScope scope;

    private Command playData;

    private Command pauseData;

    private Command setUpdateInterval;

    private Command setSelectedFrame;

    private Thread updateLoop;

    private SimpleIntegerProperty frameSliderMaxProperty = new SimpleIntegerProperty();
    private SimpleIntegerProperty frameSliderTickUnitProperty = new SimpleIntegerProperty();
    private SimpleDoubleProperty frameSliderValueProperty = new SimpleDoubleProperty();
    private ObservableList<PlayerDataRow> currentPlayerStats = FXCollections.observableArrayList();

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

        setCurrentPlayerStatsByFrame();

        // Make the frame sliders values always reasonable according to shooter file
        frameSliderMaxProperty.set(data.size());
        frameSliderTickUnitProperty.set(data.size() / 10);
    }

    public ObservableList<PlayerDataRow> setCurrentPlayerStatsByFrame() {

        // Cleaning up the table before new values are put into it
        if (currentPlayerStats.size() > 0) {
            var lastIndex = currentPlayerStats.size() - 1;
            for (int i = lastIndex; i >= 0; i--) {
                currentPlayerStats.remove(i);
            }
        }

        // Update table with current stats based on the selected frame
        for (int i = 0; i < data.get(selectedFrame).getPlayers().size(); i++) {
            currentPlayerStats.add(i, new PlayerDataRow(data.get(selectedFrame).getPlayers().get(i)));
        }

        return currentPlayerStats;
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
                            // always hold the current playerstats cause they are bound to the table
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
}
