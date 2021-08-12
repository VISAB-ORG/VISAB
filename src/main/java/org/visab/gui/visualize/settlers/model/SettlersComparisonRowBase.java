package org.visab.gui.visualize.settlers.model;

import java.util.ArrayList;
import java.util.List;

import org.visab.globalmodel.IVISABFile;
import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.gui.visualize.ComparisonRowBase;

import javafx.beans.property.Property;

public abstract class SettlersComparisonRowBase<T extends Property<?>> extends ComparisonRowBase<T> {

    public SettlersComparisonRowBase(String rowDescription) {
        super(rowDescription);
    }

    @Override
    public void updateValues(IVISABFile file) {
        var concreteFile = (SettlersFile) file;
        updateValues(concreteFile, new ArrayList<>(concreteFile.getStatistics()));
    }

    @Override
    public void updateSeries(IVISABFile file) {
        var concreteFile = (SettlersFile) file;
        updateSeries(concreteFile, new ArrayList<>(concreteFile.getStatistics()));
    }

    public abstract void updateValues(SettlersFile file, List<SettlersStatistics> statistics);

    public abstract void updateSeries(SettlersFile file, List<SettlersStatistics> statistics);

}
