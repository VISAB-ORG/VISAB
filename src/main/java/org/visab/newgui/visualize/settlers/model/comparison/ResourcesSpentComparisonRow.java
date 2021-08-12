package org.visab.newgui.visualize.settlers.model.comparison;

import java.util.HashMap;
import java.util.List;

import org.visab.globalmodel.settlers.PlayerResources;
import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.newgui.visualize.StatisticsDataStructure;
import org.visab.newgui.visualize.settlers.model.SettlersComparisonRowBase;
import org.visab.newgui.visualize.settlers.model.SettlersImplicator;
import org.visab.util.StreamUtil;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class ResourcesSpentComparisonRow extends SettlersComparisonRowBase<ObjectProperty<PlayerResources>> {

    public ResourcesSpentComparisonRow() {
        super("Cumulated resources spent");
    }

    @Override
    public void updateValues(SettlersFile file, List<SettlersStatistics> statistics) {
        var resourcesSpent = SettlersImplicator.concludeResourcesSpent(statistics, file.getPlayerNames());
        for (var entry : resourcesSpent.entrySet())
            playerValues.put(entry.getKey(), new SimpleObjectProperty<PlayerResources>(entry.getValue()));
    }

    @Override
    public void updateSeries(SettlersFile file, List<SettlersStatistics> statistics) {
        var playerData = new HashMap<String, List<StatisticsDataStructure<PlayerResources>>>();
        for (var name : file.getPlayerNames())
            playerData.put(name, SettlersImplicator.accumulatedResourcesSpentPerTurn(name, statistics));

        for (var snapshot : statistics) {
            for (var player : snapshot.getPlayers()) {
                var name = player.getName();

                if (!playerSeries.containsKey(name)) {
                    var newSeries = new Series<Integer, Number>();
                    newSeries.setName(name);
                    playerSeries.put(name, newSeries);
                }
                var resourcesSpentPerTurn = playerData.get(name);

                var graphData = playerSeries.get(name).getData();
                for (var data : resourcesSpentPerTurn) {
                    var sum = 0;
                    if (!StreamUtil.contains(graphData, x -> x.getXValue() == data.getRound())) {

                        sum += (data.getValue().getBrick() + data.getValue().getSheep() + data.getValue().getStone()
                                + data.getValue().getWheat() + data.getValue().getWood());
                        graphData.add(new Data<Integer, Number>(data.getRound(), sum));
                    }
                }
            }
        }
    }
    
}
