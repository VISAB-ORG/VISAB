package org.visab.processing.cbrshooter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.IMetaInformation;
import org.visab.globalmodel.IVISABFile;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterMapImage;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.UiHelper;
import org.visab.newgui.visualize.ILiveViewModel;
import org.visab.processing.ILiveViewable;
import org.visab.processing.ReplaySessionListenerBase;
import org.visab.util.StringFormat;
import org.visab.workspace.config.ConfigManager;

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

    private CBRShooterFile file;

    private List<ILiveViewModel<CBRShooterStatistics>> viewModels = new ArrayList<>();

    public CBRShooterListener(UUID sessionId) {
        super(ConfigManager.CBR_SHOOTER_STRING, sessionId);
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
        // Return a copy to avoid concurrent modification
        return new ArrayList<CBRShooterStatistics>(file.getStatistics());
    }

    @Override
    public void notifySessionClosed() {
        for (var viewModel : viewModels)
            viewModel.notifySessionClosed();

        viewModels.clear();
    }

    @Override
    public void notifyStatisticsAdded(CBRShooterStatistics addedStatistics) {
        for (var viewModel : viewModels)
            UiHelper.inovkeOnUiThread(() -> viewModel.notifyStatisticsAdded(addedStatistics));
    }

    @Override
    public void onSessionClosed() {
        manager.saveFile(file, sessionId.toString(), sessionId);
    }

    @Override
    public void onSessionStarted(IMetaInformation metaInformation) {
        file = new CBRShooterFile();
        // TODO: Add meta information to the file.
    }

    @Override
    public void processImage(CBRShooterMapImage mapImage) {
        // TODO Auto-generated method stub
    }

    @Override
    public void processStatistics(CBRShooterStatistics statistics) {
        file.getStatistics().add(statistics);

        writeLog(Level.DEBUG, StringFormat.niceString("has {0} entries now", file.getStatistics().size()));

        notifyStatisticsAdded(statistics);
    }

    @Override
    public IVISABFile getCurrentFile() {
        return file;
    }
}
