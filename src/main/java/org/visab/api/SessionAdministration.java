package org.visab.api;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.dynamic.DynamicSerializer;
import org.visab.eventbus.IApiEvent;
import org.visab.eventbus.event.ImageReceivedEvent;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.eventbus.publisher.ApiPublisherBase;
import org.visab.globalmodel.IMetaInformation;
import org.visab.globalmodel.SessionStatus;
import org.visab.util.StreamUtil;

/**
 * Class for administering the current transmission sessions. Holds a reference
 * to the current transmission sessions and checks them for timeout. Publishes
 * all api related events.
 *
 * @author moritz
 *
 */
public class SessionAdministration {

    // Logger needs .class for each class to use for log traces
    private Logger logger = LogManager.getLogger(SessionAdministration.class);

    /**
     * An overview of transmission session specific data.
     */
    private List<SessionStatus> statuses = new ArrayList<>();

    private List<UUID> sessionIds = new ArrayList<>();

    /**
     * TODO: REMOVE remoteCallerHostName!
     * Opens a new transmission session and publishes a SessionOpenedEvent.
     * 
     * @param sessionId            The sessionId to open a session for
     * @param metaInformation      The metainformation of the session
     * @param remoteCallerIp       The ip of the device that made the api call
     * @param remoteCallerHostName The hostname of the device that made the api call
     */
    public boolean openSession(UUID sessionId, IMetaInformation metaInformation, String remoteCallerIp,
            String remoteCallerHostName) {
        var status = new SessionStatus(sessionId, metaInformation.getGame(), true, LocalTime.now(), LocalTime.now(),
                null, 0, 0, 1, remoteCallerHostName, remoteCallerIp, "active");
        statuses.add(status);
        sessionIds.add(sessionId);

        var event = new SessionOpenedEvent(sessionId, status, metaInformation);
        // Publish the SessionOpenedEvent
        publish(event);

        return true;
    }

    /**
     * Closes a given session and publishes a SessionClosedEvent.
     * 
     * @param sessionId The sessionId to remove the transmission session of
     */
    public void closeSession(UUID sessionId) {
        // Also disable the session from timeout check
        var status = getStatus(sessionId);
        status.setIsActive(false);
        status.setStatusType("canceled");
        status.setLastRequest(LocalTime.now());
        status.setSessionClosed(LocalTime.now());
        status.setTotalRequests(status.getTotalRequests() + 1);

        // Publish the SessionClosedEvent event
        publish(new SessionClosedEvent(sessionId, status, false));
    }

    public void receiveImage(UUID sessionId, String game, String imageJson) {
        // Set the status
        var status = getStatus(sessionId);
        status.setReceivedImages(status.getReceivedImages() + 1);
        status.setLastRequest(LocalTime.now());
        status.setTotalRequests(status.getTotalRequests() + 1);

        var event = new ImageReceivedEvent(sessionId, status, game,
                DynamicSerializer.deserializeImage(imageJson, game));
        publish(event);
    }

    public void receiveStatistics(UUID sessionId, String game, String statisticsJson) {
        // Set the status
        var status = getStatus(sessionId);
        status.setReceivedStatistics(status.getReceivedStatistics() + 1);
        status.setLastRequest(LocalTime.now());
        status.setTotalRequests(status.getTotalRequests() + 1);

        var event = new StatisticsReceivedEvent(sessionId, status, game,
                DynamicSerializer.deserializeStatistics(statisticsJson, game));
        publish(event);
    }

    public List<UUID> getSessionIds() {
        return this.sessionIds;
    }

    /**
     * Gets the corresponding game for a given sessionId.
     * 
     * @param sessionId The sessionId
     * @return The game if session is active, "" else
     */
    public String getGame(UUID sessionId) {
        var status = getStatus(sessionId);

        return status == null ? "" : status.getGame();
    }

    /**
     * Gets whether a transmission session is active.
     * 
     * @param sessionId The sessionId to check
     * @return True if session is active
     */
    public boolean isSessionActive(UUID sessionId) {
        var status = getStatus(sessionId);

        return status == null ? false : status.isActive();
    }

    public SessionStatus getStatus(UUID sessionId) {
        return StreamUtil.firstOrNull(statuses, x -> x.getSessionId().equals(sessionId));
    }

    public List<SessionStatus> getSessionStatuses() {
        return statuses;
    }

    public List<SessionStatus> getActiveSessionStatuses() {
        return statuses.stream().filter(x -> x.isActive()).collect(Collectors.toList());
    }

    private class SessionOpenedPublisher extends ApiPublisherBase<SessionOpenedEvent> {
    }

    private class SessionClosedPublisher extends ApiPublisherBase<SessionClosedEvent> {
    }

    private class StatisticsReceivedPublisher extends ApiPublisherBase<StatisticsReceivedEvent> {
    }

    private class ImageReceivedPublisher extends ApiPublisherBase<ImageReceivedEvent> {
    }

    private SessionOpenedPublisher sessionOpenedPublisher = new SessionOpenedPublisher();
    private SessionClosedPublisher sessionClosedPublisher = new SessionClosedPublisher();
    private StatisticsReceivedPublisher statisticsReceivedPublisher = new StatisticsReceivedPublisher();
    private ImageReceivedPublisher imageReceivedPublisher = new ImageReceivedPublisher();

    /**
     * Publishes the given event to the ApiEventBus instance.
     * 
     * @param <T>   The type of the event
     * @param event The event to publish
     */
    private <T extends IApiEvent> void publish(T event) {
        if (event instanceof SessionOpenedEvent)
            sessionOpenedPublisher.publish((SessionOpenedEvent) event);
        else if (event instanceof SessionClosedEvent)
            sessionClosedPublisher.publish((SessionClosedEvent) event);
        else if (event instanceof StatisticsReceivedEvent)
            statisticsReceivedPublisher.publish((StatisticsReceivedEvent) event);
        else if (event instanceof ImageReceivedEvent)
            imageReceivedPublisher.publish((ImageReceivedEvent) event);
        else
            throw new RuntimeException("Received unknown event type: " + event.getClass().getName() + "");
    }

}
