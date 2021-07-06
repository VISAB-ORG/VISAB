package org.visab.newgui.visualize.settlers.model.comparison;

import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.settlers.model.SettlersComparisonRowBase;
import org.visab.newgui.visualize.settlers.model.SettlersImplicator.BuildingType;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class BuildingsBuiltComparisonRow extends SettlersComparisonRowBase<IntegerProperty> {

    private BuildingType buildingType;

    public BuildingsBuiltComparisonRow(BuildingType buildingType) {
        super(buildingType.toString() + " built");
        this.buildingType = buildingType;
    }

    @Override
    public void updateValues(SettlersFile file) {
        var lastStatistics = file.getStatistics().get(file.getStatistics().size() - 1);

        for (var player : lastStatistics.getPlayers()) {
            if (!playerValues.containsKey(player.getName()))
                playerValues.put(player.getName(), new SimpleIntegerProperty(0));

            switch (buildingType) {
            case Road:
                playerValues.get(player.getName()).set(player.getStreetCount());
                break;
            case Town:
                playerValues.get(player.getName()).set(player.getCityCount());
                break;
            case Village:
                playerValues.get(player.getName()).set(player.getVillageCount());
                break;
            default:
                throw new RuntimeException("Building type not implemented!");
            }
        }
    }

}
