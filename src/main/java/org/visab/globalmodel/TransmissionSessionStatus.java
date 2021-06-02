package org.visab.globalmodel;

import java.time.LocalTime;
import java.util.UUID;

public class TransmissionSessionStatus {

    private UUID sessionId;
    private boolean isActive;
    private LocalTime lastRequest;
    private String game;
    private String hostName;
    private String ip;

    public TransmissionSessionStatus(UUID sessionId, String game, boolean isActive, LocalTime lastRequest,
            LocalTime sessionOpened, LocalTime sessionClosed, int receivedStatistics, int receivedImages,
            int totalRequests, String hostName, String ip) {
        this.game = game;
        this.sessionId = sessionId;
        this.isActive = isActive;
        this.lastRequest = lastRequest;
        this.sessionOpened = sessionOpened;
        this.sessionClosed = sessionClosed;
        this.receivedStatistics = receivedStatistics;
        this.receivedImages = receivedImages;
        this.totalRequests = totalRequests;
        this.hostName = hostName;
        this.ip = ip;
    }

    public String getHostName() {
        return this.hostName;
    }

    public String getIp() {
        return this.ip;
    }

    private LocalTime sessionOpened;
    private LocalTime sessionClosed;
    private int receivedStatistics;
    private int receivedImages;
    private int totalRequests;

    public UUID getSessionId() {
        return this.sessionId;
    }

    public String getGame() {
        return this.game;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public LocalTime getLastRequest() {
        return this.lastRequest;
    }

    public void setLastRequest(LocalTime lastRequest) {
        this.lastRequest = lastRequest;
    }

    public LocalTime getSessionOpened() {
        return this.sessionOpened;
    }

    public LocalTime getSessionClosed() {
        return this.sessionClosed;
    }

    public void setSessionClosed(LocalTime sessionClosed) {
        this.sessionClosed = sessionClosed;
    }

    public int getReceivedStatistics() {
        return this.receivedStatistics;
    }

    public void setReceivedStatistics(int receivedStatistics) {
        this.receivedStatistics = receivedStatistics;
    }

    public int getReceivedImages() {
        return this.receivedImages;
    }

    public void setReceivedImages(int receivedImages) {
        this.receivedImages = receivedImages;
    }

    public int getTotalRequests() {
        return this.totalRequests;
    }

    public void setTotalRequests(int totalRequests) {
        this.totalRequests = totalRequests;
    }
}