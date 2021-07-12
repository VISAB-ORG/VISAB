package org.visab.newgui.visualize.cbrshooter.model.comparison;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterComparisonRowBase;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterImplicator;

import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class AimRatioComparisonRow extends CBRShooterComparisonRowBase<DoubleProperty> {

    public AimRatioComparisonRow() {
        super("Aim ratio");
    }

    @Override
    public void updateValues(CBRShooterFile file) {
        var result = CBRShooterImplicator.concludeAimRatio(file);
        for (var name : result.keySet()) {
            if (!playerValues.containsKey(name))
                playerValues.put(name, new SimpleDoubleProperty(0));

            playerValues.get(name).set(result.get(name));
        }
    }

    @Override
    public void updateSeries(CBRShooterFile file) {
        for (var statistics : file.getStatistics()) {
            for (var player : statistics.getPlayers()) {
                if (!playerSeries.containsKey(player.getName())) {
                    var newSeries = new Series<Double, Double>();
                    newSeries.setName(player.getName());
                    playerSeries.put(player.getName(), newSeries);
                }
                var data = playerSeries.get(player.getName()).getData();
                if (data.size() == 0 || (data.get(data.size() - 1).getXValue() != statistics.getRound())) {
                    // TODO
                    var x = CBRShooterImplicator.shotsPerRound(player.getName(), file);
                    data.add(new Data<Double, Double>((double)statistics.getRound(), (double)x.get(x.size() - 1).getParameter()));
                }
            }
        }
    }

}
