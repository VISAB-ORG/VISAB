package org.visab.newgui.visualize;

public class StatisticsDataStructure {
    
    private double round;
    private int parameter;
    
    public StatisticsDataStructure(Double round, Integer param) {
        this.round = round;
        this.parameter = param;
    }
    
    public double getRound() {
        return round;
    }
    
    public int getParameter() {
        return parameter;
    }

}
