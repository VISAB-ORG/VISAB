package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.globalmodel.IImage;
import org.visab.newgui.webapi.model.TransmissionSessionStatus;

/**
 * The ImageReceivedEvent, that occurs when images are received by the VISAB
 * api.
 */
public class ImageReceivedEvent extends ApiEventBase {

    private IImage mapImage;
    private String game;

    public ImageReceivedEvent(UUID sessionId, TransmissionSessionStatus status, String game, IImage mapImage) {
        super(sessionId, status);
        this.game = game;
        this.mapImage = mapImage;
    }

    public IImage getImage() {
        return this.mapImage;
    }

    public String getGame() {
        return this.game;
    }

}
