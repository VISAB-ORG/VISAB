package org.visab.processing.settlersofcatan;

import org.visab.repository.VISABFileBase;
import org.visab.util.AssignByGame;

public class SettlersFile extends VISABFileBase {

    public SettlersFile() {
        super(AssignByGame.SETTLERS_OF_CATAN_STRING, "2.0");
    }
}
