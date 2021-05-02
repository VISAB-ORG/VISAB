package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.processing.IUnityMapImage;

public class MapImageReceivedEvent extends EventBase {

    private IUnityMapImage mapImage;
    private String game;

    public MapImageReceivedEvent(UUID sessionId, String game, IUnityMapImage mapImage) {
        super(sessionId);
        this.game = game;
        this.mapImage = mapImage;
    }

    public IUnityMapImage getMapImage() {
        return this.mapImage;
    }

    public String getGame() {
        return this.game;
    }

}
