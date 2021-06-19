package org.visab.newgui.visualize.cbrshooter.model;

import java.util.function.DoublePredicate;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;

import javafx.beans.property.DoubleProperty;

public class MovementComparisonRow extends ComparisonRowBase<DoubleProperty> {

    public MovementComparisonRow() {
        super("Units walked");
    }

    @Override
    public void updateValues(CBRShooterFile file) {
        CBRShooterImplicator.concludeMetresWalked(file);
    }

}
