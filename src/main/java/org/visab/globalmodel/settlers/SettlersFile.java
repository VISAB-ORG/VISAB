package org.visab.globalmodel.settlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.BasicVISABFile;
import org.visab.globalmodel.Rectangle;
import org.visab.workspace.config.ConfigManager;

/**
 * Represents the Settlers VISAB file containing that contains all information
 * necesarry for visualizing. This class is serialized to json and written to
 * .visab files.
 * 
 * @author leonr
 *
 */
public class SettlersFile extends BasicVISABFile {

    private List<SettlersStatistics> statistics = new ArrayList<>();
    private Map<String, String> playerInformation;
    private Map<String, String> playerColors;
    private int playerCount;
    private Rectangle mapRectangle;
    private String winner;

    public SettlersFile() {
        super(ConfigManager.SETTLERS_OF_CATAN_STRING, "2.0");
    }

    public Map<String, String> getPlayerColors() {
        return playerColors;
    }

    public void setPlayerColors(Map<String, String> playerColors) {
        this.playerColors = playerColors;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Map<String, String> getPlayerInformation() {
        return playerInformation;
    }

    public void setPlayerInformation(Map<String, String> playerInformation) {
        this.playerInformation = playerInformation;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public Rectangle getMapRectangle() {
        return mapRectangle;
    }

    public void setMapRectangle(Rectangle mapRectangle) {
        this.mapRectangle = mapRectangle;
    }

    public List<SettlersStatistics> getStatistics() {
        return statistics;
    }

    public List<String> getPlayerNames() {
        return new ArrayList<>(playerInformation.keySet());
    }

}
