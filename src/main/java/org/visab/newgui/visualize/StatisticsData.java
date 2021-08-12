package org.visab.newgui.visualize;

/**
 * Helper class for calculating round based statistics.
 */
public class StatisticsData<T> {

    private int round;
    private T value;

    public StatisticsData(Integer round, T value) {
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
