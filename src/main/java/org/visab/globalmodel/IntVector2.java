package org.visab.globalmodel;

public class IntVector2 {

    private int x;
    private int y;

    public IntVector2() {
        super();
    }

    public IntVector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String toString() {
        return "(X: " + this.x + ", Y: " + this.y + ")";
    }

}
