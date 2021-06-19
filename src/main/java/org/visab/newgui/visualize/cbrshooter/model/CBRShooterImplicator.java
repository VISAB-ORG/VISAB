package org.visab.newgui.visualize.cbrshooter.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.IVISABFile;
import org.visab.globalmodel.Vector2;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.globalmodel.cbrshooter.WeaponInformation;
import org.visab.util.StreamUtil;
import org.visab.workspace.Workspace;

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
                // var ammuSpent = player.getMagazineAmmunition() - lastStatistics.getPlayers()
            }
        }

        return null;
    }

    // TODO: This really only works for two players ofcourse.
    // If there are more than two players, we can not conclude which one fired and
    // hit.
    public static Map<String, Double> concludeAimRatio(CBRShooterFile file) {
        var ratios = new HashMap<String, Double>();
        for (var name : file.getPlayerInformation().keySet())
            ratios.put(name, 1.0);

        if (file.getPlayerCount() != 2)
            return ratios;

        var lastHealths = new HashMap<String, Integer>();
        for (var snapshot : file.getStatistics()) {
            for (var player : snapshot.getPlayers()) {
                var name = player.getName();

                var lastHealth = lastHealths.get(name);

            }
        }

    }

    public static Map<String, Integer> concludeHitsTaken(CBRShooterFile file) {
        var hitsTaken = new HashMap<String, Integer>();
        for (var name : file.getPlayerInformation().keySet())
            hitsTaken.put(name, 0);

        if (file.getPlayerCount() != 2)
            return hitsTaken;

        var lastDeaths = new HashMap<String, Integer>();
        var lastHealths = new HashMap<String, Integer>();
        var lastWeapons = new HashMap<String, String>();
        for (var snapshot : file.getStatistics()) {
            for (var player : snapshot.getPlayers()) {
                var name = player.getName();

                var lastHealth = lastHealths.getOrDefault(name, 100);
                var currentHealth = player.getHealth();

                var lastDeath = lastDeaths.getOrDefault(name, 0);
                var currentDeath = player.getStatistics().getDeaths();

                // Calculate the hits taken
                if (lastHealth != currentHealth || lastDeath != currentDeath) {
                    var otherPlayerWeapon = lastWeapons.get(otherPlayerName(name, file));

                    var weaponInfo = file.getWeaponInformation().get(otherPlayerWeapon);
                    if (weaponInfo == null)
                        throw new RuntimeException("Weapon info was null");

                    var hits = 0;

                    // If damage was taken
                    if (currentHealth < lastHealth)
                        hits = (int) Math.ceil((lastHealth - currentHealth) / weaponInfo.getDamage());

                    // If player died
                    if (currentDeath > lastDeath)
                        hits = (int) Math.ceil(lastHealth / weaponInfo.getDamage());

                    // Update hits taken
                    hitsTaken.put(name, hitsTaken.get(name) + hits);
                }
                
                lastHealths.put(name, currentHealth);
                lastDeaths.put(name, currentDeath);
                lastWeapons.put(name, player.getWeapon());
            }
        }

        return hitsTaken;
    }

    public static Map<String, Double> concludeUnitsWalked(CBRShooterFile file) {
        var walked = new HashMap<String, Double>();
        for (var name : file.getPlayerInformation().keySet())
            walked.put(name, 0.0);

        var lastPositions = new HashMap<String, Vector2>();
        for (var snapshot : file.getStatistics()) {
            for (var player : snapshot.getPlayers()) {
                var name = player.getName();

                var lastPos = lastPositions.get(name);
                var currentPos = player.getPosition();

                if (lastPos == null)
                    lastPos = currentPos;

                // Euclidian distance
                var moved = Math.sqrt(Math.pow(lastPos.getX() - currentPos.getX(), 2.0)
                        + Math.pow(lastPos.getY() - currentPos.getY(), 2.0));

                walked.put(name, walked.get(name) + moved);
                lastPositions.put(name, currentPos);
            }
        }

        return walked;
    }

    private static String otherPlayerName(String myPlayer, CBRShooterFile file) {
        if (file.getPlayerCount() > 2)
            return "";

        for (var name : file.getPlayerInformation().keySet()) {
            if (name != myPlayer)
                return name;
        }

        return "";
    }

    public static void main(String[] args) {
        var file = Workspace.getInstance().getDatabaseManager()
                .<CBRShooterFile>loadFile("b833e26c-b232-4870-b14c-2964275cccbe.visab2", "CBRShooter");
        var walked = concludeUnitsWalked(file);
        var hits = concludeHitsTaken(file);
        System.out.println(walked);
    }

}
