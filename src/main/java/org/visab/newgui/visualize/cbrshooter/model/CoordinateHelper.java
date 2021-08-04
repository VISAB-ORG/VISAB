package org.visab.newgui.visualize.cbrshooter.model;

import org.visab.globalmodel.Vector2;
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
    private Vector2 drawPanePositioning;

    /**
     * Constructs a CoordinateHelper with specific boundary information.
     * 
     * @param mapRectangle        the rectangle model of the unity game.
     * @param drawPaneHeight      the height of the draw pane elements shall be
     *                            positioned on.
     * @param drawPaneWidth       the width of the draw pane elements shall be
     *                            positioned on.
     * @param drawPanePositioning the position (top-left-anchor) of the draw pane.
     */
    public CoordinateHelper(Rectangle mapRectangle, double drawPaneHeight, double drawPaneWidth,
            Vector2 drawPanePositioning) {
        System.out.println("Initializing coordinate helper with values: ");
        System.out.println("Map rectangle: " + mapRectangle.getWidth() + " * " + mapRectangle.getHeight());
        System.out.println("Draw pane height " + drawPaneHeight);
        System.out.println("Draw pane width " + drawPaneWidth);

        this.mapRectangle = mapRectangle;
        this.drawPaneHeight = drawPaneHeight;
        this.drawPaneWidth = drawPaneWidth;
        this.drawPanePositioning = drawPanePositioning;
    }

    /**
     * @param coordinatesUnity the coordinates provided by unity.
     * @return the vector that can be used for positioning in JavaFX
     */
    public Vector2 translateAccordingToMap(Vector2 coordinatesUnity) {

        // Compute positioning relative to the top left anchor point with distances
        double relativeXDistanceToTopLeftAnchorPoint = Math
                .abs(this.mapRectangle.getTopLeftAnchorPoint().getX() - coordinatesUnity.getX());
        double relativeYDistanceToTopLeftAnchorPoint = Math
                .abs(this.mapRectangle.getTopLeftAnchorPoint().getY() - coordinatesUnity.getY());

        // Calculate the percentage distance on the unity map
        double percentageMovedOnX = relativeXDistanceToTopLeftAnchorPoint / this.mapRectangle.getWidth();

        double percentageMovedOnY = relativeYDistanceToTopLeftAnchorPoint / this.mapRectangle.getHeight();

        // Calculate the positioning on the JavaFX pane that should be drawn on
        double relativePanePositionX = (int) (this.drawPanePositioning.getX()
                + (percentageMovedOnX * this.drawPaneWidth));
        double relativePanePositionY = (int) (this.drawPanePositioning.getY()
                + (percentageMovedOnY * this.drawPaneHeight));

        return new Vector2((int) relativePanePositionX, (int) relativePanePositionY);
    }

}
