package org.visab.globalmodel.settlers;

import java.util.List;

import org.visab.globalmodel.Vector2;

/**
 * Represents all available information for a player in (Unity-) Settlers of
 * Catan that is given at a certain time in the game.
 * 
 * @author leonr
 *
 */
public class PlayerInformation {

    private int villageCount;
    private List<Vector2> villagePositions = null;
    private int streetCount;
    private List<Vector2> streetPositions = null;
    private int cityCount;
    private List<Vector2> cityPositions = null;
    private PlayerResources resources;
    private boolean hasLongestRoad;
    private boolean isAi;
    private int longestRoad;
    private String name;
    private List<String> planActions = null;
    private int victoryPoints;

    public int getVillageCount() {
        return villageCount;
    }

    public void setVillageCount(int villageCount) {
        this.villageCount = villageCount;
    }

    public List<Vector2> getVillagePositions() {
        return villagePositions;
    }

    public void setVillagePositions(List<Vector2> villagePositions) {
        this.villagePositions = villagePositions;
    }

    public int getStreetCount() {
        return streetCount;
    }

    public void setStreetCount(int streetCount) {
        this.streetCount = streetCount;
    }

    public List<Vector2> getStreetPositions() {
        return streetPositions;
    }

    public void setStreetPositions(List<Vector2> streetPositions) {
        this.streetPositions = streetPositions;
    }

    public int getCityCount() {
        return cityCount;
    }

    public void setCityCount(int cityCount) {
        this.cityCount = cityCount;
    }

    public List<Vector2> getCityPositions() {
        return cityPositions;
    }

    public void setCityPositions(List<Vector2> cityPositions) {
        this.cityPositions = cityPositions;
    }

    public PlayerResources getResources() {
        return resources;
    }

    public void setResources(PlayerResources resources) {
        this.resources = resources;
    }

    public boolean isHasLongestRoad() {
        return hasLongestRoad;
    }

    public void setHasLongestRoad(boolean hasLongestRoad) {
        this.hasLongestRoad = hasLongestRoad;
    }

    public boolean isIsAi() {
        return isAi;
    }

    public void setIsAi(boolean isAi) {
        this.isAi = isAi;
    }

    public int getLongestRoad() {
        return longestRoad;
    }

    public void setLongestRoad(int longestRoad) {
        this.longestRoad = longestRoad;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPlanActions() {
        return planActions;
    }

    public void setPlanActions(List<String> planActions) {
        this.planActions = planActions;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

}