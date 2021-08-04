package org.visab.newgui.visualize.cbrshooter.model;

import java.util.Vector;

import org.visab.globalmodel.DoubleVector2;
import org.visab.globalmodel.IntVector2;
import org.visab.globalmodel.Rectangle;

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
    private DoubleVector2 drawPanePositioning;

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
            DoubleVector2 drawPanePositioning) {
        this.mapRectangle = mapRectangle;
        this.drawPaneHeight = drawPaneHeight;
        this.drawPaneWidth = drawPaneWidth;
        this.drawPanePositioning = drawPanePositioning;

        Vector<Double> vector = new Vector<>();
    }

    /**
     * @param coordinatesUnity the coordinates provided by unity.
     * @return the vector that can be used for positioning in JavaFX
     */
    public DoubleVector2 translateAccordingToMap(IntVector2 coordinatesUnity) {

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

        return new DoubleVector2(relativePanePositionX, relativePanePositionY);
    }

}
