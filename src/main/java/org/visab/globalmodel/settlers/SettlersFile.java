package org.visab.globalmodel.settlers;

import java.util.ArrayList;
import java.util.List;

import org.visab.globalmodel.BasicVISABFile;
import org.visab.workspace.config.ConfigManager;

/**
 * This class represents the structure of a VISAB data file
 * generated from Settlers of Catan data.
 * 
 * @author leonr
 *
 */
public class SettlersFile extends BasicVISABFile {

    private List<SettlersStatistics> statistics = new ArrayList<>();

    public SettlersFile() {
        super(ConfigManager.SETTLERS_OF_CATAN_STRING, "2.0");
    }

    public List<SettlersStatistics> getStatistics() {
        return statistics;
    }

}
