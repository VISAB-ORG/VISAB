package org.visab.globalmodel;

import java.time.LocalTime;
import java.util.UUID;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SessionStatus {

    private UUID sessionId;
    private String game;
    private LocalTime sessionOpened;

    private String statusType;

    private BooleanProperty isActiveProperty = new SimpleBooleanProperty();
    private ObjectProperty<LocalTime> lastRequestProperty = new SimpleObjectProperty<>();
    private IntegerProperty totalRequestsProperty = new SimpleIntegerProperty();

    private ObjectProperty<LocalTime> sessionClosedProperty = new SimpleObjectProperty<>();
    private IntegerProperty receivedStatisticsProperty = new SimpleIntegerProperty();
    private IntegerProperty receivedImagesProperty = new SimpleIntegerProperty();

    private String ip;

    public SessionStatus(UUID sessionId, String game, boolean isActive, LocalTime lastRequest, LocalTime sessionOpened,
            LocalTime sessionClosed, int receivedStatistics, int receivedImages, int totalRequests, String ip, String statusType) {
        this.game = game;
        this.sessionId = sessionId;
        this.sessionOpened = sessionOpened;
        this.ip = ip;

        this.isActiveProperty.set(isActive);
        this.lastRequestProperty.set(lastRequest);
        this.sessionClosedProperty.set(sessionClosed);
        this.receivedStatisticsProperty.set(receivedStatistics);
        this.receivedImagesProperty.set(receivedImages);
        this.totalRequestsProperty.set(totalRequests);
        this.statusType = statusType;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getIp() {
        return this.ip;
    }

    public UUID getSessionId() {
        return this.sessionId;
    }

    public String getGame() {
        return this.game;
    }

    public boolean getIsActive() {
        return this.isActive();
    }

    public LocalTime getSessionOpened() {
        return this.sessionOpened;
    }

    public BooleanProperty isActiveProperty() {
        return this.isActiveProperty;
    }

    public void setIsActive(boolean isActive) {
        this.isActiveProperty.set(isActive);
    }

    public boolean isActive() {
        return this.isActiveProperty.get();
    }

    public ObjectProperty<LocalTime> lastRequestProperty() {
        return this.lastRequestProperty;
    }

    public LocalTime getLastRequest() {
        return this.lastRequestProperty.get();
    }

    public ObjectProperty<LocalTime> sessionClosedProperty() {
        return this.sessionClosedProperty;
    }

    public LocalTime getSessionClosed() {
        return this.sessionClosedProperty.get();
    }

    public IntegerProperty totalRequestsProperty() {
        return this.totalRequestsProperty;
    }

    public int getTotalRequests() {
        return this.totalRequestsProperty.get();
    }

    public IntegerProperty receivedStatisticsProperty() {
        return this.receivedStatisticsProperty;
    }

    public int getReceivedStatistics() {
        return this.receivedStatisticsProperty.get();
    }

    public IntegerProperty receivedImagesProperty() {
        return this.receivedImagesProperty;
    }

    public int getReceivedImages() {
        return this.receivedImagesProperty.get();
    }

    public void setLastRequest(LocalTime lastRequest) {
        this.lastRequestProperty.set(lastRequest);
    }

    public void setSessionClosed(LocalTime sessionClosed) {
        this.sessionClosedProperty.set(sessionClosed);
    }

    public void setReceivedStatistics(int amount) {
        this.receivedStatisticsProperty.set(amount);
    }

    public void setReceivedImages(int amount) {
        this.receivedImagesProperty.set(amount);
    }

    public void setTotalRequests(int amount) {
        this.totalRequestsProperty.set(amount);
    }

}
