package org.visab.globalmodel.starter;

import java.util.ArrayList;
import java.util.List;

import org.visab.globalmodel.BasicVISABFile;

public class DefaultFile extends BasicVISABFile {

    /**
     * Used for deserialization
     */
    public DefaultFile() {
        super("", "");
    }

    public DefaultFile(String game) {
        super(game, "2.0");
    }

    private List<String> statistics = new ArrayList<>();

    public List<String> getStatistics() {
        return this.statistics;
    }

}
