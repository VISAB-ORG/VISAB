package org.visab.newgui.visualize.cbrshooter.model.comparison;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterImplicator;
import org.visab.newgui.visualize.cbrshooter.model.ComparisonRowBase;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class AimRatioComparisonRow extends ComparisonRowBase<DoubleProperty> {

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

}
