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

    /**
     * 
     * 
     * @param mapRectangle     the rectangle describing the limitations of the map.
     * @param coordinatesUnity the coordinates provided by unity.
     * @param panePositioning  the pane positioning in JavaFX.
     * @param height           the height of the pane in JavaFX.
     * @param width            the width of the pane in JavaFX.
     * @return
     */
    public static Vector2 translateAccordingToMap(Rectangle mapRectangle, Vector2 coordinatesUnity,
            Vector2 panePositioning, int height, int width) {

        // TODO: Logic in here does not function properly and just serves as a
        // placeholder

        // Bullshit, just a placeholder to indicate planned logic
        var unityXDifference = mapRectangle.getTopLeftAnchorPoint().getX() - coordinatesUnity.getX();
        var unityYDifference = mapRectangle.getTopLeftAnchorPoint().getY() - coordinatesUnity.getY();
        var percentageXDifference = 100 / mapRectangle.getWidth() * unityXDifference;
        var percentageYDifference = 100 / mapRectangle.getHeight() * unityYDifference;

        var relativeX = panePositioning.getX() + percentageXDifference * width;
        var relativeY = panePositioning.getY() + percentageYDifference * height;

        return new Vector2(relativeX, relativeY);
    }

}
