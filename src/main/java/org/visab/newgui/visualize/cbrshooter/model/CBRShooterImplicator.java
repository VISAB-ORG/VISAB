package org.visab.newgui.visualize.cbrshooter.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;

public final class CBRShooterImplicator {

    // TODO: This really only works for two players ofcourse.
    // If there are more than two players, we can not conclude which one fired and
    // hit.
    public static Map<String, Double> concludeAimRatio(List<CBRShooterStatistics> statistics) {
        var ratios = new HashMap<>();
        for (var player : statistics.get(0).getPlayers()) {
            
        }

        for (var stats : ) {
            
        }


    }

}
