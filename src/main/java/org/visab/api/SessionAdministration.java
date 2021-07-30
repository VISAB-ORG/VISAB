package org.visab.api;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
 * The SessionAdministration class is used for administering the current
 * transmission sessions. It has a list of all the transmission sessions that
 * are currently open and publishes all API related events.
 *
 * @author moritz
 *
 */
public class SessionAdministration {

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
     * A list of the session statuses for all transmission sessions of the current
     * runtime.
     */
    private List<SessionStatus> statuses = new ArrayList<>();

    private List<UUID> sessionIds = new ArrayList<>();

    /**
     * Opens a new transmission session and publishes a SessionOpenedEvent.
     * 
     * @param sessionId       The sessionId to open a session for
     * @param metaInformation The IMetaInformation of the session
     * @param remoteCallerIp  The ip of the device that made the API call
     * @return True
     */
    public boolean openSession(UUID sessionId, IMetaInformation metaInformation, String remoteCallerIp) {
        var status = new SessionStatus(sessionId, metaInformation.getGame(), true, LocalTime.now(), LocalTime.now(),
                null, 0, 0, 1, remoteCallerIp, "active");
        statuses.add(status);
        sessionIds.add(sessionId);

        // Publish the SessionOpenedEvent
        publish(new SessionOpenedEvent(sessionId, status, metaInformation));

        return true;
    }

    /**
     * Closes a transmission session and publishes a SessionClosedEvent.
     * 
     * @param sessionId The sessionId of the transmission session to close
     * @return True
     */
    public boolean closeSession(UUID sessionId) {
        var status = getStatus(sessionId);
        status.setIsActive(false);
        status.setStatusType("canceled");
        status.setLastRequest(LocalTime.now());
        status.setSessionClosed(LocalTime.now());
        status.setTotalRequests(status.getTotalRequests() + 1);

        // Publish the SessionClosedEvent event
        publish(new SessionClosedEvent(sessionId, status, false));

        return true;
    }

    /**
     * Deserializes an IImageContainer object from a json string and publishes a
     * ImageReceivedEvent.
     * 
     * @param sessionId The sessionId of the tranmission session who sent the images
     * @param game      The game of the tranmission session who sent the images
     * @param imageJson The IImageContainer json
     */
    public void receiveImage(UUID sessionId, String game, String imageJson) {
        // Set the status
        var status = getStatus(sessionId);
        status.setReceivedImages(status.getReceivedImages() + 1);
        status.setLastRequest(LocalTime.now());
        status.setTotalRequests(status.getTotalRequests() + 1);

        var imageContainer = DynamicSerializer.deserializeImage(imageJson, game);

        // Publish the ImageReceivedEvent
        publish(new ImageReceivedEvent(sessionId, status, game, imageContainer));
    }

    /**
     * Deserializes an IStatistics object from a json string and publishes a
     * ImageReceivedEvent.
     * 
     * @param sessionId The sessionId of the tranmission session who sent the images
     * @param game      The game of the tranmission session who sent the images
     * @param imageJson The IStatistics json
     */
    public void receiveStatistics(UUID sessionId, String game, String statisticsJson) {
        // Set the status
        var status = getStatus(sessionId);
        status.setReceivedStatistics(status.getReceivedStatistics() + 1);
        status.setLastRequest(LocalTime.now());
        status.setTotalRequests(status.getTotalRequests() + 1);

        var statistics = DynamicSerializer.deserializeStatistics(statisticsJson, game);

        // Publish the StatisticsReceivedEvent
        publish(new StatisticsReceivedEvent(sessionId, status, game, statistics));
    }

    /**
     * A list containing the session statuses for all transmission sessions of the
     * current runtime.
     */
    public List<SessionStatus> getSessionStatuses() {
        return new ArrayList<SessionStatus>(statuses);
    }

    /**
     * A list containing the session statuses for all currently active tranmission
     * session.
     */
    public List<SessionStatus> getActiveSessionStatuses() {
        return statuses.stream().filter(x -> x.isActive()).collect(Collectors.toList());
    }

    /**
     * Gets the SessionStatus of a tranmission session.
     * 
     * @param sessionId The sessionId whose session status to get
     * @return The SessionStatus of the tranmission session if found, null else
     */
    public SessionStatus getStatus(UUID sessionId) {
        return StreamUtil.firstOrNull(statuses, x -> x.getSessionId().equals(sessionId));
    }

    /**
     * Checks if the transmission session is active.
     * 
     * @param sessionId The sessionId whose session status to check
     * @return True if session is active
     */
    public boolean isSessionActive(UUID sessionId) {
        var status = getStatus(sessionId);

        return status == null ? false : status.isActive();
    }

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
            throw new RuntimeException("Received unhandeled event type: " + event.getClass().getName());
    }

}
