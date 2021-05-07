package org.visab.processing.starter;

import java.util.ArrayList;
import java.util.List;

import org.visab.processing.VISABFileBase;

public class DefaultFile extends VISABFileBase {

    /**
     * Used for deserialization
     */
    public DefaultFile() {
        super("", "", "");
    }

    public DefaultFile(String game, String fileName) {
        super(game, "2.0", fileName);
    }

    private List<String> statistics = new ArrayList<>();

    public List<String> getStatistics() {
        return this.statistics;
    }

}
