package org.visab.processing.cbrshooter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterMapImage;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.statistics.ILiveViewModel;
import org.visab.processing.ILiveViewable;
import org.visab.processing.ReplaySessionListenerBase;
import org.visab.util.AssignByGame;
import org.visab.util.StringFormat;

/**
 * The CBRShooterListener class, that is responsible for listening to
 * information sent by the CBRShooter game and creating files of that
 * information.
 *
 * @author moritz
 *
 */
public class CBRShooterListener extends ReplaySessionListenerBase<CBRShooterStatistics, CBRShooterMapImage>
        implements ILiveViewable<CBRShooterStatistics> {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(CBRShooterListener.class);

    private List<ILiveViewModel<CBRShooterStatistics>> viewModels = new ArrayList<>();

    private CBRShooterFile file;

    public CBRShooterListener(UUID sessionId) {
        super(AssignByGame.CBR_SHOOTER_STRING, sessionId);
    }

    @Override
    public void onSessionClosed() {
        manager.saveFile(file, sessionId.toString());
    }

    @Override
    public void onSessionStarted() {
        file = new CBRShooterFile();
    }

    @Override
    public void processStatistics(CBRShooterStatistics statistics) {
        file.getStatistics().add(statistics);

        logger.debug(StringFormat.niceString("[Game: {0}, SessionId: {1}] has {2} entries now.", getGame(),
                getSessionId(), file.getStatistics().size()));

        notifyStatisticsAdded(statistics);
    }

    @Override
    public void processImage(CBRShooterMapImage mapImage) {
        // TODO Auto-generated method stub
    }

    @Override
    public void addViewModel(ILiveViewModel<CBRShooterStatistics> viewModel) {
        viewModels.add(viewModel);

        // If the session isnt active anymore, instantly notify, that it was closed.
        if (!isActive)
            notifySessionClosed();
    }

    @Override
    public List<CBRShooterStatistics> getReceivedStatistics() {
        return file.getStatistics();
    }

    @Override
    public void notifyStatisticsAdded(CBRShooterStatistics addedStatistics) {
        for (var viewModel : viewModels)
            viewModel.notifyStatisticsAdded(addedStatistics);
    }

    @Override
    public void notifySessionClosed() {
        for (var viewModel : viewModels)
            viewModel.notifySessionClosed();

        viewModels.clear();
    }
}
