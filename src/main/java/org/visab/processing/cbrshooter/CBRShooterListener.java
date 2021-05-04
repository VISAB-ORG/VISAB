package org.visab.processing.cbrshooter;

import java.text.MessageFormat;
import java.util.UUID;

import org.visab.processing.UnitySessionListenerBase;
import org.visab.processing.cbrshooter.model.CBRShooterStatistics;
import org.visab.util.AssignByGame;

/**
 * The CBRShooterListener class, that is responsible for listening information
 * sent by the CBRShooter game and creating files of that information.
 *
 * @author moritz
 *
 */
public class CBRShooterListener extends UnitySessionListenerBase<CBRShooterStatistics, CBRShooterMapImage, CBRShooterMapInformation> {

    private CBRShooterFile CBRShooterFile;

    public CBRShooterListener(UUID sessionId) {
        super(AssignByGame.CBR_SHOOTER_STRING, sessionId);
    }

    @Override
	public void onSessionClosed() {
        if (repo.saveFile(CBRShooterFile))
            System.out.println("Saved file in repository!");
        else
            System.out.print("Couldent save file in repository!");
	}

    @Override
    public void onSessionStarted() {
        CBRShooterFile = new CBRShooterFile(getSessionId().toString());
    }

    @Override
    public void processStatistics(CBRShooterStatistics statistics) {
        CBRShooterFile.getStatistics().add(statistics);

        System.out.println(MessageFormat.format("[Game: {0}, SessionId: {1}] has {2} entries now.", getGame(),
                getSessionId(), CBRShooterFile.getStatistics().size()));
    }

    @Override
    public void processMapImage(CBRShooterMapImage mapImage) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void processMapInformation(CBRShooterMapInformation mapInformation) {
        // TODO Auto-generated method stub
        
    }

}
