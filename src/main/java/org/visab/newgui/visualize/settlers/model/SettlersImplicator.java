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

            countVictoryPoints = file.getStatistics().get(i).getPlayers().get(playerNumber).getVictoryPoints();

            turn = file.getStatistics().get(i).getTurn();

        }

        victoryPointsPerTurnPerPlayer.add(new StatisticsDataStructure(turn, (double) countVictoryPoints));

        return victoryPointsPerTurnPerPlayer;
    }

    public static ArrayList<StatisticsDataStructure> accumulatedResourcesGaintPerTurn(String player,
            SettlersFile file) {
        var resourcesGaintPerTurnPerPlaye = new ArrayList<StatisticsDataStructure>();
        var countResourcesSpent = 0;
        var turn = 0;
        var playerNumber = 0;

        if (player.contains("Player2")) {
            playerNumber = 1;
        }

        file.getStatistics().get(0).getPlayers().get(playerNumber).getVictoryPoints();
        for (int i = 0; i < file.getStatistics().size(); i++) {

            if (turn < file.getStatistics().get(i).getTurn()) {
                resourcesGaintPerTurnPerPlaye.add(new StatisticsDataStructure(turn, (double) countResourcesSpent));
            }

            countResourcesSpent += file.getStatistics().get(i).getPlayers().get(playerNumber).getResourcesGained()
                    .getBrick();
            countResourcesSpent += file.getStatistics().get(i).getPlayers().get(playerNumber).getResourcesGained()
                    .getSheep();
            countResourcesSpent += file.getStatistics().get(i).getPlayers().get(playerNumber).getResourcesGained()
                    .getStone();
            countResourcesSpent += file.getStatistics().get(i).getPlayers().get(playerNumber).getResourcesGained()
                    .getWheat();
            countResourcesSpent += file.getStatistics().get(i).getPlayers().get(playerNumber).getResourcesGained()
                    .getWood();

            turn = file.getStatistics().get(i).getTurn();

        }

        return resourcesGaintPerTurnPerPlaye;
    }

    public static ArrayList<StatisticsDataStructure> accumulatedResourcesSpentPerTurn(String player,
            SettlersFile file) {
        var resourcesSpentPerTurnPerPlaye = new ArrayList<StatisticsDataStructure>();
        var countResourcesSpent = 0;
        var actualResources = 0;
        var turn = 0;
        var playerNumber = 0;

        if (player.contains("Player2")) {
            playerNumber = 1;
        }

        file.getStatistics().get(0).getPlayers().get(playerNumber).getVictoryPoints();
        for (int i = 0; i < file.getStatistics().size(); i++) {

            if (turn < file.getStatistics().get(i).getTurn()) {
                resourcesSpentPerTurnPerPlaye.add(new StatisticsDataStructure(turn, (double) countResourcesSpent));
            }

            int lastResources = actualResources;

            actualResources = (file.getStatistics().get(i).getPlayers().get(playerNumber).getResources().getBrick()
                    + file.getStatistics().get(i).getPlayers().get(playerNumber).getResources().getSheep()
                    + file.getStatistics().get(i).getPlayers().get(playerNumber).getResources().getStone()
                    + file.getStatistics().get(i).getPlayers().get(playerNumber).getResources().getWheat()
                    + file.getStatistics().get(i).getPlayers().get(playerNumber).getResources().getWood());

            if (actualResources < lastResources) {
                countResourcesSpent += lastResources - actualResources;
            }

            turn = file.getStatistics().get(i).getTurn();

        }

        return resourcesSpentPerTurnPerPlaye;
    }

    public static ArrayList<StatisticsDataStructure> accumulatedBuildingBuiltPerTurn(String player, SettlersFile file,
            BuildingType buildingType) {
        var resourcesSpentPerTurnPerPlaye = new ArrayList<StatisticsDataStructure>();
        var countBuildingsbuilt = 0;
        var actualBuilding = 0;
        var lastBuilding = 0;
        var turn = 0;
        var playerNumber = 0;

        if (player.contains("Player2")) {
            playerNumber = 1;
        }

        file.getStatistics().get(0).getPlayers().get(playerNumber).getVictoryPoints();
        for (int i = 0; i < file.getStatistics().size(); i++) {

            if (turn < file.getStatistics().get(i).getTurn()) {
                resourcesSpentPerTurnPerPlaye.add(new StatisticsDataStructure(turn, (double) countBuildingsbuilt));
            }

            switch (buildingType) {
            case Road:
                lastBuilding = actualBuilding;
                actualBuilding = file.getStatistics().get(i).getPlayers().get(playerNumber).getStreetCount();

                if (lastBuilding < actualBuilding) {
                    countBuildingsbuilt += (actualBuilding - lastBuilding);
                }
                break;
            case Town:
                lastBuilding = actualBuilding;
                actualBuilding = file.getStatistics().get(i).getPlayers().get(playerNumber).getCityCount();

                if (lastBuilding < actualBuilding) {
                    countBuildingsbuilt += (actualBuilding - lastBuilding);
                }
                break;
            case Village:
                lastBuilding = actualBuilding;
                actualBuilding = file.getStatistics().get(i).getPlayers().get(playerNumber).getVillageCount();

                if (lastBuilding < actualBuilding) {
                    countBuildingsbuilt += (actualBuilding - lastBuilding);
                }
                break;
            default:
                throw new RuntimeException("Building type not implemented!");
            }

            turn = file.getStatistics().get(i).getTurn();
        }

        resourcesSpentPerTurnPerPlaye.add(new StatisticsDataStructure(turn, (double) countBuildingsbuilt));

        return resourcesSpentPerTurnPerPlaye;
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
                .loadFile("563f919a-0991-4d08-8a96-22d86a3f7198.visab2", "Settlers");

        var resourcesGained = SettlersImplicator.concludeResourcesGainedByDice(file);
        var resourcesSpent = SettlersImplicator.concludeResourcesSpent(file);

        var test = accumulatedVictoryPointsPerTurn("Player2", file);
        for (int i = 0; i < test.size(); i++) {
//            System.out.println(test.get(i).getRound() + " : " + test.get(i).getValue());
        }
    }
}
