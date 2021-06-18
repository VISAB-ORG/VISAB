package org.visab.newgui.visualize.cbrshooter.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.IVISABFile;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;

public final class CBRShooterImplicator {

    public static Map<String, Integer> concludeShotTaken(CBRShooterFile file) {
        var shots = new HashMap<String, Integer>();
        

        var statistics = file.getStatistics();
        for (int i = 1; i < statistics.size(); i++) {
            var previous = statistics.get(i - 1);
            var current = statistics.get(i);

            for (var player : previous.getPlayers()) {
            }
        }

        CBRShooterStatistics lastStatistics = null;
        for (var stats : file.getStatistics()) {
            var players = stats.getPlayers();
            for (int i = 0; i < players.size(); i++) {
                var player = players.get(i);
                var ammuSpent = player.getMagazineAmmunition() - lastStatistics.getPlayers()
            }
        }
    }

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
