package org.visab.globalmodel;

public class Rectangle {

    private IntVector2 topLeftAnchorPoint;
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

    public IntVector2 getTopLeftAnchorPoint() {
        return topLeftAnchorPoint;
    }

    public void setTopLeftAnchorPoint(IntVector2 topLeftAnchorPoint) {
        this.topLeftAnchorPoint = topLeftAnchorPoint;
    }

}
