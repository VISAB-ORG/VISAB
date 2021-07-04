package org.visab.newgui.visualize.settlers.model.comparison;

import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.newgui.visualize.settlers.model.SettlersComparisonRowBase;

import javafx.beans.property.IntegerProperty;

public class ResourcesGainedComparisonRow extends SettlersComparisonRowBase<IntegerProperty> {

    public ResourcesGainedComparisonRow() {
        super("Cumulated resource gain");
    }

    @Override
    public void updateValues(SettlersFile file) {
    }
    
}
