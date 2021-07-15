package org.visab.newgui.visualize.settlers.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class PlayerPlanOccurance {

    private String playerName;

    // Plan, total time of occurance
    private Map<String, IntegerProperty> planOccurances = new HashMap<>();

    public Map<String, IntegerProperty> getPlanTimes() {
        return planOccurances;
    }

    public PlayerPlanOccurance(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getTotalOccurances(String plan) {
        if (!planOccurances.containsKey(plan))
            return 0;
        else
            return planOccurances.get(plan).intValue();
    }

    public IntegerProperty getOccuranceProperty(String plan) {
        if (planOccurances.containsKey(plan))
            return planOccurances.get(plan);

        return null;
    }

    public void incrementOccurance(List<String> plans) {
        for (String plan : plans) {
            if (!planOccurances.containsKey(plan))
                planOccurances.put(plan, new SimpleIntegerProperty(0));
            else
                planOccurances.get(plan).set(planOccurances.get(plan).intValue() + 1);
        }
    }
}
