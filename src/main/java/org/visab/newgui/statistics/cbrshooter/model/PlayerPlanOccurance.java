package org.visab.newgui.statistics.cbrshooter.model;

import java.util.HashMap;
import java.util.Map;

public class PlayerPlanOccurance {

    private String playerName;

    private boolean isCBR;

    // Plan, total occurance
    private Map<String, Integer> occurance = new HashMap<>();

    public Map<String, Integer> getOccurance() {
        return occurance;
    }

    public PlayerPlanOccurance(String playerName, boolean isCBR) {
        this.playerName = playerName;
    }

    public boolean isCbr() {
        return isCBR;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getOccurance(String plan) {
        if (!occurance.containsKey(plan))
            return 0;
        else
            return occurance.get(plan).intValue();
    }

    // TODO: This could posssible take the current round timer into consideration
    // also.
    public void incrementOccurance(String plan) {
        if (!occurance.containsKey(plan))
            occurance.put(plan, 1);
        else
            occurance.put(plan, occurance.get(plan) + 1);
    }
}
