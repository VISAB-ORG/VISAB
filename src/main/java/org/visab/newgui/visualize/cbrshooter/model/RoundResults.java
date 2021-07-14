package org.visab.newgui.visualize.cbrshooter.model;

import java.util.HashMap;
import java.util.Map;

public class RoundResults {

    private Map<Integer, Number> results = new HashMap<>();

    public void addResult(int round, Number value) {
        results.put(round, value);
    }

    public Number getResult(int round) {
        return results.get(round);
    }

    public int getRoundCount() {
        return results.keySet().size();
    }

    public Map<Integer, Number> getResults() {
        return results;
    }

}
