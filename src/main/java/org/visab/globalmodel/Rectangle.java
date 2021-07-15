package org.visab.globalmodel;

public class Rectangle {

    private Vector2 topLeftAnchorPoint;
    private int width;
    private int height;

    public int getWidth() {
        return this.width;
    }

    public Vector2 getTopLeftAnchorPoint() {
        return topLeftAnchorPoint;
    }

    public void setTopLeftAnchorPoint(Vector2 topLeftAnchorPoint) {
        this.topLeftAnchorPoint = topLeftAnchorPoint;
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

}
