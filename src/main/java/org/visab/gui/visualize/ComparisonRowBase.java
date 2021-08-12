package org.visab.gui.visualize;

import java.util.Map;

import org.visab.globalmodel.IVISABFile;

import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.chart.XYChart.Series;

/**
 * Class used for displaying comparison statistics within a table. This should
 * also be used to update the values of the specific statistics type to compare.
 */
public abstract class ComparisonRowBase<TProperty extends Property<?>> {

    protected String rowDescription;
    protected ObservableMap<String, TProperty> playerValues = FXCollections.observableHashMap();
    protected ObservableMap<String, Series<Integer, Number>> playerSeries = FXCollections.observableHashMap();

    /**
     * @param rowDescription The description of the comparison row
     */
    public ComparisonRowBase(String rowDescription) {
        this.rowDescription = rowDescription;
    }

    /**
     * The description of the comparison row.
     */
    public String getRowDescription() {
        return this.rowDescription;
    }

    /**
     * The player properties
     */
    public Map<String, TProperty> getPlayerProperties() {
        return playerValues;
    }

    /**
     * The player series
     */
    public Map<String, Series<Integer, Number>> getPlayerSeries() {
        return playerSeries;
    }

    /**
     * Updates the values of the properties of the statistics type.
     * 
     * @param file The file using which the values are updated
     */
    public abstract void updateValues(IVISABFile file);

    /**
     * Updates the data of the series of the statistics type.
     * 
     * @param file The file using which the series data is updated
     */
    public abstract void updateSeries(IVISABFile file);

}