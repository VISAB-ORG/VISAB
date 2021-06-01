package org.visab.newgui.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ReplayView extends ImageView {

    public ReplayView() {
        super();
    }

    /**
     * @param url Url to the static base image that will be drawn upon.
     */
    public ReplayView(String url) {
        super(new Image(url));
    }

    /**
     * @param image Static base image that will be drawn upon.
     */
    public ReplayView(Image image) {
        super(image);
    }

    private BooleanProperty pausedProperty = new SimpleBooleanProperty(false);

    public BooleanProperty pausedProperty() {
        return pausedProperty;
    }

}
