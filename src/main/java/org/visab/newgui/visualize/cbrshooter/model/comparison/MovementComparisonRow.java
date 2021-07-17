package org.visab.newgui.visualize.cbrshooter.model.comparison;

import java.util.HashMap;
import java.util.List;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.newgui.visualize.StatisticsDataStructure;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterImplicator;
import org.visab.util.StreamUtil;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterComparisonRowBase;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class MovementComparisonRow extends CBRShooterComparisonRowBase<DoubleProperty> {

    public MovementComparisonRow() {
        super("Units walked");
    }

    @Override
    public void updateValues(CBRShooterFile file) {
        var result = CBRShooterImplicator.concludeUnitsWalked(file);
        for (var name : result.keySet()) {
            if (!playerValues.containsKey(name))
                playerValues.put(name, new SimpleDoubleProperty(0));
            
            playerValues.get(name).set(result.get(name));
        }
    }

    @Override
    public void updateSeries(CBRShooterFile file) {
        var playerData = new HashMap<String, List<StatisticsDataStructure>>();
        for (var name : file.getPlayerNames())
            playerData.put(name, CBRShooterImplicator.unitsWalkedPerRound(name, file));

        for (var statistics : file.getStatistics()) {
            for (var player : statistics.getPlayers()) {
                var name = player.getName();

                if (!playerSeries.containsKey(name)) {
                    var newSeries = new Series<Double, Double>();
                    newSeries.setName(name);
                    playerSeries.put(name, newSeries);
                }
                var unitsWalkedPerRound = playerData.get(name);

                var graphData = playerSeries.get(name).getData();
                for (var data : unitsWalkedPerRound) {
                    if (!StreamUtil.contains(graphData, x -> x.getXValue() == data.getRound())) {
                        graphData.add(new Data<Double, Double>((double) data.getRound(), (double) data.getParameter()));
                    }
                }
            }
        }
        
    }

}
