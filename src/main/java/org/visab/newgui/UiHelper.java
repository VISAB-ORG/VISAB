package org.visab.newgui;

import javafx.application.Platform;

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

}
