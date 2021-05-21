package org.visab.processing.cbrshooter;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterMapImage;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.processing.ReplaySessionListenerBase;
import org.visab.util.AssignByGame;
import org.visab.util.StringFormat;

/**
 * The CBRShooterListener class, that is responsible for listening information
 * sent by the CBRShooter game and creating files of that information.
 *
 * @author moritz
 *
 */
public class CBRShooterListener extends ReplaySessionListenerBase<CBRShooterStatistics, CBRShooterMapImage> {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(CBRShooterListener.class);

    private CBRShooterFile CBRShooterFile;

    public CBRShooterListener(UUID sessionId) {
        super(AssignByGame.CBR_SHOOTER_STRING, sessionId);
    }

    @Override
    public void onSessionClosed() {
        if (manager.saveFile(CBRShooterFile, sessionId.toString()))
            logger.info("Saved file in repository!");
        else
            logger.info("Couldn't save file in repository!");
    }

    @Override
    public void onSessionStarted() {
        CBRShooterFile = new CBRShooterFile();
    }

    @Override
    public void processStatistics(CBRShooterStatistics statistics) {
        CBRShooterFile.getStatistics().add(statistics);

        logger.debug(StringFormat.niceString("[Game: {0}, SessionId: {1}] has {2} entries now", getGame(),
                getSessionId(), CBRShooterFile.getStatistics().size()));
    }

    @Override
    public void processImage(CBRShooterMapImage mapImage) {
        // TODO Auto-generated method stub

    }
}
