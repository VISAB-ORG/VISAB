package org.visab.globalmodel;

/**
 * A rectangle is defined by its width, height and its top left corner anchor
 * point.
 */
public class Rectangle {

    private Vector2<Double> topLeftAnchorPoint;
    private int width;
    private int height;

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Vector2<Double> getTopLeftAnchorPoint() {
        return topLeftAnchorPoint;
    }

    public void setTopLeftAnchorPoint(Vector2<Double> topLeftAnchorPoint) {
        this.topLeftAnchorPoint = topLeftAnchorPoint;
    }

}
