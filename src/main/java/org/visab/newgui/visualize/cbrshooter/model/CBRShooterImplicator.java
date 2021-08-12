package org.visab.newgui.visualize.cbrshooter.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.visab.globalmodel.Vector2;
import org.visab.globalmodel.cbrshooter.CBRShooterFile;
import org.visab.globalmodel.cbrshooter.CBRShooterStatistics;
import org.visab.newgui.visualize.StatisticsDataStructure;
import org.visab.util.StreamUtil;
import org.visab.workspace.Workspace;

public final class CBRShooterImplicator {

    public static ArrayList<StatisticsDataStructure<Double>> shotsPerRound(String player, CBRShooterFile file) {
        var statistics = MakeStatisticsCopy(file);

        var shotsPerRoundPerPlayer = new ArrayList<StatisticsDataStructure<Double>>();
        var countShots = 0;
        var maxAmmunition = 0;
        var currentAmmunition = 0;
        var round = 0;
        var playerNumber = 0;
        if (player.contains("Jane Doe")) {
            playerNumber = 1;
        }

        for (int i = 0; i < statistics.size(); i++) {

            if (round < statistics.get(i).getRound()) {
                shotsPerRoundPerPlayer.add(new StatisticsDataStructure<Double>(round, (double) countShots));
                countShots = 0;
            }
            maxAmmunition = currentAmmunition;
            currentAmmunition = statistics.get(i).getPlayers().get(playerNumber).getTotalAmmunition();

            if (currentAmmunition < maxAmmunition) {
                countShots += (maxAmmunition - currentAmmunition);
            }

            round = statistics.get(i).getRound();
        }

        return shotsPerRoundPerPlayer;
    }

    public static ArrayList<StatisticsDataStructure<Double>> collectedCollectablesPerRound(String player, CBRShooterFile file,
            Collectable collectable) {
        var statistics = MakeStatisticsCopy(file);

        var collectedCollectablesPerRoundPerPlayer = new ArrayList<StatisticsDataStructure<Double>>();
        var countCollectedCollectables = 0;
        var round = 0;
        var playerNumber = 0;
        if (player.contains("Jane Doe")) {
            playerNumber = 1;
        }

        for (int i = 0; i < statistics.size(); i++) {

            if (round < statistics.get(i).getRound()) {
                collectedCollectablesPerRoundPerPlayer
                        .add(new StatisticsDataStructure<Double>(round, (double)countCollectedCollectables));
            }

            switch (collectable) {
            case Health:
                var lastHealth = statistics.get(i).getPlayers().get(playerNumber).getHealth();
                var currentHealth = statistics.get(i).getPlayers().get(playerNumber).getHealth();

                if (currentHealth > lastHealth) {
                    countCollectedCollectables++;
                }
                break;
            case Ammunition:
                var lastAmmu = statistics.get(i).getPlayers().get(playerNumber).getTotalAmmunition();
                var currentAmmu = statistics.get(i).getPlayers().get(playerNumber).getTotalAmmunition();

                if (currentAmmu > lastAmmu) {
                    countCollectedCollectables++;
                }
                break;
            case Weapon:
                var lastWeapon = statistics.get(i).getPlayers().get(playerNumber).getWeapon();
                var currentWeapon = statistics.get(i).getPlayers().get(playerNumber).getWeapon();

                if (!currentWeapon.equals(lastWeapon)) {
                    countCollectedCollectables++;
                }
                break;
            default:
                break;
            }

            round = statistics.get(i).getRound();
        }

        return collectedCollectablesPerRoundPerPlayer;
    }

    public static ArrayList<StatisticsDataStructure<Double>> aimRatioPerRound(String player, CBRShooterFile file) {
        // aim Ratio = hits / shotsFired
        var aimRatioPerRoundPerPlayer = new ArrayList<StatisticsDataStructure<Double>>();
        var shotsFired = shotsPerRound(player, file);
        var hits = hitsOnEnemyPerRound(player, file);

        for (int i = 0; i < shotsFired.size(); i++) {

            if (shotsFired.get(i).getValue() > 0) {
                var aimRatio = (hits.get(i).getValue() / shotsFired.get(i).getValue());
                aimRatioPerRoundPerPlayer.add(new StatisticsDataStructure<Double>(i, 
                        aimRatio));
            } else {
                aimRatioPerRoundPerPlayer.add(new StatisticsDataStructure<Double>(i, 0.0));
            }

        }

        return aimRatioPerRoundPerPlayer;
    }

    public static ArrayList<StatisticsDataStructure<Double>> hitsOnEnemyPerRound(String player, CBRShooterFile file) {
        var statistics = MakeStatisticsCopy(file);

        var hitsTakenPerRoundPerPlayer = new ArrayList<StatisticsDataStructure<Double>>();
        var countHits = 0;
        var lastHealth = 100;
        var currentHealth = 0;
        var round = 0;
        var playerNumber = 1;
        if (player.contains("Jane Doe")) {
            playerNumber = 0;
        }

        for (int i = 0; i < statistics.size(); i++) {

            if (round < statistics.get(i).getRound()) {
                hitsTakenPerRoundPerPlayer.add(new StatisticsDataStructure<Double>(round, (double)countHits));
                countHits = 0;
            }
            lastHealth = currentHealth;
            currentHealth = statistics.get(i).getPlayers().get(playerNumber).getHealth();

            if (currentHealth < lastHealth) {
                countHits += (int) Math
                        .ceil((lastHealth - currentHealth) / file.getWeaponInformation().get(playerNumber).getDamage());
            }

            round = statistics.get(i).getRound();
            statistics.get(i).getPlayers().get(playerNumber).getPosition();
        }

        return hitsTakenPerRoundPerPlayer;
    }

    public static ArrayList<StatisticsDataStructure<Double>> unitsWalkedPerRound(String player, CBRShooterFile file) {
        var statistics = MakeStatisticsCopy(file);

        var accumulatedDeathsPerRoundPerPlayer = new ArrayList<StatisticsDataStructure<Double>>();
        var unitsWalked = 0;
        var currentPos = new Vector2();
        var moved = 0.0;
        var round = 0;
        var playerNumber = 0;
        if (player.contains("Jane Doe")) {
            playerNumber = 1;
        }

        for (int i = 0; i < statistics.size(); i++) {

            if (round < file.getStatistics().get(i).getRound()) {
                accumulatedDeathsPerRoundPerPlayer.add(new StatisticsDataStructure<Double>(round, (double) unitsWalked));
                unitsWalked = 0;
            }

            var lastPos = currentPos;
            currentPos = statistics.get(i).getPlayers().get(playerNumber).getPosition();

            if (lastPos != currentPos) {
                moved = Math.sqrt(Math.pow(lastPos.getX() - currentPos.getX(), 2.0)
                        + Math.pow(lastPos.getY() - currentPos.getY(), 2.0));
            }

            unitsWalked += moved;
            round = statistics.get(i).getRound();
        }

        return accumulatedDeathsPerRoundPerPlayer;
    }

    public static ArrayList<StatisticsDataStructure<Double>> accumulatedDeathsPerRound(String player, CBRShooterFile file) {
        var statistics = MakeStatisticsCopy(file);

        var accumulatedDeathsPerRoundPerPlayer = new ArrayList<StatisticsDataStructure<Double>>();
        var round = 0;
        var playerNumber = 0;
        if (player.contains("Jane Doe")) {
            playerNumber = 1;
        }

        for (int i = 0; i < statistics.size(); i++) {

            if (round < statistics.get(i).getRound()) {
                accumulatedDeathsPerRoundPerPlayer.add(new StatisticsDataStructure<Double>(round,
                        (double)statistics.get(i).getPlayers().get(playerNumber).getStatistics().getDeaths()));
            }

            round = statistics.get(i).getRound();
        }

        return accumulatedDeathsPerRoundPerPlayer;
    }

    public static ArrayList<StatisticsDataStructure<Double>> accumulatedKillsPerRound(String player, CBRShooterFile file) {
        var statistics = MakeStatisticsCopy(file);

        var accumulatedKillsPerRoundPerPlayer = new ArrayList<StatisticsDataStructure<Double>>();
        var round = 0;
        var playerNumber = 1;
        if (player.contains("Jane Doe")) {
            playerNumber = 0;
        }

        for (int i = 0; i < statistics.size(); i++) {

            if (round < statistics.get(i).getRound()) {
                accumulatedKillsPerRoundPerPlayer.add(new StatisticsDataStructure<Double>(round,
                        (double)statistics.get(i).getPlayers().get(playerNumber).getStatistics().getDeaths()));
            }

            round = statistics.get(i).getRound();
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
        for (var name : file.getPlayerNames())
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
        var statistics = MakeStatisticsCopy(file);

        var shots = new HashMap<String, Integer>();
        for (var name : file.getPlayerNames())
            shots.put(name, 0);

        var lastAmmunitions = new HashMap<String, Integer>();
        for (var snapshot : statistics) {
            for (var player : snapshot.getPlayers()) {
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
        var statistics = MakeStatisticsCopy(file);

        var collected = new HashMap<String, Integer>();
        for (var name : file.getPlayerNames())
            collected.put(name, 0);

        CBRShooterStatistics lastStatistics = null;
        for (var snapshot : statistics) {
            for (var player : snapshot.getPlayers()) {
                var name = player.getName();
                var hasCollected = playerCollected(name, collectable, lastStatistics, snapshot);

                if (hasCollected)
                    collected.put(name, collected.get(name) + 1);
            }
            lastStatistics = snapshot;
        }

        return collected;
    }

    public static Map<String, Integer> concludeHitsTaken(CBRShooterFile file) {
        var statistics = MakeStatisticsCopy(file);

        var hitsTaken = new HashMap<String, Integer>();
        for (var name : file.getPlayerNames())
            hitsTaken.put(name, 0);

        if (file.getPlayerCount() != 2)
            return hitsTaken;

        var lastDeaths = new HashMap<String, Integer>();
        var lastHealths = new HashMap<String, Integer>();
        var lastWeapons = new HashMap<String, String>();
        for (var snapshot : statistics) {
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
        var statistics = MakeStatisticsCopy(file);

        var walked = new HashMap<String, Double>();
        for (var name : file.getPlayerNames())
            walked.put(name, 0.0);

        var lastPositions = new HashMap<String, Vector2>();
        for (var snapshot : statistics) {
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

    private static List<CBRShooterStatistics> MakeStatisticsCopy(CBRShooterFile file) {
        return new ArrayList<>(file.getStatistics());
    }

    private static String otherPlayerName(String myPlayer, CBRShooterFile file) {
        if (file.getPlayerCount() > 2)
            return "";

        for (var name : file.getPlayerNames()) {
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
            System.out.println(test.get(i).getRound() + " : " + test.get(i).getValue());
        }

        // var shots = concludeShotsFired(file);
        // System.out.println(shots);
    }

}
