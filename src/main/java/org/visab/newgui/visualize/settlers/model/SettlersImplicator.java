package org.visab.newgui.visualize.settlers.model;

import java.util.HashMap;
import java.util.Map;

import org.visab.globalmodel.settlers.PlayerResources;
import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.workspace.Workspace;

public final class SettlersImplicator {

    public enum BuildingType {
        Town, Village, Road
    }

    public static final Map<String, PlayerResources> concludeResourcesGainedByDice(SettlersFile file) {
        var resourcesGained = new HashMap<String, PlayerResources>();
        for (String name : file.getPlayerNames())
            resourcesGained.put(name, new PlayerResources());

        for (var statistics : file.getStatistics()) {
            for (var player : statistics.getPlayers()) {
                var name = player.getName();

                var untilNow = resourcesGained.get(name);
                var current = player.getResourcesGained();

                resourcesGained.put(name, PlayerResources.add(untilNow, current));
            }
        }

        return resourcesGained;
    }

    public static final Map<String, PlayerResources> concludeResourcesSpent(SettlersFile file) {
        var resourcesSpent = new HashMap<String, PlayerResources>();
        for (String name : file.getPlayerNames())
            resourcesSpent.put(name, new PlayerResources());

        var resourcesLastTurn = new HashMap<String, PlayerResources>();
        for (var statistics : file.getStatistics()) {
            for (var player : statistics.getPlayers()) {
                if (!player.isMyTurn()) {
                    resourcesLastTurn.put(player.getName(), player.getResources());
                    
                    continue;
                }
                
                var name = player.getName();
                var lastTurn = resourcesLastTurn.getOrDefault(name, new PlayerResources());

                PlayerResources gainedThisTurn = null;
                if (statistics.getTurn() == 3 || statistics.getTurn() == 4)
                    gainedThisTurn = player.getVillageResourcesGained();
                else
                    gainedThisTurn = player.getResourcesGained();

                var lastTurnAndGained = PlayerResources.add(lastTurn, gainedThisTurn);

                var thisTurn = player.getResources();
                var spent = PlayerResources.sub(lastTurnAndGained, thisTurn);

                var existing = resourcesSpent.get(name);
                resourcesSpent.put(name, PlayerResources.add(existing, spent));

                resourcesLastTurn.put(name, thisTurn);
            }
        }

        return resourcesSpent;
    }

    public static void main(String[] args) {
        var file = (SettlersFile) Workspace.getInstance().getDatabaseManager()
                .loadFile("00b2391a-c44f-482b-8e88-df9c0381e515.visab2", "Settlers");

        var resourcesGained = SettlersImplicator.concludeResourcesGainedByDice(file);
        var resourcesSpent = SettlersImplicator.concludeResourcesSpent(file);
    }
}
