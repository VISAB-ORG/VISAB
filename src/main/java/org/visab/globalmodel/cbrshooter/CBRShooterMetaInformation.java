package org.visab.globalmodel.cbrshooter;

import java.util.HashMap;

import org.visab.globalmodel.IMetaInformation;
import org.visab.globalmodel.Rectangle;

// TODO: Decide what to put here.
public class CBRShooterMetaInformation implements IMetaInformation {

    private String game;
    private int playerCount;
    private Rectangle mapRectangle;
    private HashMap<String, String> playerInformation = new HashMap<>();
    private float gameSpeed;

    public int getPlayerCount() {
        return this.playerCount;
    }

    public HashMap<String, String> getPlayerInformation() {
        return playerInformation;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public Rectangle getMapRectangle() {
        return this.mapRectangle;
    }

    public void setMapRectangle(Rectangle mapSize) {
        this.mapRectangle = mapSize;
    }

    public float getGameSpeed() {
        return this.gameSpeed;
    }

    public void setGameSpeed(float gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

    public String setGame() {
        return game;
    }

    @Override
    public String getGame() {
        return game;
    }

}
