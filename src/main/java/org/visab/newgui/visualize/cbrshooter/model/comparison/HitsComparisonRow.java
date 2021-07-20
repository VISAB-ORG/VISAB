package org.visab.newgui.visualize.cbrshooter.model.comparison;

import java.util.HashMap;
import java.util.List;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.newgui.visualize.StatisticsDataStructure;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterImplicator;
import org.visab.util.StreamUtil;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterComparisonRowBase;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class HitsComparisonRow extends CBRShooterComparisonRowBase<IntegerProperty> {

    public HitsComparisonRow() {
        super("Total hits on enemy");
    }

    @Override
    public void updateValues(CBRShooterFile file) {
        var result = CBRShooterImplicator.concludeHitsTaken(file);
        for (var name : result.keySet()) {
            if (!playerValues.containsKey(name))
                playerValues.put(name, new SimpleIntegerProperty(0));

            var otherPlayerName = getOtherPlayerName(file, name);
            playerValues.get(name).set(result.get(otherPlayerName));
        }
    }

    private String getOtherPlayerName(CBRShooterFile file, String myPlayer) {
        for (var name : file.getPlayerNames()) {
            if (name != myPlayer)
                return name;
        }

        return "";
    }

    @Override
    public void updateSeries(CBRShooterFile file) {
        var statistics = makeStatisticsCopy(file);

        var playerData = new HashMap<String, List<StatisticsDataStructure>>();
        for (var name : file.getPlayerNames())
            playerData.put(name, CBRShooterImplicator.hitsOnEnemyPerRound(name, file));

        for (var snapshot : statistics) {
            for (var player : snapshot.getPlayers()) {
                var name = player.getName();

                if (!playerSeries.containsKey(name)) {
                    var newSeries = new Series<Integer, Number>();
                    newSeries.setName(name);
                    playerSeries.put(name, newSeries);
                }
                var hitsOnEnemyPerRound = playerData.get(name);

                var graphData = playerSeries.get(name).getData();
                for (var data : hitsOnEnemyPerRound) {
                    if (!StreamUtil.contains(graphData, x -> x.getXValue() == data.getRound())) {
                        graphData.add(new Data<Integer, Number>(data.getRound(), data.getParameter()));
                    }
                }
            }
        }
        
    }

}
