package org.visab.globalmodel.cbrshooter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.BasicVISABFile;
import org.visab.globalmodel.Rectangle;
import org.visab.workspace.config.ConfigManager;

public class CBRShooterFile extends BasicVISABFile {

    private List<CBRShooterStatistics> statistics = new ArrayList<>();
    private int playerCount;
    private Rectangle mapRectangle;
    private Map<String, String> playerInformation = new HashMap<>();
    private float gameSpeed;
    private List<WeaponInformation> weaponInformation = new ArrayList<>();

    public CBRShooterFile() {
        super(ConfigManager.CBR_SHOOTER_STRING, "2.0");
    }

    public List<WeaponInformation> getWeaponInformation() {
        return weaponInformation;
    }

    public List<CBRShooterStatistics> getStatistics() {
        return statistics;
    }

    public int getPlayerCount() {
        return this.playerCount;
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

    public Map<String, String> getPlayerInformation() {
        return this.playerInformation;
    }

    public float getGameSpeed() {
        return this.gameSpeed;
    }

    public void setGameSpeed(float gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

}
