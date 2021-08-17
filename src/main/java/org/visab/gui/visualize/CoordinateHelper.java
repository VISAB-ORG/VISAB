package org.visab.gui.visualize;

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
    private Vector2<Double> standardIconVector;
    private Vector2<Double> offsetVector;
    private Vector2<Double> drawPaneCenter;

    /**
     * Constructs a CoordinateHelper with specific boundary information.
     * 
     * @param mapRectangle       the rectangle model of the unity game.
     * @param drawPaneHeight     the height of the draw pane elements shall be
     *                           positioned on.
     * @param drawPaneWidth      the width of the draw pane elements shall be
     *                           positioned on.
     * @param standardIconVector the vector of the used icon size, that is necessary
     *                           for centering the visuals in the view.
     * @param offsetVector       the vector used to provide some offset for the
     *                           calculation if a sent map image has empty space on
     *                           X or Y axis.
     */
    public CoordinateHelper(Rectangle mapRectangle, double drawPaneHeight, double drawPaneWidth,
            Vector2<Double> standardIconVector, Vector2<Double> offsetVector) {
        this.mapRectangle = mapRectangle;
        this.drawPaneHeight = drawPaneHeight;
        this.drawPaneWidth = drawPaneWidth;
        this.standardIconVector = standardIconVector;
        this.offsetVector = offsetVector;
        this.drawPaneCenter = new Vector2<Double>(this.drawPaneWidth / 2, this.drawPaneHeight / 2);
    }

    /**
     * @param coordinatesUnity the coordinates provided by unity.
     * @return the vector that can be used for positioning in JavaFX
     */
    public Vector2<Double> translateAccordingToMap(Vector2<Double> coordinatesUnity, boolean isIcon) {

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

        // If an offset vector is given, adjust the positioning relatively to overcome
        // map image issues
        if (!this.offsetVector.checkIfZero()) {
            if (relativePanePositionX > this.drawPaneCenter.getX()) {
                relativePanePositionX -= (this.offsetVector.getX()
                        * (Math.abs(relativePanePositionX - this.drawPaneCenter.getX()) / this.drawPaneWidth));
            } else {
                relativePanePositionX += (this.offsetVector.getX()
                        * (Math.abs(relativePanePositionX - this.drawPaneCenter.getX()) / this.drawPaneWidth));
            }

            if (relativePanePositionY > this.drawPaneCenter.getY()) {
                relativePanePositionY -= (this.offsetVector.getY()
                        * ((Math.abs(relativePanePositionY - this.drawPaneCenter.getY()) / this.drawPaneHeight)));
            } else {
                relativePanePositionY += (this.offsetVector.getY()
                        * ((Math.abs(relativePanePositionY - this.drawPaneCenter.getY()) / this.drawPaneHeight)));
            }
        }

        return new Vector2<Double>(relativePanePositionX, relativePanePositionY);
    }

}
