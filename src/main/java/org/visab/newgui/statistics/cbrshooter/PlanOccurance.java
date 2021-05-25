package org.visab.newgui.statistics.cbrshooter;

import java.util.HashMap;
import java.util.Map;

public class PlanOccurance {

    private Map<String, Integer> CBR = new HashMap<>();

    private Map<String, Integer> Script = new HashMap<>();

    public PlanOccurance() {
    }

    public Map<String, Integer> getCBR() {
        return CBR;
    }

    public Map<String, Integer> getScript() {
        return Script;
    }
}
