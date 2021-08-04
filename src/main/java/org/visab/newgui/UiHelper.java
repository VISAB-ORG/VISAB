package org.visab.newgui;

import javafx.application.Platform;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

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

    public static ImageView greyScaleImage(Image inputImage) {
        ColorAdjust monochrome = new ColorAdjust();
        monochrome.setSaturation(-1);
        ImageView grayScaledImage = new ImageView(inputImage);
        grayScaledImage.setEffect(monochrome);
        return grayScaledImage;
    }

    public static Color translateHexToRgbColor(String hexCode) {
        return Color.valueOf(hexCode);
    }

}
