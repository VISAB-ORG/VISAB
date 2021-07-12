package org.visab.newgui.visualize.cbrshooter.model.comparison;

import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterComparisonRowBase;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class KillsComparisonRow extends CBRShooterComparisonRowBase<IntegerProperty> {

    public KillsComparisonRow() {
        super("Kills");
    }

    @Override
    public void updateValues(CBRShooterFile file) {
        var lastStatistics = file.getStatistics().get(file.getStatistics().size() - 1);
        for (var player : lastStatistics.getPlayers()) {
            var name = player.getName();
            if (!playerValues.containsKey(name))
                playerValues.put(name, new SimpleIntegerProperty(0));

            playerValues.get(name).set(player.getStatistics().getFrags());
        }
    }

}
