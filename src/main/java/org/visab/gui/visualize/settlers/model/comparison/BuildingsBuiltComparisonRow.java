package org.visab.gui.visualize.settlers.model.comparison;

import java.util.HashMap;
import java.util.List;

import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.gui.visualize.StatisticsData;
import org.visab.gui.visualize.settlers.model.SettlersComparisonRowBase;
import org.visab.gui.visualize.settlers.model.SettlersImplicator;
import org.visab.gui.visualize.settlers.model.SettlersImplicator.BuildingType;
import org.visab.util.StreamUtil;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class BuildingsBuiltComparisonRow extends SettlersComparisonRowBase<IntegerProperty> {

    private BuildingType buildingType;

    public BuildingsBuiltComparisonRow(BuildingType buildingType) {
        super(buildingType.toString() + " built");
        this.buildingType = buildingType;
    }

    @Override
    public void updateValues(SettlersFile file, List<SettlersStatistics> statistics) {
        var lastStatistics = statistics.get(statistics.size() - 1);

        for (var player : lastStatistics.getPlayers()) {
            if (!playerValues.containsKey(player.getName()))
                playerValues.put(player.getName(), new SimpleIntegerProperty(0));

            switch (buildingType) {
            case Road:
                playerValues.get(player.getName()).set(player.getStreetCount());
                break;
            case Town:
                playerValues.get(player.getName()).set(player.getCityCount());
                break;
            case Village:
                playerValues.get(player.getName()).set(player.getVillageCount());
                break;
            default:
                throw new RuntimeException("Building type not implemented!");
            }
        }
    }

    @Override
    public void updateSeries(SettlersFile file, List<SettlersStatistics> statistics) {
        var playerData = new HashMap<String, List<StatisticsData<Double>>>();
        for (var name : file.getPlayerNames())
            playerData.put(name,
                    SettlersImplicator.accumulatedBuildingBuiltPerTurn(name, statistics, this.buildingType));

        for (var snapshot : statistics) {
            for (var player : snapshot.getPlayers()) {
                var name = player.getName();

                if (!playerSeries.containsKey(name)) {
                    var newSeries = new Series<Integer, Number>();
                    newSeries.setName(name);
                    playerSeries.put(name, newSeries);
                }
                var buildingsBuiltPerTurn = playerData.get(name);

                var graphData = playerSeries.get(name).getData();
                for (var data : buildingsBuiltPerTurn) {
                    if (!StreamUtil.contains(graphData, x -> x.getXValue() == data.getRound())) {
                        graphData.add(new Data<Integer, Number>(data.getRound(), data.getValue()));
                    }
                }
            }
        }
    }

}