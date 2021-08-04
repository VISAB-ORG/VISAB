package org.visab.newgui.visualize.settlers.model.comparison;

import java.util.HashMap;
import java.util.List;

import org.visab.globalmodel.settlers.PlayerResources;
import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.newgui.visualize.StatisticsDataStructure;
import org.visab.newgui.visualize.settlers.model.SettlersComparisonRowBase;
import org.visab.newgui.visualize.settlers.model.SettlersImplicator.ResourceType;
import org.visab.newgui.visualize.settlers.model.SettlersImplicator;
import org.visab.util.StreamUtil;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class SingleResourcesGainedComparisonRow extends SettlersComparisonRowBase<ObjectProperty<PlayerResources>> {
    
    private ResourceType resourceType;

    public SingleResourcesGainedComparisonRow(ResourceType resourceType) {
        super(resourceType + " gained");
        this.resourceType = resourceType;
    }

    @Override
    public void updateValues(SettlersFile file) {
        var resourcesGained = SettlersImplicator.concludeResourcesGainedByDice(file);
        for (var entry : resourcesGained.entrySet())
            playerValues.put(entry.getKey(), new SimpleObjectProperty<PlayerResources>(entry.getValue()));
    }

    @Override
    public void updateSeries(SettlersFile file) {
        var statistics = file.getStatistics();

        var playerData = new HashMap<String, List<StatisticsDataStructure<Double>>>();
        for (var name : file.getPlayerNames())
            playerData.put(name, SettlersImplicator.accumulatedSingleResourceGainedPerTurn(name, file, resourceType));

        for (var snapshot : statistics) {
            for (var player : snapshot.getPlayers()) {
                var name = player.getName();

                if (!playerSeries.containsKey(name)) {
                    var newSeries = new Series<Integer, Number>();
                    newSeries.setName(name);
                    playerSeries.put(name, newSeries);
                }
                var resourceGainedPerTurn = playerData.get(name);

                var graphData = playerSeries.get(name).getData();
                for (var data : resourceGainedPerTurn) {
                    if (!StreamUtil.contains(graphData, x -> x.getXValue() == data.getRound())) {
                        graphData.add(new Data<Integer, Number>(data.getRound(), data.getValue()));
                    }
                }
            }
        }
    }

}
