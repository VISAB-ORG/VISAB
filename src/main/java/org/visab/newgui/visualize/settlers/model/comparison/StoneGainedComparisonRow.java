package org.visab.newgui.visualize.settlers.model.comparison;

import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.newgui.visualize.settlers.model.SettlersComparisonRowBase;

import javafx.beans.property.IntegerProperty;

public class StoneGainedComparisonRow extends SettlersComparisonRowBase<IntegerProperty> {

    public StoneGainedComparisonRow() {
        super("Stone gained");
    }

    @Override
    public void updateValues(SettlersFile file) {
    }
    
}
