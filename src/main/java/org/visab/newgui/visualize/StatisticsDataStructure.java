package org.visab.newgui.visualize;

public class StatisticsDataStructure {
    
    private int round;
    private double parameter;
    
    public StatisticsDataStructure(Integer round, Double param) {
        this.round = round;
        this.parameter = param;
    }
    
    public int getRound() {
        return round;
    }
    
    public double getParameter() {
        return parameter;
    }

}
