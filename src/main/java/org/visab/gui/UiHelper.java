package org.visab.gui;

import org.visab.globalmodel.Vector2;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Class containing Helper methods for modifying the Ui.
 */
public final class UiHelper {

    /**
     * Runs a Runnable on the Ui thread. Use this when you you want to update the Ui
     * but are not on the Ui tread.
     * 
     * @param runnable The runnable to run
     */
    public static void inovkeOnUiThread(Runnable runnable) {
        Platform.runLater(runnable);
    }

    /**
     * This method recolors any input image pixel by pixel with a given JavaFX
     * color.
     * 
     * @param inputImage the image that needs to be recolored.
     * @param newColor   the new color for the given image.
     * @return the recolored image.
     */
    public static Image recolorImage(Image inputImage, Color newColor) {
        final double r = newColor.getRed();
        final double g = newColor.getGreen();
        final double b = newColor.getBlue();
        final int w = (int) inputImage.getWidth();
        final int h = (int) inputImage.getHeight();
        final WritableImage outputImage = new WritableImage(w, h);
        final PixelWriter writer = outputImage.getPixelWriter();
        final PixelReader reader = inputImage.getPixelReader();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                // Keeping the opacity of every pixel as it is.
                writer.setColor(x, y, new Color(r, g, b, reader.getColor(x, y).getOpacity()));
            }
        }
        return outputImage;
    }

    /**
     * This method grey scales an image so that other colored elements can be better
     * seen on it.
     * 
     * @param inputImage the image that needs to be grey-scaled.
     * @param contrast   the contrast for the image.
     * @return the grey-scaled image.
     */
    public static ImageView greyScaleImage(Image inputImage, double contrast) {
        ColorAdjust monochrome = new ColorAdjust();
        monochrome.setSaturation(-1);
        monochrome.setContrast(contrast);
        ImageView grayScaledImage = new ImageView(inputImage);
        grayScaledImage.setEffect(monochrome);
        return grayScaledImage;
    }

    /**
     * This method translates a hex color code to a JavaFX color.
     * 
     * @param hexCode the hex color code.
     * @return the equivalent JavaFX color.
     */
    public static Color translateHexToRgbColor(String hexCode) {
        return Color.valueOf(hexCode);
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
     * @param label the label that shall be adjusted.
     * @param x     the x coordinate for the JavaFX positioning.
     * @param y     the y coordinate for the JavaFX positioning.
     */
    public static void adjustVisual(Label label, double x, double y) {
        label.setLayoutX(x);
        label.setLayoutY(y);
    }

    /**
     * Simple method that sets relevant information for a given image view.
     * 
     * @param imageView the image view that shall be adjusted.
     * @param position  the positioning vector for the given image view.
     */
    public static void adjustVisual(ImageView imageView, Vector2<Double> position) {
        imageView.setX(position.getX());
        imageView.setY(position.getY());
    }

    /**
     * Simple method that sets relevant information for a given image view.
     * 
     * @param imageView the image view that shall be adjusted.
     * @param visible   the visibility for the given image view.
     * @param position  the positioning vector for the given image view.
     */
    public static void adjustVisual(ImageView imageView, boolean visible, Vector2<Double> position) {
        imageView.setX(position.getX());
        imageView.setY(position.getY());
        imageView.setVisible(visible);
    }

    /**
     * Simple method that sets relevant information for a given image view.
     * 
     * @param imageView the image view that shall be adjusted.
     * @param position  the positioning vector for the given image view.
     * @param fitSizes  the fit size vector for the given image view.
     */
    public static void adjustVisual(ImageView imageView, Vector2<Double> position, Vector2<Double> fitSizes) {
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
    public static void adjustVisual(ImageView imageView, boolean visible, Vector2<Double> position,
            Vector2<Double> fitSizes) {
        imageView.setVisible(visible);
        imageView.setX(position.getX());
        imageView.setY(position.getY());
        imageView.setFitWidth(fitSizes.getX());
        imageView.setFitHeight(fitSizes.getY());
    }

    /**
     * Reesizes an ImageView to the size of a given vector.
     * 
     * @param resizeImage The ImageView to resize
     * @param sizeVector  The vector whose X and Y coordinates determine the new
     *                    width and height respectively
     * @return The resized ImageView
     */
    public static ImageView resizeImage(ImageView resizeImage, Vector2<Double> sizeVector) {
        resizeImage.setFitWidth(sizeVector.getX());
        resizeImage.setFitHeight(sizeVector.getY());
        return resizeImage;
    }

}
