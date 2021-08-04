package org.visab.processing.cbrshooter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.visab.globalmodel.GameName;
import org.visab.globalmodel.IVISABFile;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterImages;
import org.visab.globalmodel.cbrshooter.CBRShooterMetaInformation;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.UiHelper;
import org.visab.newgui.visualize.ILiveViewModel;
import org.visab.processing.ILiveViewable;
import org.visab.processing.ReplaySessionListenerBase;
import org.visab.util.NiceString;

/**
 * The CBRShooterListener class, that is responsible for listening to
 * information sent by the CBRShooter game and creating files with that
 * information.
 */
public class CBRShooterListener
        extends ReplaySessionListenerBase<CBRShooterMetaInformation, CBRShooterStatistics, CBRShooterImages>
        implements ILiveViewable<CBRShooterStatistics> {

    private CBRShooterFile file;

    private List<ILiveViewModel<CBRShooterStatistics>> viewModels = new ArrayList<>();

    public CBRShooterListener(UUID sessionId) {
        super(GameName.CBR_SHOOTER, sessionId);
    }

    @Override
    public void addViewModel(ILiveViewModel<CBRShooterStatistics> viewModel) {
        viewModels.add(viewModel);

        // If the session isnt active anymore, instantly notify that it was closed.
        if (!isActive)
            notifySessionClosed();
    }

    @Override
    public List<CBRShooterStatistics> getStatisticsCopy() {
        // Return a copy to avoid concurrent modification
        return new ArrayList<CBRShooterStatistics>(file.getStatistics());
    }

    @Override
    public void notifySessionClosed() {
        for (var viewModel : viewModels)
            UiHelper.inovkeOnUiThread(() -> viewModel.onSessionClosed());

        viewModels.clear();
    }

    @Override
    public void notifyStatisticsAdded(CBRShooterStatistics addedStatistics) {
        for (var viewModel : viewModels)
            UiHelper.inovkeOnUiThread(() -> viewModel.onStatisticsAdded(addedStatistics, getStatisticsCopy()));
    }

    @Override
    public void onSessionClosed() {
        if (file.getStatistics().size() > 0) {
            var lastStatistics = file.getStatistics().get(file.getStatistics().size() - 1);

            int mostKills = 0;
            var playerName = "";
            for (var player : lastStatistics.getPlayers()) {
                if (player.getStatistics().getFrags() > mostKills) {
                    playerName = player.getName();
                    mostKills = player.getStatistics().getFrags();
                }
            }
            file.setWinner(playerName);
        }

        manager.saveFile(file, sessionId.toString(), sessionId);

        notifySessionClosed();
    }

    @Override
    public void onSessionStarted(CBRShooterMetaInformation metaInformation) {
        file = new CBRShooterFile();
        file.setGameSpeed(metaInformation.getGameSpeed());
        file.setMapRectangle(metaInformation.getMapRectangle());
        file.setPlayerCount(metaInformation.getPlayerCount());
        file.getPlayerInformation().putAll(metaInformation.getPlayerInformation());
        file.getWeaponInformation().addAll(metaInformation.getWeaponInformation());
        file.getPlayerColors().putAll(metaInformation.getPlayerColors());
    }

    @Override
    public void processImage(CBRShooterImages mapImage) {
        writeLog(Level.DEBUG, "Received images");
        file.setImages(mapImage);
    }

    @Override
    public void processStatistics(CBRShooterStatistics statistics) {
        file.getStatistics().add(statistics);

        writeLog(Level.DEBUG, NiceString.make("has {0} entries now", file.getStatistics().size()));

        notifyStatisticsAdded(statistics);
    }

    @Override
    public IVISABFile getCurrentFile() {
        return file;
    }

    @Override
    public void removeViewModel(ILiveViewModel<CBRShooterStatistics> viewModel) {
        viewModels.remove(viewModel);
    }
}
