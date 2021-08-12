package org.visab.gui.visualize.cbrshooter.model;

import java.util.ArrayList;
import java.util.List;

import org.visab.globalmodel.IVISABFile;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.gui.visualize.ComparisonRowBase;

import javafx.beans.property.Property;

public abstract class CBRShooterComparisonRowBase<TProperty extends Property<?>> extends ComparisonRowBase<TProperty> {

    public CBRShooterComparisonRowBase(String rowDescription) {
        super(rowDescription);
    }

    @Override
    public void updateValues(IVISABFile file) {
        var concreteFile = (CBRShooterFile) file;
        updateValues(concreteFile, new ArrayList<>(concreteFile.getStatistics()));
    }

    @Override
    public void updateSeries(IVISABFile file) {
        var concreteFile = (CBRShooterFile) file;
        updateSeries(concreteFile, new ArrayList<>(concreteFile.getStatistics()));
    }

    public abstract void updateValues(CBRShooterFile file, List<CBRShooterStatistics> statistics);

    public abstract void updateSeries(CBRShooterFile file, List<CBRShooterStatistics> statistics);

}
