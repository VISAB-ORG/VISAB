package org.visab.gui.visualize.settlers.model.comparison;

import java.util.HashMap;
import java.util.List;

import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.gui.visualize.StatisticsData;
import org.visab.gui.visualize.settlers.model.SettlersImplicator;
import org.visab.gui.visualize.settlers.model.SettlersComparisonRowBase;
import org.visab.util.StreamUtil;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class VictoryPointsComparisonRow extends SettlersComparisonRowBase<IntegerProperty> {

    public VictoryPointsComparisonRow() {
        super("Victory points");
    }

    @Override
    public void updateValues(SettlersFile file, List<SettlersStatistics> statistics) {
        var lastStatistics = statistics.get(statistics.size() - 1);

        for (var player : lastStatistics.getPlayers()) {
            if (!playerValues.containsKey(player.getName()))
                playerValues.put(player.getName(), new SimpleIntegerProperty(0));

            playerValues.get(player.getName()).set(player.getVictoryPoints());
        }
    }

    @Override
    public void updateSeries(SettlersFile file, List<SettlersStatistics> statistics) {
        var playerData = new HashMap<String, List<StatisticsData<Double>>>();
        for (var name : file.getPlayerNames())
            playerData.put(name, SettlersImplicator.accumulatedVictoryPointsPerTurn(name, statistics));

        for (var snapshot : statistics) {
            for (var player : snapshot.getPlayers()) {
                var name = player.getName();

                if (!playerSeries.containsKey(name)) {
                    var newSeries = new Series<Integer, Number>();
                    newSeries.setName(name);
                    playerSeries.put(name, newSeries);
                }
                var victoryPointsPerTurn = playerData.get(name);

                var graphData = playerSeries.get(name).getData();
                for (var data : victoryPointsPerTurn) {
                    if (!StreamUtil.contains(graphData, x -> x.getXValue() == data.getRound())) {
                        graphData.add(new Data<Integer, Number>(data.getRound(), data.getValue()));
                    }
                }
            }
        }
    }
    
}
