package org.visab.globalmodel.settlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.BasicVISABFile;
import org.visab.globalmodel.Rectangle;
import org.visab.workspace.config.ConfigManager;

/**
 * This class represents the structure of a VISAB data file generated from
 * Settlers of Catan data.
 * 
 * @author leonr
 *
 */
public class SettlersFile extends BasicVISABFile {

    private List<SettlersStatistics> statistics = new ArrayList<>();
    private Map<String, String> playerInformation;
    private int playerCount;
    private Rectangle mapRectangle;

    public SettlersFile() {
        super(ConfigManager.SETTLERS_OF_CATAN_STRING, "2.0");
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
