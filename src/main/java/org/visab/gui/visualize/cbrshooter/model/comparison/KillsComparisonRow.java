package org.visab.gui.visualize.cbrshooter.model.comparison;

import java.util.HashMap;
import java.util.List;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.gui.visualize.StatisticsData;
import org.visab.gui.visualize.cbrshooter.model.CBRShooterComparisonRowBase;
import org.visab.gui.visualize.cbrshooter.model.CBRShooterImplicator;
import org.visab.util.StreamUtil;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class KillsComparisonRow extends CBRShooterComparisonRowBase<IntegerProperty> {

    public KillsComparisonRow() {
        super("Kills");
    }

    @Override
    public void updateValues(CBRShooterFile file, List<CBRShooterStatistics> statistics) {
        var lastStatistics = statistics.get(statistics.size() - 1);
        for (var player : lastStatistics.getPlayers()) {
            var name = player.getName();
            if (!playerValues.containsKey(name))
                playerValues.put(name, new SimpleIntegerProperty(0));

            playerValues.get(name).set(player.getStatistics().getFrags());
        }
    }

    @Override
    public void updateSeries(CBRShooterFile file, List<CBRShooterStatistics> statistics) {
        var playerData = new HashMap<String, List<StatisticsData<Double>>>();
        for (var name : file.getPlayerNames())
            playerData.put(name, CBRShooterImplicator.accumulatedKillsPerRound(name, statistics));

        for (var snapshot : statistics) {
            for (var player : snapshot.getPlayers()) {
                var name = player.getName();

                if (!playerSeries.containsKey(name)) {
                    var newSeries = new Series<Integer, Number>();
                    newSeries.setName(name);
                    playerSeries.put(name, newSeries);
                }
                var killsPerRound = playerData.get(name);

                var graphData = playerSeries.get(name).getData();
                for (var data : killsPerRound) {
                    if (!StreamUtil.contains(graphData, x -> x.getXValue() == data.getRound())) {
                        graphData.add(new Data<Integer, Number>(data.getRound(), data.getValue()));
                    }
                }
            }
        }
    }

}
