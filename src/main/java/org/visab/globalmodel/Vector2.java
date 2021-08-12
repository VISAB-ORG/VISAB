package org.visab.globalmodel;

/**
 * A two dimensional vector representation.
 */
public class Vector2<T> {

    private T x;
    private T y;

    public Vector2() {
        super();
    }

    public Vector2(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    public void setX(T x) {
        this.x = x;
    }

    public void setY(T y) {
        this.y = y;
    }

    public boolean checkIfZero() {
        return ((double) this.x == 0.0 && (double) this.y == 0.0);
    }

    public boolean checkIfNull() {
        return this.x == null;
    }

    public String toString() {
        return "(X: " + this.x + ", Y: " + this.y + ")";
    }

}
