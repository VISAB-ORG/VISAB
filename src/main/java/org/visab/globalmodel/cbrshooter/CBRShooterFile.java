package org.visab.globalmodel.cbrshooter;

import java.util.ArrayList;
import java.util.List;

import org.visab.globalmodel.VISABFileBase;
import org.visab.util.AssignByGame;

public class CBRShooterFile extends VISABFileBase {

    private List<CBRShooterStatistics> statistics = new ArrayList<>();

    public CBRShooterFile() {
        super(AssignByGame.CBR_SHOOTER_STRING, "2.0");
    }

    public List<CBRShooterStatistics> getStatistics() {
        return statistics;
    }

}