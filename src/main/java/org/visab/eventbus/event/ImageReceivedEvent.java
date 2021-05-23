package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.globalmodel.IImage;

/**
 * The ImageReceivedEvent, that occurs when images are received by the
 * VISAB api.
 */
public class ImageReceivedEvent extends EventBase {

    private IImage mapImage;
    private String game;

    public ImageReceivedEvent(UUID sessionId, String game, IImage mapImage) {
        super(sessionId);
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
