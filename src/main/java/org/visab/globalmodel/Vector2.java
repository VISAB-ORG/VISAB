package org.visab.globalmodel;

/**
 * A two dimensional vector representation.
 */
public class Vector2 {

    private int x;
    private int y;

    public Vector2() {
        super();
    }

    public Vector2(int x, int y) {
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

    public boolean isZero() {
        return (this.x == 0 && this.y == 0);
    }

    public String toString() {
        return "(X: " + this.x + ", Y: " + this.y + ")";
    }

}
