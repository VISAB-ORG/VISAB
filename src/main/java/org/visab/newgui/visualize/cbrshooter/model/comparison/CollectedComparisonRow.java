package org.visab.newgui.visualize.cbrshooter.model.comparison;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.visualize.StatisticsDataStructure;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterImplicator;
import org.visab.newgui.visualize.cbrshooter.model.Collectable;
import org.visab.util.StreamUtil;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterComparisonRowBase;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class CollectedComparisonRow extends CBRShooterComparisonRowBase<IntegerProperty> {

    private Collectable collectable;

    public CollectedComparisonRow(Collectable collectable) {
        super(collectable.toString() + " items collected");
        this.collectable = collectable;
    }

    @Override
    public void updateValues(CBRShooterFile file, List<CBRShooterStatistics> statistics) {
        var result = CBRShooterImplicator.concludeCollected(statistics, file.getPlayerNames(), collectable);
        for (var name : result.keySet()) {
            if (!playerValues.containsKey(name))
                playerValues.put(name, new SimpleIntegerProperty(0));

            playerValues.get(name).set(result.get(name));
        }
    }

    @Override
    public void updateSeries(CBRShooterFile file, List<CBRShooterStatistics> statistics) {
        var playerData = new HashMap<String, List<StatisticsDataStructure<Double>>>();
        for (var name : file.getPlayerNames())
            playerData.put(name, CBRShooterImplicator.collectedCollectablesPerRound(name, statistics, collectable));

        for (var snapshot : statistics) {
            for (var player : snapshot.getPlayers()) {
                var name = player.getName();

                if (!playerSeries.containsKey(name)) {
                    var newSeries = new Series<Integer, Number>();
                    newSeries.setName(name);
                    playerSeries.put(name, newSeries);
                }
                var collectedCollectablesPerRound = playerData.get(name);

                var graphData = playerSeries.get(name).getData();
                for (var data : collectedCollectablesPerRound) {
                    if (!StreamUtil.contains(graphData, x -> x.getXValue() == data.getRound())) {
                        graphData.add(new Data<Integer, Number>(data.getRound(), data.getValue()));
                    }
                }
            }
        }
    }

}
