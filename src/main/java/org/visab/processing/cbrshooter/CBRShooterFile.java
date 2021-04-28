package org.visab.processing.cbrshooter;

import java.util.ArrayList;
import java.util.List;

import org.visab.processing.VISABFileBase;
import org.visab.processing.cbrshooter.model.CBRShooterStatistics;

public class CBRShooterFile extends VISABFileBase {

    private List<CBRShooterStatistics> statistics = new ArrayList<>();

    public CBRShooterFile() {
        super("CBRShooter", "2.0");
    }

    public List<CBRShooterStatistics> getStatistics() {
        return statistics;
    }

}
