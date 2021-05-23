package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.globalmodel.IImage;

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
