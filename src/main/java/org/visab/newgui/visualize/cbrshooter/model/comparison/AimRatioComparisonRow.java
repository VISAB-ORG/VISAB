package org.visab.newgui.visualize.cbrshooter.model.comparison;

import java.util.HashMap;
import java.util.List;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.newgui.visualize.StatisticsDataStructure;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterComparisonRowBase;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterImplicator;
import org.visab.util.StreamUtil;

import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class AimRatioComparisonRow extends CBRShooterComparisonRowBase<DoubleProperty> {

    public AimRatioComparisonRow() {
        super("Aim ratio");
    }

    @Override
    public void updateValues(CBRShooterFile file) {
        var result = CBRShooterImplicator.concludeAimRatio(file);
        for (var name : result.keySet()) {
            if (!playerValues.containsKey(name))
                playerValues.put(name, new SimpleDoubleProperty(0));

            playerValues.get(name).set(result.get(name));
        }
    }

    @Override
    public void updateSeries(CBRShooterFile file) {
        var playerData = new HashMap<String, List<StatisticsDataStructure>>();
        for (var name : file.getPlayerInformation().keySet())
            playerData.put(name, CBRShooterImplicator.aimRatioPerRound(name, file));

        for (var statistics : file.getStatistics()) {
            for (var player : statistics.getPlayers()) {
                var name = player.getName();

                if (!playerSeries.containsKey(name)) {
                    var newSeries = new Series<Double, Double>();
                    newSeries.setName(name);
                    playerSeries.put(name, newSeries);
                }
                var aimRatioPerRound = playerData.get(name);

                var graphData = playerSeries.get(name).getData();
                for (var data : aimRatioPerRound) {
                    if (!StreamUtil.contains(graphData, x -> x.getXValue() == data.getRound())) {
                        graphData.add(new Data<Double, Double>((double) data.getRound(), (double) data.getParameter()));
                    }
                }
            }
        }
        
    }

}
