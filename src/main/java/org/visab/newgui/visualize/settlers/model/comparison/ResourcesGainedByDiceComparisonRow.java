package org.visab.newgui.visualize.settlers.model.comparison;

import org.visab.globalmodel.settlers.PlayerResources;
import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.newgui.visualize.settlers.model.SettlersComparisonRowBase;
import org.visab.newgui.visualize.settlers.model.SettlersImplicator;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ResourcesGainedByDiceComparisonRow extends SettlersComparisonRowBase<ObjectProperty<PlayerResources>> {

    public ResourcesGainedByDiceComparisonRow() {
        super("Cumulated resource gain by dice");
    }

    @Override
    public void updateValues(SettlersFile file) {
        var resourcesGained = SettlersImplicator.concludeResourcesGainedByDice(file);
        for (var entry : resourcesGained.entrySet())
            playerValues.put(entry.getKey(), new SimpleObjectProperty<PlayerResources>(entry.getValue()));
    }

    @Override
    public void updateSeries(SettlersFile file) {
        // TODO Auto-generated method stub
        
    }

}
