package org.visab.newgui.visualize.settlers.model.comparison;

import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.newgui.visualize.settlers.model.SettlersComparisonRowBase;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class VictoryPointsComparisonRow extends SettlersComparisonRowBase<IntegerProperty> {

    public VictoryPointsComparisonRow() {
        super("Victory points");
    }

    @Override
    public void updateValues(SettlersFile file) {
        var lastStatistics = file.getStatistics().get(file.getStatistics().size() - 1);

        for (var player : lastStatistics.getPlayers()) {
            if (!playerValues.containsKey(player.getName()))
                playerValues.put(player.getName(), new SimpleIntegerProperty(0));

            playerValues.get(player.getName()).set(player.getVictoryPoints());
        }
    }

    @Override
    public void updateSeries(SettlersFile file) {
        // TODO Auto-generated method stub
        
    }

}
