package org.visab.newgui.visualize.settlers.model.comparison;

import java.util.HashMap;
import java.util.List;

import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.newgui.visualize.StatisticsDataStructure;
import org.visab.newgui.visualize.settlers.model.SettlersImplicator;
import org.visab.newgui.visualize.settlers.model.SettlersComparisonRowBase;
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
    public void updateValues(SettlersFile file) {
        var lastStatistics = file.getStatistics().get(file.getStatistics().size() - 1);

        for (var player : lastStatistics.getPlayers()) {
            if (!playerValues.containsKey(player.getName()))
                playerValues.put(player.getName(), new SimpleIntegerProperty(0));

            playerValues.get(player.getName()).set(player.getVictoryPoints());
        }
    }

    @Override
    public void updateSeries(SettlersFile file) {
        var statistics = file.getStatistics();

        var playerData = new HashMap<String, List<StatisticsDataStructure<Double>>>();
        for (var name : file.getPlayerNames())
            playerData.put(name, SettlersImplicator.accumulatedVictoryPointsPerTurn(name, file));

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
