package org.visab.globalmodel.cbrshooter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.visab.globalmodel.BasicVISABFile;
import org.visab.globalmodel.Rectangle;
import org.visab.workspace.config.ConfigManager;

public class CBRShooterFile extends BasicVISABFile {

    private List<CBRShooterStatistics> statistics = new ArrayList<>();
    private int playerCount;
    private Rectangle mapRectangle;
    private HashMap<String, String> playerInformation = new HashMap<>();
    private float gameSpeed;

    public CBRShooterFile() {
        super(ConfigManager.CBR_SHOOTER_STRING, "2.0");
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

    public HashMap<String, String> getPlayerInformation() {
        return this.playerInformation;
    }

    public float getGameSpeed() {
        return this.gameSpeed;
    }

    public void setGameSpeed(float gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

}
