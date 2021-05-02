package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.processing.IUnityMapInformation;

public class MapInformationReceivedEvent extends EventBase {
    
    private String game;
    private IUnityMapInformation mapInformation;

    public MapInformationReceivedEvent(UUID sessionId, String game, IUnityMapInformation mapInformation) {
        super(sessionId);
        this.game = game;
        this.mapInformation = mapInformation;
    }

    public String getGame() {
        return game;
    }

    public IUnityMapInformation getMapInformation() {
        return mapInformation;
    }
}
