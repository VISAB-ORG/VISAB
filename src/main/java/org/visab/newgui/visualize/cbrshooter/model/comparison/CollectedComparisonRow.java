package org.visab.newgui.visualize.cbrshooter.model.comparison;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterImplicator;
import org.visab.newgui.visualize.cbrshooter.model.Collectable;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterComparisonRowBase;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class CollectedComparisonRow extends CBRShooterComparisonRowBase<IntegerProperty> {

    private Collectable collectable;

    public CollectedComparisonRow(Collectable collectable) {
        super(collectable.toString() + " items collected");
    }

    @Override
    public void updateValues(CBRShooterFile file) {
        var result = CBRShooterImplicator.concludeCollected(file, collectable);
        for (var name : result.keySet()) {
            if (!playerValues.containsKey(name))
                playerValues.put(name, new SimpleIntegerProperty(0));

            playerValues.get(name).set(result.get(name));
        }
    }

    @Override
    public void updateSeries(CBRShooterFile file) {
        
    }

}
