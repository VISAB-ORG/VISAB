package org.visab.newgui.visualize.cbrshooter.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.visab.globalmodel.Vector2;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.visualize.StatisticsDataStructure;
import org.visab.util.StreamUtil;
import org.visab.workspace.Workspace;

public final class CBRShooterImplicator {

    public static ArrayList<StatisticsDataStructure> shotsPerRound(String player, CBRShooterFile file) {
        var shotsPerRoundPerPlayer = new ArrayList<StatisticsDataStructure>();
        var countShots = 0;
        var maxAmmunition = 0;
        var currentAmmunition = 0;
        var round = 0;
        var playerNumber = 0;
        if (player.contains("Jane Doe")) {
            playerNumber = 1;
        }

        for (int i = 0; i < file.getStatistics().size(); i++) {

            if (round < file.getStatistics().get(i).getRound()) {
                shotsPerRoundPerPlayer.add(new StatisticsDataStructure((double) round, countShots));
                countShots = 0;
            }
            maxAmmunition = currentAmmunition;
            currentAmmunition = file.getStatistics().get(i).getPlayers().get(playerNumber).getTotalAmmunition();
            
            if (currentAmmunition < maxAmmunition) {
                countShots += (maxAmmunition - currentAmmunition);
            }

            round = file.getStatistics().get(i).getRound();
        }

        return shotsPerRoundPerPlayer;
    }
    
    public static ArrayList<StatisticsDataStructure> collectedCollectablesPerRound(String player, CBRShooterFile file,
            Collectable collectable) {
        var collectedCollectablesPerRoundPerPlayer = new ArrayList<StatisticsDataStructure>();
        var countCollectedCollectables = 0;
        var round = 0;
        var playerNumber = 0;
        if (player.contains("Jane Doe")) {
            playerNumber = 1;
        }
        
        for (int i = 0; i < file.getStatistics().size(); i++) {

            if (round < file.getStatistics().get(i).getRound()) {
                collectedCollectablesPerRoundPerPlayer.add(new StatisticsDataStructure((double) round, countCollectedCollectables));
                countCollectedCollectables = 0;
            }
            
            switch (collectable) {
            case Health:
                var lastHealth = file.getStatistics().get(i).getPlayers().get(playerNumber).getHealth();
                var currentHealth = file.getStatistics().get(i).getPlayers().get(playerNumber).getHealth();
                
                if (currentHealth > lastHealth) {
                    countCollectedCollectables++;
                }
                break;
            case Ammunition:
                var lastAmmu = file.getStatistics().get(i).getPlayers().get(playerNumber).getTotalAmmunition();
                var currentAmmu = file.getStatistics().get(i).getPlayers().get(playerNumber).getTotalAmmunition();
                
                if (currentAmmu > lastAmmu) {
                    countCollectedCollectables++;
                }
                break;
            case Weapon:
                var lastWeapon = file.getStatistics().get(i).getPlayers().get(playerNumber).getWeapon();
                var currentWeapon = file.getStatistics().get(i).getPlayers().get(playerNumber).getWeapon();

                if (!currentWeapon.equals(lastWeapon)) {
                    countCollectedCollectables++;
                }
                break;
            default:
                break;
            }

            round = file.getStatistics().get(i).getRound();
        }

        return collectedCollectablesPerRoundPerPlayer;
    }
    
    public static ArrayList<StatisticsDataStructure> aimRatioPerRound(String player, CBRShooterFile file) {
        // aim Ratio = hits / shotsFired
        var aimRatioPerRoundPerPlayer = new ArrayList<StatisticsDataStructure>();
        var shotsFired = shotsPerRound(player, file);
        var hits = hitsOnEnemyPerRound(player, file);

        for (int i = 0; i < shotsFired.size(); i++) {

            if (shotsFired.get(i).getParameter() > 0) {
                aimRatioPerRoundPerPlayer.add(new StatisticsDataStructure((double) i, 
                        (hits.get(i).getParameter() / shotsFired.get(i).getParameter())));
            } else {
                aimRatioPerRoundPerPlayer.add(new StatisticsDataStructure((double) i, 0));
            }
                
        }

        return aimRatioPerRoundPerPlayer;
    }
    
    public static ArrayList<StatisticsDataStructure> hitsOnEnemyPerRound(String player, CBRShooterFile file) {
        var hitsTakenPerRoundPerPlayer = new ArrayList<StatisticsDataStructure>();
        var countHits = 0;
        var lastHealth = 100;
        var currentHealth = 0;
        var round = 0;
        var playerNumber = 1;
        if (player.contains("Jane Doe")) {
            playerNumber = 0;
        }

        for (int i = 0; i < file.getStatistics().size(); i++) {

            if (round < file.getStatistics().get(i).getRound()) {
                hitsTakenPerRoundPerPlayer.add(new StatisticsDataStructure((double) round, countHits));
                countHits = 0;
            }
            lastHealth = currentHealth;
            currentHealth = file.getStatistics().get(i).getPlayers().get(playerNumber).getHealth();
            
            if (currentHealth < lastHealth) {
                countHits += (int) Math.ceil((lastHealth - currentHealth) / file.getWeaponInformation().get(playerNumber).getDamage());
            }

            round = file.getStatistics().get(i).getRound();
            file.getStatistics().get(i).getPlayers().get(playerNumber).getPosition();
        }

        return hitsTakenPerRoundPerPlayer;
    }
    
    public static ArrayList<StatisticsDataStructure> unitsWalkedPerRound(String player, CBRShooterFile file) {
        var accumulatedDeathsPerRoundPerPlayer = new ArrayList<StatisticsDataStructure>();
        var unitsWalked = 0;
        var currentPos = new Vector2();
        var moved = 0.0;
        var round = 0;
        var playerNumber = 0;
        if (player.contains("Jane Doe")) {
            playerNumber = 1;
        }

        for (int i = 0; i < file.getStatistics().size(); i++) {

            if (round < file.getStatistics().get(i).getRound()) {
                accumulatedDeathsPerRoundPerPlayer.add(new StatisticsDataStructure((double) round, unitsWalked));
                unitsWalked = 0;
            }
            
            var lastPos = currentPos;
            currentPos = file.getStatistics().get(i).getPlayers().get(playerNumber).getPosition();
            
            if (lastPos != currentPos) {
                moved = Math.sqrt(Math.pow(lastPos.getX() - currentPos.getX(), 2.0)
                        + Math.pow(lastPos.getY() - currentPos.getY(), 2.0));
            }
               
            unitsWalked += moved;
            round = file.getStatistics().get(i).getRound();
        }

        return accumulatedDeathsPerRoundPerPlayer;
    }
    
    public static ArrayList<StatisticsDataStructure> accumulatedDeathsPerRound(String player, CBRShooterFile file) {
        var accumulatedDeathsPerRoundPerPlayer = new ArrayList<StatisticsDataStructure>();
        var round = 0;
        var playerNumber = 0;
        if (player.contains("Jane Doe")) {
            playerNumber = 1;
        }

        for (int i = 0; i < file.getStatistics().size(); i++) {

            if (round < file.getStatistics().get(i).getRound()) {
                accumulatedDeathsPerRoundPerPlayer.add(new StatisticsDataStructure((double) round, 
                        file.getStatistics().get(i).getPlayers().get(playerNumber).getStatistics().getDeaths()));
            }

            round = file.getStatistics().get(i).getRound();
        }

        return accumulatedDeathsPerRoundPerPlayer;
    }
    
    public static ArrayList<StatisticsDataStructure> accumulatedKillsPerRound(String player, CBRShooterFile file) {
        var accumulatedKillsPerRoundPerPlayer = new ArrayList<StatisticsDataStructure>();
        var round = 0;
        var playerNumber = 1;
        if (player.contains("Jane Doe")) {
            playerNumber = 0;
        }

        for (int i = 0; i < file.getStatistics().size(); i++) {

            if (round < file.getStatistics().get(i).getRound()) {
                accumulatedKillsPerRoundPerPlayer.add(new StatisticsDataStructure((double) round, 
                        file.getStatistics().get(i).getPlayers().get(playerNumber).getStatistics().getDeaths()));
            }

            round = file.getStatistics().get(i).getRound();
        }

        return accumulatedKillsPerRoundPerPlayer;
    }

    public static boolean wasCollected(Collectable collectable, CBRShooterStatistics last,
            CBRShooterStatistics current) {

        if (last == null || current == null)
            return false;

        switch (collectable) {
        case Ammunition:
            return last.getIsAmmunitionCollected() == false && current.getIsAmmunitionCollected();
        case Health:
            return last.getIsHealthCollected() == false && current.getIsHealthCollected();
        case Weapon:
            return last.getIsWeaponCollected() == false && current.getIsWeaponCollected();
        default:
            return false;
        }
    }

    public static boolean playerCollected(String name, Collectable collectable, CBRShooterStatistics last,
            CBRShooterStatistics current) {
        if (last == null || current == null)
            return false;

        var lastPlayerStats = StreamUtil.firstOrNull(last.getPlayers(), x -> x.getName().equals(name));
        var currentPlayerStats = StreamUtil.firstOrNull(current.getPlayers(), x -> x.getName().equals(name));

        var lastRound = last.getRound();
        var currentRound = last.getRound();
        if (lastRound == currentRound)
            return false;

        var wasCollected = wasCollected(collectable, last, current);
        if (!wasCollected)
            return false;

        switch (collectable) {
        case Health:
            var lastHealth = lastPlayerStats.getHealth();
            var currentHealth = currentPlayerStats.getHealth();

            return currentHealth >= lastHealth;
        case Ammunition:
            var lastAmmu = lastPlayerStats.getTotalAmmunition();
            var currentAmmu = currentPlayerStats.getTotalAmmunition();

            return currentAmmu >= lastAmmu;
        case Weapon:
            var lastWeapon = lastPlayerStats.getWeapon();
            var currentWeapon = currentPlayerStats.getWeapon();

            return lastWeapon != currentWeapon;
        default:
            return false;
        }
    }

    public static Map<String, Double> concludeAimRatio(CBRShooterFile file) {
        var ratios = new HashMap<String, Double>();
        for (var name : file.getPlayerInformation().keySet())
            ratios.put(name, 1.0);

        if (file.getPlayerCount() != 2)
            return ratios;

        var shotsFired = concludeShotsFired(file);
        var hitsTaken = concludeHitsTaken(file);

        for (var name : ratios.keySet()) {
            var otherHitsTaken = hitsTaken.get(otherPlayerName(name, file));
            ratios.put(name, ((double) otherHitsTaken) / shotsFired.get(name));
        }

        return ratios;
    }

    public static Map<String, Integer> concludeShotsFired(CBRShooterFile file) {
        var shots = new HashMap<String, Integer>();
        for (var name : file.getPlayerInformation().keySet())
            shots.put(name, 0);

        var lastAmmunitions = new HashMap<String, Integer>();
        for (var statistics : file.getStatistics()) {
            for (var player : statistics.getPlayers()) {
                var name = player.getName();

                var lastAmmu = lastAmmunitions.getOrDefault(name, player.getTotalAmmunition());
                var currentAmmu = player.getTotalAmmunition();

                // Only add if less ammu than before. Else round ended or ammunition was
                // collected.
                if (currentAmmu < lastAmmu)
                    shots.put(name, shots.get(name) + lastAmmu - currentAmmu);

                lastAmmunitions.put(name, currentAmmu);
            }
        }

        return shots;
    }

    public static Map<String, Integer> concludeCollected(CBRShooterFile file, Collectable collectable) {
        var collected = new HashMap<String, Integer>();
        for (var name : file.getPlayerInformation().keySet())
            collected.put(name, 0);

        CBRShooterStatistics lastStatistics = null;
        for (var statistics : file.getStatistics()) {
            for (var player : statistics.getPlayers()) {
                var name = player.getName();
                var hasCollected = playerCollected(name, collectable, lastStatistics, statistics);

                if (hasCollected)
                    collected.put(name, collected.get(name) + 1);
            }
            lastStatistics = statistics;
        }

        return collected;
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

                    var weaponInfo = StreamUtil.firstOrNull(file.getWeaponInformation(),
                            x -> x.getName().equals(otherPlayerWeapon));
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

                var lastPos = lastPositions.getOrDefault(name, player.getPosition());
                var currentPos = player.getPosition();

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
        var file = (CBRShooterFile) Workspace.getInstance().getDatabaseManager()
                .loadFile("bd632b71-f2bf-43e4-ab1d-11c231a4a860.visab2", "CBRShooter");
        var test = collectedCollectablesPerRound("John Doe", file, Collectable.Ammunition);
        for (int i = 0; i < test.size(); i++) {
            System.out.println(test.get(i).getRound() + " : " + test.get(i).getParameter());
        }

        // var shots = concludeShotsFired(file);
        // System.out.println(shots);
    }

}
