package org.visab.newgui;

import javafx.application.Platform;
import javafx.scene.image.ImageView;

/**
 * Class containg Helper methods for modifying the Ui.
 */
public final class UiHelper {

    /**
     * invoke a Runnable on the Ui Thread. Use this when you you want to update the
     * Ui but are not on the Ui Thread.
     * 
     * @param runnable The runnable to invoke
     */
    public static void inovkeOnUiThread(Runnable runnable) {
        Platform.runLater(runnable);
    }

    /**
     * Simple method that sets relevant information for a given image view.
     * 
     * @param imageView the image view that shall be adjusted.
     * @param visible   the visibility for the given image view.
     * @param x         the x coordinate for the JavaFX positioning.
     * @param y         the y coordinate for the JavaFX positioning.
     */
    public static void adjustVisual(ImageView imageView, boolean visible, double x, double y) {
        imageView.setVisible(visible);
        imageView.setX(x);
        imageView.setY(y);
    }

}
