package org.visab.processing.settlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.IMetaInformation;
import org.visab.globalmodel.IVISABFile;
import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.globalmodel.settlers.SettlersMapImage;
import org.visab.globalmodel.settlers.SettlersMetaInformation;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.newgui.UiHelper;
import org.visab.newgui.visualize.ILiveStatisticsViewModel;
import org.visab.processing.ILiveViewable;
import org.visab.processing.ReplaySessionListenerBase;
import org.visab.util.StringFormat;
import org.visab.workspace.config.ConfigManager;

/**
 * The SettlersListener class, that is responsible for listening to information
 * sent by the Unity Settlers of Catan game and creating files of that
 * information.
 *
 * @author leonr
 *
 */
public class SettlersListener extends ReplaySessionListenerBase<SettlersStatistics, SettlersMapImage>
        implements ILiveViewable<SettlersStatistics> {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(SettlersListener.class);

    private SettlersFile file;

    private List<ILiveStatisticsViewModel<SettlersStatistics>> viewModels = new ArrayList<>();

    public SettlersListener(UUID sessionId) {
        super(ConfigManager.SETTLERS_OF_CATAN_STRING, sessionId);
    }

    @Override
    public void addViewModel(ILiveStatisticsViewModel<SettlersStatistics> viewModel) {
        viewModels.add(viewModel);

        // If the session isnt active anymore, instantly notify, that it was closed.
        if (!isActive)
            notifySessionClosed();
    }

    @Override
    public List<SettlersStatistics> getReceivedStatistics() {
        // Return a copy to avoid concurrent modification
        return new ArrayList<SettlersStatistics>(file.getStatistics());
    }

    @Override
    public void notifySessionClosed() {
        for (var viewModel : viewModels)
            viewModel.notifySessionClosed();

        viewModels.clear();
    }

    @Override
    public void notifyStatisticsAdded(SettlersStatistics addedStatistics) {
        for (var viewModel : viewModels)
            UiHelper.inovkeOnUiThread(() -> viewModel.notifyStatisticsAdded(addedStatistics));
    }

    @Override
    public void onSessionClosed() {
        manager.saveFile(file, sessionId.toString(), sessionId);
    }

    @Override
    public void onSessionStarted(IMetaInformation metaInformation) {
        file = new SettlersFile();
        var asSettler = (SettlersMetaInformation) metaInformation;
        file.setMapRectangle(asSettler.getMapRectangle());
        file.setPlayerCount(asSettler.getPlayerCount());
        file.setPlayerInformation(asSettler.getPlayerInformation());
    }

    @Override
    public void processImage(SettlersMapImage mapImage) {
        // TODO Auto-generated method stub
    }

    @Override
    public void processStatistics(SettlersStatistics statistics) {
        file.getStatistics().add(statistics);

        writeLog(Level.DEBUG, StringFormat.niceString("has {0} entries now", file.getStatistics().size()));

        notifyStatisticsAdded(statistics);
    }

    @Override
    public IVISABFile getCurrentFile() {
        return file;
    }
}
