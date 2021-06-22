package org.visab.newgui.visualize.cbrshooter.model;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class MovementComparisonRow extends ComparisonRowBase<DoubleProperty> {

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

}
