package org.newgui.webapi.model;

import java.time.LocalTime;
import java.util.UUID;

public class SessionTableRow {

    private UUID sessionId;
    private String game;
    private LocalTime sessionStart;
    private LocalTime lastReceived;
    private boolean isActive = true;
    private String remoteIp;
    private String remoteHostName;

    public SessionTableRow(UUID sessionId, String game, LocalTime sessionStart, LocalTime lastReceived, String remoteIp, String remoteHostName) {
        this.sessionId = sessionId;
        this.game = game;
        this.sessionStart = sessionStart;
        this.lastReceived = lastReceived;
        this.remoteIp = remoteIp;
        this.remoteHostName = remoteHostName;
    }

    public String getRemoteIp() {
        return this.remoteIp;
    }

    public String getRemoteHostName() {
        return this.remoteHostName;
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
