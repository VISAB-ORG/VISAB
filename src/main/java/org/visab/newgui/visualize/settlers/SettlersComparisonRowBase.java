package org.visab.newgui.visualize.settlers;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.newgui.visualize.cbrshooter.model.ComparisonRowBase;

import javafx.beans.property.Property;

public class SettlersComparisonRowBase<T extends Property<?>> extends ComparisonRowBase<T> {

    public SettlersComparisonRowBase(String rowDescription) {
        super(rowDescription);
    }

    @Override
    public void updateValues(CBRShooterFile file) {
        // TODO Auto-generated method stub
        
    }

}
