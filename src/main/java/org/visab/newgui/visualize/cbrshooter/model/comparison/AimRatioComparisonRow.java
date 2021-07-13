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
    }

}
