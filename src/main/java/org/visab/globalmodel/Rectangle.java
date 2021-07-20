package org.visab.globalmodel;

public class Rectangle {

    private int width;
    private int height;
    private Vector2 topLeftAnchorPoint;

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

    public Vector2 getTopLeftAnchorPoint() {
        return topLeftAnchorPoint;
    }

    public void setTopLeftAnchorPoint(Vector2 topLeftAnchorPoint) {
        this.topLeftAnchorPoint = topLeftAnchorPoint;
    }

}
