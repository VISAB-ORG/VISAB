package org.newgui.model;

import java.time.LocalTime;
import java.util.UUID;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class SessionTableRow {

    private SimpleStringProperty sessionId;
    private SimpleStringProperty game;
    private SimpleStringProperty sessionStart;
    private SimpleStringProperty lastReceived;
    private SimpleBooleanProperty isActive = new SimpleBooleanProperty(true);

    public SessionTableRow(UUID sessionId, String game, LocalTime sessionStart, LocalTime lastReceived) {
        this.sessionId = new SimpleStringProperty(sessionId.toString());
        this.game = new SimpleStringProperty(game);
        this.sessionStart = new SimpleStringProperty(sessionStart.toString());
        this.lastReceived = new SimpleStringProperty(lastReceived.toString());
    }

    public String getSessionId() {
        return this.sessionId.get();
    }

    public String getGame() {
        return this.game.get();
    }

    public String getSessionStart() {
        return this.sessionStart.get();
    }

    public String getLastReceived() {
        return this.lastReceived.get();
    }

    public void setLastReceived(LocalTime lastReceived) {
        this.lastReceived.set(lastReceived.toString());
    }

    public boolean getIsActive() {
        return this.isActive.get();
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }

}
