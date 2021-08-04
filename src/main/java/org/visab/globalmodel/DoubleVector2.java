package org.visab.globalmodel;

public class DoubleVector2 {

    private double x;
    private double y;

    public DoubleVector2() {
        super();
    }

    public DoubleVector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String toString() {
        return "(X: " + this.x + ", Y: " + this.y + ")";
    }

}
