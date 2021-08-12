package org.visab.newgui.visualize;

public class StatisticsDataStructure<T> {
    
    private int round;
    private T value;
    
    public StatisticsDataStructure(Integer round, T value) {
        this.round = round;
        this.value = value;
    }
    
    public int getRound() {
        return round;
    }
    
    public T getValue() {
        return value;
    }

}
