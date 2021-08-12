package org.visab.newgui.visualize.cbrshooter.model.comparison;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.visualize.StatisticsData;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterComparisonRowBase;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterImplicator;
import org.visab.util.StreamUtil;

import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class AimRatioComparisonRow extends CBRShooterComparisonRowBase<DoubleProperty> {

    public AimRatioComparisonRow() {
        super("Aim ratio (in %)");
    }

    @Override
    public void updateValues(CBRShooterFile file, List<CBRShooterStatistics> statistics) {
        var result = CBRShooterImplicator.concludeAimRatio(statistics, file.getPlayerNames(),
                file.getWeaponInformation());
        for (var name : result.keySet()) {
            if (!playerValues.containsKey(name))
                playerValues.put(name, new SimpleDoubleProperty(0));

            playerValues.get(name).set(result.get(name) * 100);
        }
    }

    @Override
    public void updateSeries(CBRShooterFile file, List<CBRShooterStatistics> statistics) {
        var playerData = new HashMap<String, List<StatisticsData<Double>>>();
        for (var name : file.getPlayerNames())
            playerData.put(name, CBRShooterImplicator.aimRatioPerRound(name, statistics, file.getWeaponInformation()));

        for (var snapshot : statistics) {
            for (var player : snapshot.getPlayers()) {
                var name = player.getName();

                if (!playerSeries.containsKey(name)) {
                    var newSeries = new Series<Integer, Number>();
                    newSeries.setName(name);
                    playerSeries.put(name, newSeries);
                }
                var aimRatioPerRound = playerData.get(name);

                var graphData = playerSeries.get(name).getData();
                for (var data : aimRatioPerRound) {
                    // If there is no value for this round
                    if (!StreamUtil.contains(graphData, x -> x.getXValue() == data.getRound())) {
                        graphData.add(new Data<Integer, Number>(data.getRound(), data.getValue() * 100));
                    }
                }
            }
        }
    }

}
