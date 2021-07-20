package org.visab.newgui.visualize.cbrshooter.viewmodel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.globalmodel.cbrshooter.WeaponInformation;
import org.visab.newgui.visualize.LiveViewModelBase;
import org.visab.newgui.visualize.cbrshooter.model.PlayerInformation;
import org.visab.processing.ILiveViewable;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CBRShooterMetaViewModel extends LiveViewModelBase<CBRShooterFile, CBRShooterStatistics> {

    private IntegerProperty roundsProperty;
    private FloatProperty statisticsPerSecondProperty;
    private DoubleProperty ingameTimeProperty;
    private ObservableList<WeaponInformation> weaponInformation;
    private ObservableList<PlayerInformation> playerInformation;

    public void initialize() {
        List<CBRShooterStatistics> statistics = null;
        if (scope.isLive()) {
            super.initializeLive(scope.getSessionListener());
            // Register ourselves, for when the view closes
            scope.registerOnStageClosing(s -> onSessionClosed());

            statistics = ((ILiveViewable<CBRShooterStatistics>) scope.getSessionListener()).getStatisticsCopy();
        } else {
            super.initialize(scope.getFile());
            statistics = file.getStatistics();
        }

        if (statistics.size() == 0)
            return;

        var lastStatistics = statistics.get(statistics.size() - 1);
        roundsProperty = new SimpleIntegerProperty(lastStatistics.getRound());
        ingameTimeProperty = new SimpleDoubleProperty(lastStatistics.getTotalTime());
        statisticsPerSecondProperty = new SimpleFloatProperty(statistics.size() / lastStatistics.getTotalTime());

        weaponInformation = FXCollections.observableArrayList(file.getWeaponInformation());

        playerInformation = FXCollections.observableArrayList();
        for (var entry : file.getPlayerInformation().entrySet()) {
            var color = file.getPlayerColors().get(entry.getKey());
            playerInformation.add(new PlayerInformation(entry.getKey(), entry.getValue(), color));
        }
    }

    public IntegerProperty getRoundsProperty() {
        return this.roundsProperty;
    }

    public FloatProperty getStatisticsPerSecondProperty() {
        return this.statisticsPerSecondProperty;
    }

    public DoubleProperty getIngameTimeProperty() {
        return this.ingameTimeProperty;
    }

    public void setIngameTimeProperty(DoubleProperty ingameTimeProperty) {
        this.ingameTimeProperty = ingameTimeProperty;
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

    public Float getGameSpeed() {
        return file.getGameSpeed();
    }

    public ObservableList<WeaponInformation> getWeaponInformation() {
        return weaponInformation;
    }

    public ObservableList<PlayerInformation> getPlayerInformation() {
        return playerInformation;
    }

    @Override
    public void onStatisticsAdded(CBRShooterStatistics newStatistics, List<CBRShooterStatistics> statisticsCopy) {
        roundsProperty.set(newStatistics.getRound());
        ingameTimeProperty.set(newStatistics.getTotalTime());
        statisticsPerSecondProperty.set(statisticsCopy.size() / newStatistics.getTotalTime());
    }

    @Override
    public void onSessionClosed() {
        if (listener != null)
            listener.removeViewModel(this);
    }

}
