package org.visab.newgui.visualize.cbrshooter.model.comparison;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterImplicator;
import org.visab.newgui.visualize.cbrshooter.model.ComparisonRowBase;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class HitsComparisonRow extends ComparisonRowBase<IntegerProperty> {

    public HitsComparisonRow() {
        super("Total hits");
    }

    @Override
    public void updateValues(CBRShooterFile file) {
        var result = CBRShooterImplicator.concludeHitsTaken(file);
        for (var name : result.keySet()) {
            if (!playerValues.containsKey(name))
                playerValues.put(name, new SimpleIntegerProperty(0));

            playerValues.get(name).set(result.get(name));
        }
    }

}
