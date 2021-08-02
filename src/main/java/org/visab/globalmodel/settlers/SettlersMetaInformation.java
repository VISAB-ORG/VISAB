package org.visab.globalmodel.settlers;

import java.util.Map;

import org.visab.globalmodel.IMetaInformation;
import org.visab.globalmodel.Rectangle;

public class SettlersMetaInformation implements IMetaInformation {

    private String game;
    private Map<String, String> playerInformation;
    private int playerCount;
    private Rectangle mapRectangle;
    private Map<String, String> playerColors;

    public String setGame() {
        return game;
    }

    public Map<String, String> getPlayerColors() {
        return playerColors;
    }

    public void setPlayerColors(Map<String, String> playerColors) {
        this.playerColors = playerColors;
    }

    public Rectangle getMapRectangle() {
        return mapRectangle;
    }

    public void setMapRectangle(Rectangle mapRectangle) {
        this.mapRectangle = mapRectangle;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public Map<String, String> getPlayerInformation() {
        return playerInformation;
    }

    public void setPlayerInformation(Map<String, String> playerInformation) {
        this.playerInformation = playerInformation;
    }

    @Override
    public String getGame() {
        return game;
    }

}
