package org.visab.gui.visualize.settlers.viewmodel;

import java.time.LocalDateTime;
import java.util.List;

import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.gui.visualize.LiveVisualizeViewModelBase;
import org.visab.gui.visualize.PlayerInformation;
import org.visab.processing.ILiveViewable;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SettlersMetaViewModel extends LiveVisualizeViewModelBase<SettlersFile, SettlersStatistics> {

    private IntegerProperty roundsProperty;
    private ObservableList<PlayerInformation> playerInformation;
    private StringProperty winnerProperty;

    @SuppressWarnings("unchecked")
    public void initialize() {
        List<SettlersStatistics> statistics = null;
        if (scope.isLive()) {
            super.initialize(scope.getSessionListener());
            // Register ourselves, for when the view closes
            scope.registerOnStageClosing(s -> onSessionClosed());

            statistics = ((ILiveViewable<SettlersStatistics>) scope.getSessionListener()).getStatistics();
        } else {
            super.initialize(scope.getFile());
            statistics = file.getStatistics();
        }

        if (statistics.size() == 0)
            return;

        var lastStatistics = statistics.get(statistics.size() - 1);
        roundsProperty = new SimpleIntegerProperty(lastStatistics.getTurn());

        playerInformation = FXCollections.observableArrayList();
        for (var entry : file.getPlayerInformation().entrySet()) {
            var color = file.getPlayerColors().get(entry.getKey());
            playerInformation.add(new PlayerInformation(entry.getKey(), entry.getValue(), color));
        }

        winnerProperty = new SimpleStringProperty(file.getWinner());
    }

    public IntegerProperty getRoundsProperty() {
        return this.roundsProperty;
    }

    public LocalDateTime getCreationDate() {
        return file.getCreationDate();
    }

    public String getGame() {
        return file.getGame();
    }

    public String getFileFormatVersion() {
        return file.getFileFormatVersion();
    }

    public ObservableList<PlayerInformation> getPlayerInformation() {
        return playerInformation;
    }

    @Override
    public void onStatisticsAdded(SettlersStatistics newStatistics) {
        roundsProperty.set(newStatistics.getTurn());
    }

    public StringProperty winnerProperty() {
        return winnerProperty;
    }

}
