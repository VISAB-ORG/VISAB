package org.visab.newgui.visualize.cbrshooter.model;

import org.visab.globalmodel.Rectangle;
import org.visab.globalmodel.Vector2;

/**
 * This class serves as a util to align provided coordinates from unity with a
 * JavaFX pane to draw on.
 * 
 * @author leonr
 *
 */
public class CoordinateHelper {

    private Rectangle mapRectangle;
    private double drawPaneHeight;
    private double drawPaneWidth;
    private Vector2 standardIconVector;

    /**
     * Constructs a CoordinateHelper with specific boundary information.
     * 
     * @param mapRectangle   the rectangle model of the unity game.
     * @param drawPaneHeight the height of the draw pane elements shall be
     *                       positioned on.
     * @param drawPaneWidth  the width of the draw pane elements shall be positioned
     *                       on.
     */
    public CoordinateHelper(Rectangle mapRectangle, double drawPaneHeight, double drawPaneWidth,
            Vector2 standardIconVector) {
        this.mapRectangle = mapRectangle;
        this.drawPaneHeight = drawPaneHeight;
        this.drawPaneWidth = drawPaneWidth;
        this.standardIconVector = standardIconVector;
    }

    /**
     * @param coordinatesUnity the coordinates provided by unity.
     * @return the vector that can be used for positioning in JavaFX
     */
    public Vector2 translateAccordingToMap(Vector2 coordinatesUnity, boolean isIcon) {

        // Compute positioning relative to the top left anchor point with distances
        double relativeXDistanceToTopLeftAnchorPoint = Math
                .abs(this.mapRectangle.getTopLeftAnchorPoint().getX() - coordinatesUnity.getX());
        double relativeYDistanceToTopLeftAnchorPoint = Math
                .abs(this.mapRectangle.getTopLeftAnchorPoint().getY() - coordinatesUnity.getY());

        // Calculate the percentage distance on the unity map
        double percentageMovedOnX = relativeXDistanceToTopLeftAnchorPoint / this.mapRectangle.getWidth();
        double percentageMovedOnY = relativeYDistanceToTopLeftAnchorPoint / this.mapRectangle.getHeight();

        double centerOnXOffset = 0.0;
        double centerOnYOffset = 0.0;
        if (isIcon) {
            centerOnXOffset = (-1) * (this.standardIconVector.getX() / 2);
            centerOnYOffset = (-1) * (this.standardIconVector.getY() / 2);
        }

        // Calculate the positioning on the JavaFX pane that should be drawn on
        double relativePanePositionX = percentageMovedOnX * this.drawPaneWidth + centerOnXOffset;
        double relativePanePositionY = percentageMovedOnY * this.drawPaneHeight + centerOnYOffset;

        return new Vector2((int) relativePanePositionX, (int) relativePanePositionY);
    }

}
