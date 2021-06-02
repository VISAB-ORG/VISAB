package org.visab.newgui.webapi.model;

import java.time.LocalTime;
import java.util.UUID;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class TransmissionSessionStatus {

    private UUID sessionId;
    private String game;
    private LocalTime sessionOpened;

    private BooleanProperty isActiveProperty = new SimpleBooleanProperty();
    private ObjectProperty<LocalTime> lastRequestProperty = new SimpleObjectProperty<>();
    private IntegerProperty totalRequestsProperty = new SimpleIntegerProperty();

    private ObjectProperty<LocalTime> sessionClosedProperty = new SimpleObjectProperty<>();
    private IntegerProperty receivedStatisticsProperty = new SimpleIntegerProperty();
    private IntegerProperty receivedImagesProperty = new SimpleIntegerProperty();

    private String hostName;
    private String ip;

    public TransmissionSessionStatus(UUID sessionId, String game, boolean isActive, LocalTime lastRequest,
            LocalTime sessionOpened, LocalTime sessionClosed, int receivedStatistics, int receivedImages,
            int totalRequests, String hostName, String ip) {
        this.game = game;
        this.sessionId = sessionId;
        this.sessionOpened = sessionOpened;
        this.hostName = hostName;
        this.ip = ip;

        this.isActiveProperty.set(isActive);
        this.lastRequestProperty.set(lastRequest);
        this.sessionClosedProperty.set(sessionClosed);
        this.receivedStatisticsProperty.set(receivedStatistics);
        this.receivedImagesProperty.set(receivedImages);
        this.totalRequestsProperty.set(totalRequests);
    }

    public String getHostName() {
        return this.hostName;
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
