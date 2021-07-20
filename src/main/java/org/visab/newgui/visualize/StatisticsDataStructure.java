package org.visab.newgui.visualize;

public class StatisticsDataStructure {
    
    private int round;
    private double value;
    
    public StatisticsDataStructure(Integer round, Double value) {
        this.round = round;
        this.value = value;
    }
    
    public int getRound() {
        return round;
    }
    
    public double getValue() {
        return value;
    }

}
