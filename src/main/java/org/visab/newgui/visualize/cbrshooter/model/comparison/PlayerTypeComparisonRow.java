package org.visab.newgui.visualize.cbrshooter.model.comparison;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterComparisonRowBase;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlayerTypeComparisonRow extends CBRShooterComparisonRowBase<StringProperty> {

    public PlayerTypeComparisonRow() {
        super("Player type");
    }

    @Override
    public void updateValues(CBRShooterFile file) {
        for (var entry : file.getPlayerInformation().entrySet()) {
            if (!playerValues.containsKey(entry.getKey())) {
                var raw = entry.getValue();
                var playerType = raw.substring(0, 1).toUpperCase() + raw.substring(1, raw.length());
                
                playerValues.put(entry.getKey(), new SimpleStringProperty(playerType));
            }
        }
    }

    @Override
    public void updateSeries(CBRShooterFile file) {
        // TODO Auto-generated method stub
        
    }

}
