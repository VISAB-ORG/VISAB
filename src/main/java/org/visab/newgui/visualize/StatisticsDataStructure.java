package org.visab.newgui.visualize;

public class StatisticsDataStructure {
    
    private double round;
    private double parameter;
    
    public StatisticsDataStructure(Double round, Double param) {
        this.round = round;
        this.parameter = param;
    }
    
    public double getRound() {
        return round;
    }
    
    public double getParameter() {
        return parameter;
    }

}
