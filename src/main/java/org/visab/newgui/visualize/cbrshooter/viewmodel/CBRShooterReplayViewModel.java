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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

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
    private ObservableList<PlayerDataRow> currentPlayerStats = FXCollections.observableArrayList();

    // Used to control the speed in which the data is updated in the replay view
    private double updateInterval;
    private int selectedFrame;

    private List<CBRShooterStatistics> data = new ArrayList<CBRShooterStatistics>();

    /**
     * Called after the instance was constructed by javafx/mvvmfx.
     */
    public void initialize() {
        updateInterval = 1000;
        selectedFrame = 1;

        CBRShooterFile file = (CBRShooterFile) scope.getFile();
        data = file.getStatistics();

        setCurrentPlayerStatsByFrame(currentPlayerStats);
        // TODO: Add necessary logic
        // Load all data from the respective file
        // Initialize frame slider
        frameSliderMaxProperty.set(data.size());
        frameSliderTickUnitProperty.set(data.size() / 10);
    }

    public ObservableList<PlayerDataRow> setCurrentPlayerStatsByFrame(
            ObservableList<PlayerDataRow> currentPlayerStats) {

        currentPlayerStats.removeAll();

        for (int i = 0; i < data.get(selectedFrame).getPlayers().size(); i++) {
            currentPlayerStats.add(i, new PlayerDataRow(data.get(selectedFrame).getPlayers().get(i)));
        }

        return currentPlayerStats;
    }

    // ----- Command methods -----
    public Command playData() {
        playData = runnableCommand(() -> {
            logger.info("Pressed play button.");
            // Start this as a thread to provide the possibility of interrupting it on pause
            updateLoop = new Thread() {
                @Override
                public void run() {
                    // Iterate over frames and constantly update data
                    for (int i = selectedFrame; i < data.size(); i++) {
                        if (!this.isInterrupted()) {
                            setCurrentPlayerStatsByFrame(currentPlayerStats);
                            System.out.println("Updated data to:");

                            CBRShooterStatistics stat = data.get(i);

                            System.out.println("Frame: " + i);
                            System.out.println(stat.getPlayers().get(0).getName());
                            System.out.println(stat.getPlayers().get(0).getPosition().getX() + " "
                                    + stat.getPlayers().get(0).getPosition().getY());
                            selectedFrame++;
                            try {
                                System.out.println("Sleeping: " + updateInterval);
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
        logger.info("Pressed pause button.");
        pauseData = runnableCommand(() -> {
            System.out.println("Interrupting update loop.");
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
        });
        return setSelectedFrame;
    }

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

    public ObservableList<PlayerDataRow> getCurrentPlayerStats() {
        return currentPlayerStats;
    }

    public void setCurrentPlayerStats(ObservableList<PlayerDataRow> currentPlayerStats) {
        this.currentPlayerStats = currentPlayerStats;
    }

    public void initializePlayerDataTable(TableView<PlayerDataRow> playerDataTable) {

        CBRShooterStatistics currentFrameStats = data.get(selectedFrame);

        for (int i = 0; i < currentFrameStats.getPlayers().size(); i++) {
            playerDataTable.getItems().add(i, new PlayerDataRow(currentFrameStats.getPlayers().get(i)));
        }
    }

}
