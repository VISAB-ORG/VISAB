package org.visab.newgui;

import org.visab.globalmodel.Vector2;

import javafx.application.Platform;
import javafx.scene.image.ImageView;

/**
 * Class containing Helper methods for modifying the Ui.
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
     * @param x         the x coordinate for the JavaFX positioning.
     * @param y         the y coordinate for the JavaFX positioning.
     */
    public static void adjustVisual(ImageView imageView, double x, double y) {
        imageView.setX(x);
        imageView.setY(y);
    }

    /**
     * Simple method that sets relevant information for a given image view.
     * 
     * @param imageView the image view that shall be adjusted.
     * @param position  the positioning vector for the given image view.
     */
    public static void adjustVisual(ImageView imageView, Vector2 position) {
        imageView.setX(position.getX());
        imageView.setY(position.getY());
    }

    /**
     * Simple method that sets relevant information for a given image view.
     * 
     * @param imageView the image view that shall be adjusted.
     * @param position  the positioning vector for the given image view.
     * @param fitSizes  the fit size vector for the given image view.
     */
    public static void adjustVisual(ImageView imageView, Vector2 position, Vector2 fitSizes) {
        imageView.setX(position.getX());
        imageView.setY(position.getY());
        imageView.setFitWidth(fitSizes.getX());
        imageView.setFitHeight(fitSizes.getY());
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

    /**
     * Simple method that sets relevant information for a given image view.
     * 
     * @param imageView the image view that shall be adjusted.
     * @param visible   the visibility for the given image view.
     * @param x         the x coordinate for the JavaFX positioning.
     * @param y         the y coordinate for the JavaFX positioning.
     * @param fitWidth  the fit width of the given image view.
     * @param fitHeight the fit height of the given image view.
     */
    public static void adjustVisual(ImageView imageView, boolean visible, double x, double y, double fitWidth,
            double fitHeight) {
        imageView.setVisible(visible);
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
    }

    /**
     * Simple method that sets relevant information for a given image view.
     * 
     * @param imageView the image view that shall be adjusted.
     * @param visible   the visibility for the given image view.
     * @param position  the positioning vector for the given image view.
     * @param fitSizes  the fit size vector for the given image view.
     */
    public static void adjustVisual(ImageView imageView, boolean visible, Vector2 position, Vector2 fitSizes) {
        imageView.setVisible(visible);
        imageView.setX(position.getX());
        imageView.setY(position.getY());
        imageView.setFitWidth(fitSizes.getX());
        imageView.setFitHeight(fitSizes.getY());
    }

}
