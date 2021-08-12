package org.visab.gui.visualize.cbrshooter.viewmodel;

import java.time.LocalDateTime;
import java.util.List;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.globalmodel.cbrshooter.WeaponInformation;
import org.visab.gui.visualize.LiveVisualizeViewModelBase;
import org.visab.gui.visualize.PlayerInformation;
import org.visab.processing.ILiveViewable;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CBRShooterMetaViewModel extends LiveVisualizeViewModelBase<CBRShooterFile, CBRShooterStatistics> {

    private IntegerProperty roundsProperty;
    private FloatProperty statisticsPerSecondProperty;
    private DoubleProperty ingameTimeProperty;
    private ObservableList<WeaponInformation> weaponInformation;
    private ObservableList<PlayerInformation> playerInformation;
    private StringProperty winnerProperty;

    @SuppressWarnings("unchecked")
    public void initialize() {
        List<CBRShooterStatistics> statistics = null;
        if (scope.isLive()) {
            super.initialize(scope.getSessionListener());
            // Register ourselves, for when the view closes
            scope.registerOnStageClosing(s -> onSessionClosed());

            statistics = ((ILiveViewable<CBRShooterStatistics>) scope.getSessionListener()).getStatistics();
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
        winnerProperty = new SimpleStringProperty(file.getWinner());

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
    public void onStatisticsAdded(CBRShooterStatistics newStatistics) {
        roundsProperty.set(newStatistics.getRound());
        ingameTimeProperty.set(newStatistics.getTotalTime());
        statisticsPerSecondProperty.set(file.getStatistics().size() / newStatistics.getTotalTime());
    }

    public StringProperty winnerProperty() {
        return winnerProperty;
    }

}
