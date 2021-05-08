package org.visab.processing.cbrshooter;

import java.util.ArrayList;
import java.util.List;

import org.visab.processing.cbrshooter.model.CBRShooterStatistics;
import org.visab.repository.VISABFileBase;
import org.visab.util.AssignByGame;

public class CBRShooterFile extends VISABFileBase {

    private List<CBRShooterStatistics> statistics = new ArrayList<>();

    /**
     * Used for deserialization
     */
    public CBRShooterFile() {
        super("", "", "");
    }

    public CBRShooterFile(String fileName) {
        super(AssignByGame.CBR_SHOOTER_STRING, "2.0", fileName);
    }

    public List<CBRShooterStatistics> getStatistics() {
        return statistics;
    }

}
