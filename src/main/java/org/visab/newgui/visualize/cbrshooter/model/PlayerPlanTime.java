package org.visab.newgui.visualize.cbrshooter.model;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class PlayerPlanTime {

    private String playerName;

    private boolean isCBR;

    // Plan, total time of occurance
    private Map<String, DoubleProperty> planTimes = new HashMap<>();

    public Map<String, DoubleProperty> getPlanTimes() {
        return planTimes;
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
        if (!planTimes.containsKey(plan))
            return 0.0;
        else
            return planTimes.get(plan).doubleValue();
    }

    public DoubleProperty getTimeProperty(String plan) {
        if (planTimes.containsKey(plan))
            return planTimes.get(plan);

        return null;
    }

    private float lastRoundTime = 0;
    private int lastRound = 0;

    public void incrementOccurance(String plan, int round, float roundTime) {
        if (!planTimes.containsKey(plan))
            planTimes.put(plan, new SimpleDoubleProperty(0.0));

        // If round changed, set last round time to 0
        if (lastRound != round)
            lastRoundTime = 0;

        var timePassed = roundTime - lastRoundTime;
        planTimes.get(plan).set(planTimes.get(plan).doubleValue() + timePassed);

        lastRound = round;
        lastRoundTime = roundTime;
    }
}
