package org.visab.newgui.visualize.cbrshooter.viewmodel;

import java.time.LocalDateTime;
import java.util.Map;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.globalmodel.cbrshooter.WeaponInformation;
import org.visab.newgui.visualize.LiveViewModelBase;
import org.visab.newgui.visualize.cbrshooter.model.PlayerInformation;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

public class CBRShooterMetaViewModel extends LiveViewModelBase<CBRShooterFile, CBRShooterStatistics> {

    private IntegerProperty roundsProperty = new SimpleIntegerProperty();
    private FloatProperty statisticsPerSecondProperty = new SimpleFloatProperty();
    private DoubleProperty ingameTimeProperty = new SimpleDoubleProperty();
    private ObservableList<WeaponInformation> weaponInformation;
    private ObservableList<PlayerInformation> playerInformation;

    public void initialize() {
        if (scope.isLive()) {
            super.initializeLive(scope.getSessionListener());
        } else {
            super.initialize(scope.getFile());
        }

        var statistics = file.getStatistics();
        if (statistics.size() == 0)
            return;

        var lastStatistics = statistics.get(statistics.size() - 1);
        roundsProperty.set(lastStatistics.getRound());
        ingameTimeProperty.set(lastStatistics.getTotalTime());
        statisticsPerSecondProperty.set(statistics.size() / lastStatistics.getTotalTime());

        weaponInformation.addAll(file.getWeaponInformation());

        for (var entry : file.getPlayerInformation().entrySet())
            playerInformation.add(new PlayerInformation(entry.getKey(), entry.getValue()));

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
    public void onStatisticsAdded(CBRShooterStatistics newStatistics) {
        roundsProperty.set(newStatistics.getRound());
        ingameTimeProperty.set(newStatistics.getTotalTime());
        statisticsPerSecondProperty.set(file.getStatistics().size() / newStatistics.getTotalTime());
    }

    @Override
    public void onSessionClosed() {
        // TODO Auto-generated method stub
        listener.removeViewModel(this);
    }

}
