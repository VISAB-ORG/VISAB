package org.visab.newgui.visualize.cbrshooter.model;

import java.util.HashMap;
import java.util.Map;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;

import javafx.beans.property.Property;

public abstract class ComparisonRowBase<TProperty extends Property<?>> {

    protected String rowDescription;
    protected Map<String, TProperty> playerValues = new HashMap<>();

    public ComparisonRowBase(String rowDescrition) {
        this.rowDescription = rowDescrition;
    }

    public String getRowDescrition() {
        return this.rowDescription;
    }

    public Map<String, TProperty> getPlayerValues() {
        return playerValues;
    }

    public abstract void updateValues(CBRShooterFile file);

}