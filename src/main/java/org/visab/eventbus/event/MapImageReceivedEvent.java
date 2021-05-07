package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.processing.IMapImage;

public class MapImageReceivedEvent extends EventBase {

    private IMapImage mapImage;
    private String game;

    public MapImageReceivedEvent(UUID sessionId, String game, IMapImage mapImage) {
        super(sessionId);
        this.game = game;
        this.mapImage = mapImage;
    }

    public IMapImage getImage() {
        return this.mapImage;
    }

    public String getGame() {
        return this.game;
    }

}
