package org.visab.globalmodel.settlers;

import org.visab.util.StringFormat;

/**
 * Represents a snapshot of the resources availible.
 */
public class PlayerResources {

    private int sheep;
    private int brick;
    private int stone;
    private int wheat;
    private int wood;

    public int getSheep() {
        return sheep;
    }

    public void setSheep(int sheep) {
        this.sheep = sheep;
    }

    public int getBrick() {
        return brick;
    }

    public void setBrick(int brick) {
        this.brick = brick;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getWheat() {
        return wheat;
    }

    public void setWheat(int wheat) {
        this.wheat = wheat;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    /**
     * Adds two PlayerResources by adding their detailed resources.
     * 
     * @param one The first PlayerResources instance
     * @param two The second PlayerResources instance
     * @return A new PlayerResource instance with the cumulated resources
     */
    public static PlayerResources add(PlayerResources one, PlayerResources two) {
        var newResources = new PlayerResources();
        newResources.brick = one.brick + two.brick;
        newResources.sheep = one.sheep + two.sheep;
        newResources.stone = one.stone + two.stone;
        newResources.wheat = one.wheat + two.wheat;
        newResources.wood = one.wood + two.wood;

        return newResources;
    }

    /**
     * Adds two PlayerResources by subtracting their detailed resources.
     * 
     * @param substractFrom The resources to substract from
     * @param substract     The resources to substract
     * @return A new PlayerResource instance with substracted resources
     */
    public static PlayerResources sub(PlayerResources substractFrom, PlayerResources substract) {
        var newResources = new PlayerResources();
        newResources.brick = substractFrom.brick - substract.brick;
        newResources.sheep = substractFrom.sheep - substract.sheep;
        newResources.stone = substractFrom.stone - substract.stone;
        newResources.wheat = substractFrom.wheat - substract.wheat;
        newResources.wood = substractFrom.wood - substract.wood;

        return newResources;
    }

    @Override
    public String toString() {
        return StringFormat.niceString("{ Brick:{0}, Sheep:{1}, Stone:{2}, Wheat:{3}, Wood:{4}", brick, sheep, stone,
                wheat, wood);
    }
}
