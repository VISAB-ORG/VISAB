package org.visab.newgui.visualize.cbrshooter.model;


import org.visab.globalmodel.IVISABFile;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.newgui.visualize.ComparisonRowBase;

import javafx.beans.property.Property;

public abstract class CBRShooterComparisonRowBase<TProperty extends Property<?>> extends ComparisonRowBase<TProperty> {

    public CBRShooterComparisonRowBase(String rowDescription) {
        super(rowDescription);
    }

    @Override
    public void updateValues(IVISABFile file) {
        updateValues((CBRShooterFile) file);
    }

    @Override
    public void updateSeries(IVISABFile file) {
        updateSeries((CBRShooterFile) file);
    }

    public abstract void updateValues(CBRShooterFile file);

    public abstract void updateSeries(CBRShooterFile file);

}
