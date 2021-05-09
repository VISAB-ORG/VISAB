package org.newgui.webapi.model;

import java.time.LocalTime;
import java.util.UUID;

public class SessionTableRow {

    private String game;
    private boolean isActive = true;
    private LocalTime lastReceived;
    private String remoteHostName;
    private String remoteIp;
    private UUID sessionId;
    private LocalTime sessionStart;

    public SessionTableRow(UUID sessionId, String game, LocalTime sessionStart, LocalTime lastReceived, String remoteIp,
            String remoteHostName) {
        this.sessionId = sessionId;
        this.game = game;
        this.sessionStart = sessionStart;
        this.lastReceived = lastReceived;
        this.remoteIp = remoteIp;
        this.remoteHostName = remoteHostName;
    }

    public String getGame() {
        return this.game;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public LocalTime getLastReceived() {
        return this.lastReceived;
    }

    public String getRemoteHostName() {
        return this.remoteHostName;
    }

    public String getRemoteIp() {
        return this.remoteIp;
    }

    public UUID getSessionId() {
        return this.sessionId;
    }

    public LocalTime getSessionStart() {
        return this.sessionStart;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setLastReceived(LocalTime lastReceived) {
        this.lastReceived = lastReceived;
    }

}
