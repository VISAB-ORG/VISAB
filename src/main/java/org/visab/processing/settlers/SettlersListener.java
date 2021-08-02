package org.visab.processing.settlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.visab.globalmodel.GameName;
import org.visab.globalmodel.IVISABFile;
import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.globalmodel.settlers.SettlersImages;
import org.visab.globalmodel.settlers.SettlersMetaInformation;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.newgui.UiHelper;
import org.visab.newgui.visualize.ILiveViewModel;
import org.visab.processing.ILiveViewable;
import org.visab.processing.ReplaySessionListenerBase;
import org.visab.util.NiceString;

/**
 * The SettlersListener class, that is responsible for listening to information
 * sent by the Unity Settlers of Catan game and creating files of that
 * information.
 *
 * @author leonr
 *
 */
public class SettlersListener
        extends ReplaySessionListenerBase<SettlersMetaInformation, SettlersStatistics, SettlersImages>
        implements ILiveViewable<SettlersStatistics> {

    private SettlersFile file;

    private List<ILiveViewModel<SettlersStatistics>> viewModels = new ArrayList<>();

    public SettlersListener(UUID sessionId) {
        super(GameName.SETTLERS_OF_CATAN, sessionId);
    }

    @Override
    public void addViewModel(ILiveViewModel<SettlersStatistics> viewModel) {
        viewModels.add(viewModel);

        // If the session isnt active anymore, instantly notify, that it was closed.
        if (!isActive)
            notifySessionClosed();
    }

    @Override
    public List<SettlersStatistics> getStatisticsCopy() {
        // Return a copy to avoid concurrent modification
        return new ArrayList<SettlersStatistics>(file.getStatistics());
    }

    @Override
    public void notifySessionClosed() {
        for (var viewModel : viewModels)
            UiHelper.inovkeOnUiThread(() -> viewModel.onSessionClosed());

        viewModels.clear();
    }

    @Override
    public void notifyStatisticsAdded(SettlersStatistics addedStatistics) {
        for (var viewModel : viewModels)
            UiHelper.inovkeOnUiThread(() -> viewModel.onStatisticsAdded(addedStatistics, getStatisticsCopy()));
    }

    @Override
    public void onSessionClosed() {
        var lastStatistics = file.getStatistics().get(file.getStatistics().size() - 1);

        var playerName = "";
        for (var player : lastStatistics.getPlayers()) {
            if (player.getVictoryPoints() == 10)
                playerName = player.getName();
        }
        file.setWinner(playerName);

        manager.saveFile(file, sessionId.toString(), sessionId);

        notifySessionClosed();
    }

    @Override
    public void onSessionStarted(SettlersMetaInformation metaInformation) {
        file = new SettlersFile();
        file.setMapRectangle(metaInformation.getMapRectangle());
        file.setPlayerCount(metaInformation.getPlayerCount());
        file.setPlayerInformation(metaInformation.getPlayerInformation());
        file.setPlayerColors(metaInformation.getPlayerColors());
    }

    @Override
    public void processImage(SettlersImages mapImage) {
        // TODO Auto-generated method stub
    }

    @Override
    public void processStatistics(SettlersStatistics statistics) {
        file.getStatistics().add(statistics);

        writeLog(Level.DEBUG, NiceString.make("has {0} entries now", file.getStatistics().size()));

        notifyStatisticsAdded(statistics);
    }

    @Override
    public IVISABFile getCurrentFile() {
        return file;
    }

    @Override
    public void removeViewModel(ILiveViewModel<SettlersStatistics> viewModel) {
        viewModels.remove(viewModel);
    }
}
