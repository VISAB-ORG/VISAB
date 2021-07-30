package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.eventbus.APIEventBase;
import org.visab.globalmodel.IImageContainer;
import org.visab.globalmodel.SessionStatus;

/**
 * The ImageReceivedEvent, that occurs when images are received by the VISAB
 * api.
 */
public class ImageReceivedEvent extends APIEventBase {

    private IImageContainer mapImage;
    private String game;

    public ImageReceivedEvent(UUID sessionId, SessionStatus status, String game, IImageContainer mapImage) {
        super(sessionId, status);
        this.game = game;
        this.mapImage = mapImage;
    }

    public IImageContainer getImage() {
        return this.mapImage;
    }

    public String getGame() {
        return this.game;
    }

}
