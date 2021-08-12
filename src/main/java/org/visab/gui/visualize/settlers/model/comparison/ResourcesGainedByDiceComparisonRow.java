package org.visab.gui.visualize.settlers.model.comparison;

import java.util.HashMap;
import java.util.List;

import org.visab.globalmodel.settlers.PlayerResources;
import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.gui.visualize.StatisticsData;
import org.visab.gui.visualize.settlers.model.SettlersComparisonRowBase;
import org.visab.gui.visualize.settlers.model.SettlersImplicator;
import org.visab.util.StreamUtil;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class ResourcesGainedByDiceComparisonRow extends SettlersComparisonRowBase<ObjectProperty<PlayerResources>> {

    public ResourcesGainedByDiceComparisonRow() {
        super("Cumulated resource gain by dice");
    }

    @Override
    public void updateValues(SettlersFile file, List<SettlersStatistics> statistics) {
        var resourcesGained = SettlersImplicator.concludeResourcesGainedByDice(statistics, file.getPlayerNames());
        for (var entry : resourcesGained.entrySet())
            playerValues.put(entry.getKey(), new SimpleObjectProperty<PlayerResources>(entry.getValue()));
    }

    @Override
    public void updateSeries(SettlersFile file, List<SettlersStatistics> statistics) {
        var playerData = new HashMap<String, List<StatisticsData<PlayerResources>>>();
        for (var name : file.getPlayerNames())
            playerData.put(name, SettlersImplicator.accumulatedResourcesGainedPerTurn(name, statistics));

        for (var snapshot : statistics) {
            for (var player : snapshot.getPlayers()) {
                var name = player.getName();

                if (!playerSeries.containsKey(name)) {
                    var newSeries = new Series<Integer, Number>();
                    newSeries.setName(name);
                    playerSeries.put(name, newSeries);
                }
                var resourcesGaintPerTurn = playerData.get(name);

                var graphData = playerSeries.get(name).getData();
                for (var data : resourcesGaintPerTurn) {
                    if (!StreamUtil.contains(graphData, x -> x.getXValue() == data.getRound())) {
                        var sum = 0;
                        sum += data.getValue().getBrick() + data.getValue().getSheep() + data.getValue().getStone()
                                + data.getValue().getWheat() + data.getValue().getWood();
                        graphData.add(new Data<Integer, Number>(data.getRound(), sum));
                    }
                }
            }
        }
    }

}