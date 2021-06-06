package org.visab.globalmodel.settlers;

/**
 * Represents all available information for a player in (Unity-) Settlers of
 * Catan that is given at a certain time in the game.
 * 
 * @author leonr
 *
 */
public class PlayerInformation {
    private String name;
    private int brick;
    private int wheat;
    private int sheep;
    private int wood;
    private int stone;
    private int victoryPoints;
    private float roadRange;
    private int longestRoad;
    private boolean isAi;
    private boolean freeBuildRoad;
    private boolean freeBuild;
    private boolean hasLongestRoad;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBrick() {
        return brick;
    }

    public void setBrick(int brick) {
        this.brick = brick;
    }

    public int getWheat() {
        return wheat;
    }

    public void setWheat(int wheat) {
        this.wheat = wheat;
    }

    public int getSheep() {
        return sheep;
    }

    public void setSheep(int sheep) {
        this.sheep = sheep;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public float getRoadRange() {
        return roadRange;
    }

    public void setRoadRange(float roadRange) {
        this.roadRange = roadRange;
    }

    public int getLongestRoad() {
        return longestRoad;
    }

    public void setLongestRoad(int longestRoad) {
        this.longestRoad = longestRoad;
    }

    public boolean isAi() {
        return isAi;
    }

    public void setAi(boolean isAi) {
        this.isAi = isAi;
    }

    public boolean isFreeBuildRoad() {
        return freeBuildRoad;
    }

    public void setFreeBuildRoad(boolean freeBuildRoad) {
        this.freeBuildRoad = freeBuildRoad;
    }

    public boolean isFreeBuild() {
        return freeBuild;
    }

    public void setFreeBuild(boolean freeBuild) {
        this.freeBuild = freeBuild;
    }

    public boolean isHasLongestRoad() {
        return hasLongestRoad;
    }

    public void setHasLongestRoad(boolean hasLongestRoad) {
        this.hasLongestRoad = hasLongestRoad;
    }

}