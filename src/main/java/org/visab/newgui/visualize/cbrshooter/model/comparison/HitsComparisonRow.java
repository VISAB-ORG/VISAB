package org.visab.newgui.visualize.cbrshooter.model.comparison;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterImplicator;
import org.visab.newgui.visualize.cbrshooter.model.ComparisonRowBase;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class HitsComparisonRow extends ComparisonRowBase<IntegerProperty> {

    public HitsComparisonRow() {
        super("Total hits on enemy");
    }

    @Override
    public void updateValues(CBRShooterFile file) {
        var result = CBRShooterImplicator.concludeHitsTaken(file);
        for (var name : result.keySet()) {
            if (!playerValues.containsKey(name))
                playerValues.put(name, new SimpleIntegerProperty(0));

            var otherPlayerName = getOtherPlayerName(file, name);
            playerValues.get(name).set(result.get(otherPlayerName));
        }
    }

    private String getOtherPlayerName(CBRShooterFile file, String myPlayer) {
        for (var name : file.getPlayerInformation().keySet()) {
            if (name != myPlayer)
                return name;
        }

        return "";
    }

}
