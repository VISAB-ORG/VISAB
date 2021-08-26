package org.visab.gui.visualize.settlers.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.settlers.PlayerResources;
import org.visab.globalmodel.settlers.SettlersStatistics;
import org.visab.gui.visualize.StatisticsData;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

/**
 * Helper class for concluding information based on a list of given Settlers
 * statistics.
 */
public final class SettlersImplicator {

    public static List<StatisticsData<Double>> accumulatedVictoryPointsPerTurn(String player,
            List<SettlersStatistics> statisticsList) {
        var victoryPointsPerTurnPerPlayer = new ArrayList<StatisticsData<Double>>();
        var countVictoryPoints = 0;
        var turn = 0;
        var playerNumber = 0;

        if (player.contains("Player2")) {
            playerNumber = 1;
        }

        statisticsList.get(0).getPlayers().get(playerNumber).getVictoryPoints();
        for (int i = 0; i < statisticsList.size(); i++) {

            if (turn < statisticsList.get(i).getTurn()) {
                victoryPointsPerTurnPerPlayer.add(new StatisticsData<Double>(turn, (double) countVictoryPoints));
            }

            countVictoryPoints = statisticsList.get(i).getPlayers().get(playerNumber).getVictoryPoints();

            turn = statisticsList.get(i).getTurn();

        }

        victoryPointsPerTurnPerPlayer.add(new StatisticsData<Double>(turn, (double) countVictoryPoints));

        return victoryPointsPerTurnPerPlayer;
    }

    public static List<StatisticsData<PlayerResources>> accumulatedResourcesGainedPerTurn(String player,
            List<SettlersStatistics> statisticsList) {
        var resourcesGaintPerTurnPerPlayer = new ArrayList<StatisticsData<PlayerResources>>();
        var countResourcesGained = new PlayerResources();
        var turn = 0;
        var playerNumber = 0;

        if (player.contains("Player2")) {
            playerNumber = 1;
        }

        for (var statistics : statisticsList) {

            if (turn < statistics.getTurn()) {
                resourcesGaintPerTurnPerPlayer.add(new StatisticsData<PlayerResources>(turn, countResourcesGained));
            }
            var playerData = statistics.getPlayers().get(playerNumber);
            countResourcesGained = PlayerResources.add(countResourcesGained, playerData.getResourcesGained());

            turn = statistics.getTurn();
        }

        return resourcesGaintPerTurnPerPlayer;
    }

    public static List<StatisticsData<PlayerResources>> accumulatedResourcesSpentPerTurn(String player,
            List<SettlersStatistics> statisticsList) {
        var resourcesSpentPerTurnPerPlayer = new ArrayList<StatisticsData<PlayerResources>>();
        var countResourcesSpent = new PlayerResources();
        var currentTurnEndResources = new PlayerResources();
        var playerData = new PlayerResources();
        var turn = 0;
        var playerNumber = 0;

        if (player.contains("Player2")) {
            playerNumber = 1;
        }

        for (var statistics : statisticsList) {

            if (turn < statistics.getTurn()) {
                resourcesSpentPerTurnPerPlayer.add(new StatisticsData<PlayerResources>(turn, countResourcesSpent));
                countResourcesSpent = new PlayerResources();
            }

            var lastTurn = currentTurnEndResources;
            currentTurnEndResources = statistics.getPlayers().get(playerNumber).getResources();
            var currentTurnGainedResources = statistics.getPlayers().get(playerNumber).getResourcesGained();

            if (lastTurn.getBrick()
                    - (currentTurnEndResources.getBrick() + currentTurnGainedResources.getBrick()) >= 0) {
                playerData.setBrick(lastTurn.getBrick()
                        - (currentTurnEndResources.getBrick() + currentTurnGainedResources.getBrick()));
            }
            if (lastTurn.getStone()
                    - (currentTurnEndResources.getStone() + currentTurnGainedResources.getStone()) >= 0) {
                playerData.setStone(lastTurn.getStone()
                        - (currentTurnEndResources.getStone() + currentTurnGainedResources.getStone()));
            }
            if (lastTurn.getSheep()
                    - (currentTurnEndResources.getSheep() + currentTurnGainedResources.getSheep()) >= 0) {
                playerData.setSheep(lastTurn.getSheep()
                        - (currentTurnEndResources.getSheep() + currentTurnGainedResources.getSheep()));
            }
            if (lastTurn.getWood() - (currentTurnEndResources.getWood() + currentTurnGainedResources.getWood()) >= 0) {
                playerData.setWood(lastTurn.getWood()
                        - (currentTurnEndResources.getWood() + currentTurnGainedResources.getWood()));
            }
            if (lastTurn.getWheat()
                    - (currentTurnEndResources.getWheat() + currentTurnGainedResources.getWheat()) >= 0) {
                playerData.setWheat(lastTurn.getWheat()
                        - (currentTurnEndResources.getWheat() + currentTurnGainedResources.getWheat()));
            }

            countResourcesSpent = PlayerResources.add(countResourcesSpent, playerData);

            turn = statistics.getTurn();
        }

        return resourcesSpentPerTurnPerPlayer;
    }

    public static List<StatisticsData<Double>> accumulatedBuildingBuiltPerTurn(String player,
            List<SettlersStatistics> statisticsList, BuildingType buildingType) {
        var resourcesSpentPerTurnPerPlayer = new ArrayList<StatisticsData<Double>>();
        var countBuildingsbuilt = 0;
        var actualBuilding = 0;
        var lastBuilding = 0;
        var turn = 0;
        var playerNumber = 0;

        if (player.contains("Player2")) {
            playerNumber = 1;
        }

        statisticsList.get(0).getPlayers().get(playerNumber).getVictoryPoints();
        for (int i = 0; i < statisticsList.size(); i++) {

            if (turn < statisticsList.get(i).getTurn()) {
                resourcesSpentPerTurnPerPlayer.add(new StatisticsData<Double>(turn, (double) countBuildingsbuilt));
            }

            switch (buildingType) {
            case Road:
                lastBuilding = actualBuilding;
                actualBuilding = statisticsList.get(i).getPlayers().get(playerNumber).getStreetCount();

                if (lastBuilding < actualBuilding) {
                    countBuildingsbuilt += (actualBuilding - lastBuilding);
                }
                break;
            case Town:
                lastBuilding = actualBuilding;
                actualBuilding = statisticsList.get(i).getPlayers().get(playerNumber).getCityCount();

                if (lastBuilding < actualBuilding) {
                    countBuildingsbuilt += (actualBuilding - lastBuilding);
                }
                break;
            case Village:
                lastBuilding = actualBuilding;
                actualBuilding = statisticsList.get(i).getPlayers().get(playerNumber).getVillageCount();

                if (lastBuilding < actualBuilding) {
                    countBuildingsbuilt += (actualBuilding - lastBuilding);
                }
                break;
            default:
                throw new RuntimeException("Building type not implemented!");
            }

            turn = statisticsList.get(i).getTurn();
        }

        resourcesSpentPerTurnPerPlayer.add(new StatisticsData<Double>(turn, (double) countBuildingsbuilt));

        return resourcesSpentPerTurnPerPlayer;
    }

    private static List<Series<String, Number>> createResourceSeries(List<StatisticsData<PlayerResources>> player1Data,
            List<StatisticsData<PlayerResources>> player2Data) {

        Series<String, Number> woodSeries = new Series<>();
        Series<String, Number> sheepSeries = new Series<>();
        Series<String, Number> stoneSeries = new Series<>();
        Series<String, Number> wheatSeries = new Series<>();
        Series<String, Number> brickSeries = new Series<>();
        sheepSeries.setName("Sheep");
        woodSeries.setName("Wood");
        stoneSeries.setName("Stone");
        wheatSeries.setName("Wheat");
        brickSeries.setName("Brick");

        for (int i = 0; i < player1Data.size(); i++) {
            var resourcesPlayer1 = player1Data.get(i).getValue();
            var resourcesPlayer2 = player2Data.get(i).getValue();

            woodSeries.getData().add(
                    new XYChart.Data<String, Number>(String.valueOf(i + 1) + " - Player 1", resourcesPlayer1.getWood()));
            woodSeries.getData().add(
                    new XYChart.Data<String, Number>(String.valueOf(i + 1) + " - Player 2", resourcesPlayer2.getWood()));
            sheepSeries.getData().add(
                    new XYChart.Data<String, Number>(String.valueOf(i + 1) + " - Player 1", resourcesPlayer1.getSheep()));
            sheepSeries.getData().add(
                    new XYChart.Data<String, Number>(String.valueOf(i + 1) + " - Player 2", resourcesPlayer2.getSheep()));
            stoneSeries.getData().add(
                    new XYChart.Data<String, Number>(String.valueOf(i + 1) + " - Player 1", resourcesPlayer1.getStone()));
            stoneSeries.getData().add(
                    new XYChart.Data<String, Number>(String.valueOf(i + 1) + " - Player 2", resourcesPlayer2.getStone()));
            wheatSeries.getData().add(
                    new XYChart.Data<String, Number>(String.valueOf(i + 1) + " - Player 1", resourcesPlayer1.getWheat()));
            wheatSeries.getData().add(
                    new XYChart.Data<String, Number>(String.valueOf(i + 1) + " - Player 2", resourcesPlayer2.getWheat()));
            brickSeries.getData().add(
                    new XYChart.Data<String, Number>(String.valueOf(i + 1) + " - Player 1", resourcesPlayer1.getBrick()));
            brickSeries.getData().add(
                    new XYChart.Data<String, Number>(String.valueOf(i + 1) + " - Player 2", resourcesPlayer2.getBrick()));
        }

        return Arrays.asList(woodSeries, sheepSeries, stoneSeries, wheatSeries, brickSeries);

    }

    public static final List<Series<String, Number>> resourcesSpentSeries(List<SettlersStatistics> statisticsList) {
        var resourcesPlayer1 = accumulatedResourcesSpentPerTurn("Player1", statisticsList);
        var resourcesPlayer2 = accumulatedResourcesSpentPerTurn("Player2", statisticsList);

        return createResourceSeries(resourcesPlayer1, resourcesPlayer2);
    }

    public static final List<Series<String, Number>> resourcesGainedSeries(List<SettlersStatistics> statisticsList) {
        var resourcesPlayer1 = accumulatedResourcesGainedPerTurn("Player1", statisticsList);
        var resourcesPlayer2 = accumulatedResourcesGainedPerTurn("Player2", statisticsList);

        return createResourceSeries(resourcesPlayer1, resourcesPlayer2);
    }

    public enum BuildingType {
        Town, Village, Road
    }

    public enum ResourceType {
        Brick, Sheep, Stone, Wheat, Wood
    }

    public static final List<String> getResourceNames() {
        List<String> resources = new ArrayList<>();
        resources.addAll(Arrays.asList(ResourceType.values().toString()));

        return resources;
    }

    public static final Map<String, PlayerResources> concludeResourcesGainedByDice(
            List<SettlersStatistics> statisticsList, List<String> playerNames) {
        var resourcesGained = new HashMap<String, PlayerResources>();
        for (String name : playerNames)
            resourcesGained.put(name, new PlayerResources());

        for (var statistics : statisticsList) {
            for (var player : statistics.getPlayers()) {
                var name = player.getName();

                var untilNow = resourcesGained.get(name);
                var current = player.getResourcesGained();

                resourcesGained.put(name, PlayerResources.add(untilNow, current));
            }
        }

        return resourcesGained;
    }

    public static final Map<String, PlayerResources> concludeResourcesSpent(List<SettlersStatistics> statisticsList,
            List<String> playerNames) {
        var resourcesSpent = new HashMap<String, PlayerResources>();
        for (String name : playerNames)
            resourcesSpent.put(name, new PlayerResources());

        var resourcesLastTurn = new HashMap<String, PlayerResources>();
        for (var statistics : statisticsList) {
            for (var player : statistics.getPlayers()) {
                if (!player.isMyTurn()) {
                    resourcesLastTurn.put(player.getName(), player.getResources());

                    continue;
                }

                var name = player.getName();
                var lastTurn = resourcesLastTurn.getOrDefault(name, new PlayerResources());

                PlayerResources gainedThisTurn = player.getResourcesGained();

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

}