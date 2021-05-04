package org.newgui.model;

import java.time.LocalTime;
import java.util.UUID;

public class SessionTableRow {

    private UUID sessionId;
    private String game;
    private LocalTime sessionStart;
    private LocalTime lastReceived;
    private boolean isActive = true;

    public SessionTableRow(UUID sessionId, String game, LocalTime sessionStart, LocalTime lastReceived) {
        this.sessionId = sessionId;
        this.game = game;
        this.sessionStart = sessionStart;
        this.lastReceived = lastReceived;
    }

    public UUID getSessionId() {
        return this.sessionId;
    }

    public String getGame() {
        return this.game;
    }

    public LocalTime getSessionStart() {
        return this.sessionStart;
    }

    public LocalTime getLastReceived() {
        return this.lastReceived;
    }

    public void setLastReceived(LocalTime lastReceived) {
        this.lastReceived = lastReceived;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

}
