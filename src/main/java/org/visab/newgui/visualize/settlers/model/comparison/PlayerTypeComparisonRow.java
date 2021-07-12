package org.visab.newgui.visualize.settlers.model.comparison;

import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.newgui.visualize.settlers.model.SettlersComparisonRowBase;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlayerTypeComparisonRow extends SettlersComparisonRowBase<StringProperty> {

    public PlayerTypeComparisonRow() {
        super("Player type");
    }

    @Override
    public void updateValues(SettlersFile file) {
        for (var entry : file.getPlayerInformation().entrySet()) {
            if (!playerValues.containsKey(entry.getKey()))
                playerValues.put(entry.getKey(), new SimpleStringProperty(entry.getValue()));
        }
    }

}
