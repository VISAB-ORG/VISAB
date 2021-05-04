package org.visab.processing.settlersofcatan;

import org.visab.processing.VISABFileBase;
import org.visab.util.AssignByGame;

public class SettlersFile extends VISABFileBase {

    /**
     * Used for deserialization
     */
    public SettlersFile() {
        super("", "", "");
    }

    public SettlersFile(String fileName) {
        super(AssignByGame.SETTLERS_OF_CATAN_STRING, "2.0", fileName);
    }
}
