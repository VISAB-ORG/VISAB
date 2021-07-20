package org.visab.globalmodel.cbrshooter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.IMetaInformation;
import org.visab.globalmodel.Rectangle;

public class CBRShooterMetaInformation implements IMetaInformation {

    private String game;
    private int playerCount;
    private Rectangle mapRectangle;
    private Map<String, String> playerInformation = new HashMap<>();
    private float gameSpeed;
    private List<WeaponInformation> weaponInformation = new ArrayList<>();
    private Map<String, String> playerColors = new HashMap<>();

    public List<WeaponInformation> getWeaponInformation() {
        return weaponInformation;
    }

    public Map<String, String> getPlayerColors() {
        return playerColors;
    }

    public void setPlayerColors(Map<String, String> playerColors) {
        this.playerColors = playerColors;
    }

    public int getPlayerCount() {
        return this.playerCount;
    }

    public Map<String, String> getPlayerInformation() {
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
