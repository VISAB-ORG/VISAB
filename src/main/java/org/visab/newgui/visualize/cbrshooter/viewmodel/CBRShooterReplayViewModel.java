package org.visab.newgui.visualize.cbrshooter.viewmodel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.newgui.ViewModelBase;
import org.visab.newgui.visualize.VisualizeScope;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.utils.commands.Command;

public class CBRShooterReplayViewModel extends ViewModelBase {

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

    /**
     * Called after the instance was constructed by javafx/mvvmfx.
     */
    public void initialize() {
        updateInterval = 1000;
        selectedFrame = 1;
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
                    for (int i = 0; i < 100; i++) {
                        if (!this.isInterrupted()) {
                            System.out.println("Updated data");
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
