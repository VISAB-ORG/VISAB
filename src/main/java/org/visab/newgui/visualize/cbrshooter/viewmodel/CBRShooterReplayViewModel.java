package org.visab.newgui.visualize.cbrshooter.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.visualize.ReplayViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.utils.commands.Command;

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
        // TODO: Add necessary logic
        // Load all data from the respective file
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
                    for (int i = 0; i < data.size(); i++) {
                        if (!this.isInterrupted()) {
                            System.out.println("Updated data to:");

                            CBRShooterStatistics stat = data.get(i);

                            System.out.println(stat.getPlayers().get(0).getName());
                            System.out.println(stat.getPlayers().get(0).getPosition().getX() + " "
                                    + stat.getPlayers().get(0).getPosition().getY());

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
}
