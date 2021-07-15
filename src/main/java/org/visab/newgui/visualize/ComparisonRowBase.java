package org.visab.newgui.visualize;

import java.util.HashMap;
import java.util.Map;

import org.visab.globalmodel.IVISABFile;

import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.chart.XYChart.Series;

public abstract class ComparisonRowBase<TProperty extends Property<?>> {
    protected String rowDescription;
    protected ObservableMap<String, TProperty> playerValues = FXCollections.observableHashMap();
    protected ObservableMap<String, Series<Double, Double>> playerSeries = FXCollections.observableHashMap();

    public ComparisonRowBase(String rowDescription) {
        this.rowDescription = rowDescription;
    }

    public String getRowDescription() {
        return this.rowDescription;
    }

    public Map<String, TProperty> getPlayerValues() {
        return playerValues;
    }

    public Map<String, Series<Double, Double>> getPlayerSeries() {
        return playerSeries;
    }

    public abstract void updateValues(IVISABFile file);

    public abstract void updateSeries(IVISABFile file);

}