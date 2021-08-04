package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.eventbus.IEvent;

/**
 * The VISABFileSavedEvent that occurs when a VISAB file is saved by the
 * DatabaseManager.
 */
public class VISABFileSavedEvent implements IEvent {

    private boolean isSavedByListener;
    private UUID sessionId;
    private String fileName;
    private String game;

    public VISABFileSavedEvent(String fileName, String game, UUID sessionId) {
        this.fileName = fileName;
        this.game = game;
        this.sessionId = sessionId;
        this.isSavedByListener = true;
    }

    public String getGame() {
        return game;
    }

    public String getFileName() {
        return fileName;
    }

    public VISABFileSavedEvent(String fileName, String game) {
        this.fileName = fileName;
        this.game = game;
        this.isSavedByListener = false;
    }

    public boolean isSavedByListener() {
        return isSavedByListener;
    }

    public UUID getSessionId() {
        return this.sessionId;
    }

}
