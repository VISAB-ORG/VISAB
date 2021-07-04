package org.visab.newgui.visualize.settlers.model;

import org.visab.globalmodel.IVISABFile;
import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.newgui.visualize.ComparisonRowBase;

import javafx.beans.property.Property;

public abstract class SettlersComparisonRowBase<T extends Property<?>> extends ComparisonRowBase<T> {

    public SettlersComparisonRowBase(String rowDescription) {
        super(rowDescription);
    }

    @Override
    public void updateValues(IVISABFile file) {
        updateValues((SettlersFile)file);
    }

    public abstract void updateValues(SettlersFile file);

}
