package org.visab.newgui.visualize.settlers.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.visab.globalmodel.settlers.PlayerResources;
import org.visab.globalmodel.settlers.SettlersFile;
import org.visab.newgui.visualize.StatisticsDataStructure;
import org.visab.workspace.Workspace;

public final class SettlersImplicator {
    
    public static ArrayList<StatisticsDataStructure> accumulatedVictoryPointsPerTurn(String player, SettlersFile file) {
        var victoryPointsPerTurnPerPlayer = new ArrayList<StatisticsDataStructure>();
        var countVictoryPoints = 0;
        var turn = 0;
        var playerNumber = 0;
        
        if (player.contains("Player2")) {
            playerNumber = 1;
        }

        file.getStatistics().get(0).getPlayers().get(playerNumber).getVictoryPoints();
        for (int i = 0; i < file.getStatistics().size(); i++) {
            
            if (turn < file.getStatistics().get(i).getTurn()) {
                victoryPointsPerTurnPerPlayer.add(new StatisticsDataStructure(turn, (double) countVictoryPoints));
            }
            
            countVictoryPoints += file.getStatistics().get(i).getPlayers().get(playerNumber).getVictoryPoints();

            turn = file.getStatistics().get(i).getTurn();            
            
        }

        return victoryPointsPerTurnPerPlayer;
    }
    
    public static ArrayList<StatisticsDataStructure> accumulatedResourcesGaintPerTurn(String player, SettlersFile file) {
        var resourcesGaintPerTurnPerPlaye = new ArrayList<StatisticsDataStructure>();
        var countResourcesGaint = 0;
        var turn = 0;
        var playerNumber = 0;
        
        if (player.contains("Player2")) {
            playerNumber = 1;
        }

        file.getStatistics().get(0).getPlayers().get(playerNumber).getVictoryPoints();
        for (int i = 0; i < file.getStatistics().size(); i++) {
            
            if (turn < file.getStatistics().get(i).getTurn()) {
                resourcesGaintPerTurnPerPlaye.add(new StatisticsDataStructure(turn, (double) countResourcesGaint));
            }
            
            countResourcesGaint += file.getStatistics().get(i).getPlayers().get(playerNumber).getResourcesGained().getBrick();
            countResourcesGaint += file.getStatistics().get(i).getPlayers().get(playerNumber).getResourcesGained().getSheep();
            countResourcesGaint += file.getStatistics().get(i).getPlayers().get(playerNumber).getResourcesGained().getStone();
            countResourcesGaint += file.getStatistics().get(i).getPlayers().get(playerNumber).getResourcesGained().getWheat();
            countResourcesGaint += file.getStatistics().get(i).getPlayers().get(playerNumber).getResourcesGained().getWood();
            
            System.out.print(countResourcesGaint);
            
            turn = file.getStatistics().get(i).getTurn();            
            
        }

        return resourcesGaintPerTurnPerPlaye;
    }

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
                .loadFile("8d977e30-6209-446f-ba52-e612d6a77a3e.visab2", "Settlers");

        var resourcesGained = SettlersImplicator.concludeResourcesGainedByDice(file);
        var resourcesSpent = SettlersImplicator.concludeResourcesSpent(file);
        
        var test = accumulatedResourcesGaintPerTurn("Player1", file);
        for (int i = 0; i < test.size(); i++) {
            System.out.println(test.get(i).getRound() + " : " + test.get(i).getValue());
        }
    }
}
