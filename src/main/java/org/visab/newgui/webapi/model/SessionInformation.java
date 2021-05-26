package org.visab.newgui.webapi.model;

import java.time.LocalTime;
import java.util.UUID;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SessionInformation {

    private String game;
    private BooleanProperty isActiveProperty = new SimpleBooleanProperty();
    private StringProperty lastReceivedProperty = new SimpleStringProperty();
    private String remoteHostName;
    private String remoteIp;
    private UUID sessionId;
    private LocalTime sessionStart;

    public SessionInformation(UUID sessionId, String game, LocalTime sessionStart, LocalTime lastReceived,
            String remoteIp, String remoteHostName) {
        this.sessionId = sessionId;
        this.game = game;
        this.sessionStart = sessionStart;
        this.remoteIp = remoteIp;
        this.remoteHostName = remoteHostName;
        
        isActiveProperty.set(true);
        lastReceivedProperty.set(lastReceived.toString());
    }

    public String getGame() {
        return this.game;
    }

    public boolean getIsActive() {
        return this.isActiveProperty.get();
    }

    public String getLastReceived() {
        return lastReceivedProperty.get();
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
        this.isActiveProperty.set(isActive);
    }

    public void setLastReceived(LocalTime lastReceived) {
        this.lastReceivedProperty.set(lastReceived.toString());
    }

    public BooleanProperty isActiveProperty() {
        return this.isActiveProperty;
    }

    public StringProperty lastReceivedProperty() {
        return this.lastReceivedProperty;
    }

}
