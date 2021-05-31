package org.visab.newgui.statistics.cbrshooter.model;

import java.util.HashMap;
import java.util.Map;

public class PlayerPlanTime {

    private String playerName;

    private boolean isCBR;

    // Plan, total time of occurance
    private Map<String, Double> occurance = new HashMap<>();

    public Map<String, Double> getOccurance() {
        return occurance;
    }

    public PlayerPlanTime(String playerName, boolean isCBR) {
        this.playerName = playerName;
    }

    public boolean isCbr() {
        return isCBR;
    }

    public String getPlayerName() {
        return playerName;
    }

    public double getTotalTime(String plan) {
        if (!occurance.containsKey(plan))
            return 0.0;
        else
            return occurance.get(plan);
    }


    private float lastRoundTime = 0;
    private int lastRound = 0;

    public void incrementOccurance(String plan, int round, float roundTime) {
        if (!occurance.containsKey(plan))
            occurance.put(plan, 0.0);

        // If round changed, set last round time to 0
        if (lastRound != round)
            lastRoundTime = 0;

        var passedTime = roundTime - lastRoundTime;
        occurance.put(plan, occurance.get(plan) + passedTime);
        
        lastRound = round;
        lastRoundTime = roundTime;
    }
}
