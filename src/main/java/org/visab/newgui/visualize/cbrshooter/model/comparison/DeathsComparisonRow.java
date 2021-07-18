package org.visab.newgui.visualize.cbrshooter.model.comparison;

import java.util.HashMap;
import java.util.List;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.newgui.visualize.StatisticsDataStructure;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

import org.visab.newgui.visualize.cbrshooter.model.CBRShooterComparisonRowBase;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterImplicator;
import org.visab.util.StreamUtil;

public class DeathsComparisonRow extends CBRShooterComparisonRowBase<IntegerProperty> {

    public DeathsComparisonRow() {
        super("Deaths");
    }

    @Override
    public void updateValues(CBRShooterFile file) {
        var lastStatistics = file.getStatistics().get(file.getStatistics().size() - 1);
        for (var player : lastStatistics.getPlayers()) {
            var name = player.getName();
            if (!playerValues.containsKey(name))
                playerValues.put(name, new SimpleIntegerProperty(0));

            playerValues.get(name).set(player.getStatistics().getDeaths());
        }
    }

    @Override
    public void updateSeries(CBRShooterFile file) {
        var statistics = makeStatisticsCopy(file);

        var playerData = new HashMap<String, List<StatisticsDataStructure>>();
        for (var name : file.getPlayerNames())
            playerData.put(name, CBRShooterImplicator.accumulatedDeathsPerRound(name, file));

        for (var snapshot : statistics) {
            for (var player : snapshot.getPlayers()) {
                var name = player.getName();

                if (!playerSeries.containsKey(name)) {
                    var newSeries = new Series<Double, Double>();
                    newSeries.setName(name);
                    playerSeries.put(name, newSeries);
                }
                var deathsPerRound = playerData.get(name);

                var graphData = playerSeries.get(name).getData();
                for (var data : deathsPerRound) {
                    if (!StreamUtil.contains(graphData, x -> x.getXValue() == data.getRound())) {
                        graphData.add(new Data<Double, Double>((double) data.getRound(), (double) data.getParameter()));
                    }
                }
            }
        }
        
    }

}
