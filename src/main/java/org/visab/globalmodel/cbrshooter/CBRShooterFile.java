package org.visab.globalmodel.cbrshooter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.BasicVISABFile;
import org.visab.globalmodel.GameName;
import org.visab.globalmodel.Rectangle;

/**
 * Represents the CBRShooter VISAB file containing that contains all information necesarry
 * for visualizing. This class is serialized to json and written to .visab
 * files.
 */
public class CBRShooterFile extends BasicVISABFile {

    private List<CBRShooterStatistics> statistics = new ArrayList<>();
    private int playerCount;
    private Rectangle mapRectangle;
    private Map<String, String> playerInformation = new HashMap<>();
    private float gameSpeed;
    private List<WeaponInformation> weaponInformation = new ArrayList<>();
    private Map<String, String> playerColors = new HashMap<>();
    private CBRShooterImages images;
    private String winner;

    public CBRShooterFile() {
        super(GameName.CBR_SHOOTER, "2.0");
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public CBRShooterImages getImages() {
        return images;
    }

    public void setImages(CBRShooterImages images) {
        this.images = images;
    }

    public Map<String, String> getPlayerColors() {
        return playerColors;
    }

    public void setPlayerColors(Map<String, String> playerColors) {
        this.playerColors = playerColors;
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

    public List<String> getPlayerNames() {
        return new ArrayList<>(playerInformation.keySet());
    }

}
