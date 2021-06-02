package org.visab.api;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.visab.globalmodel.TransmissionSessionStatus;
import org.visab.util.StreamUtil;

/**
 * Class for administering the current transmission sessions. Holds a reference
 * to the current transmission sessions and checks them for timeout. Publishes
 * all api related events.
 *
 * @author moritz
 *
 */
public class TempThingy {

    // Logger needs .class for each class to use for log traces
    private Logger logger = LogManager.getLogger(TempThingy.class);

    /**
     * An overview of transmission session specific data.
     */
    private List<TransmissionSessionStatus> statuses = new ArrayList<>();

    /**
     * Opens a new transmission session and publishes a SessionOpenedEvent.
     * 
     * @param sessionId            The sessionId to open a session for
     * @param game                 The game of the session
     * @param remoteCallerIp       The ip of the device that made the api call
     * @param remoteCallerHostName The hostname of the device that made the api call
     */
    public void openSession(UUID sessionId, String game, String remoteCallerIp, String remoteCallerHostName) {
        var status = new TransmissionSessionStatus(sessionId, game, true, LocalTime.now(), LocalTime.now(), null, 0, 0,
                1, remoteCallerHostName, remoteCallerIp);
        statuses.add(status);

        var event = new SessionOpenedEvent(sessionId, status, game);

        // Publish the SessionOpenedEvent
        publish(event);
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

        return status == null ? false : status.getIsActive();
    }

    public TransmissionSessionStatus getStatus(UUID sessionId) {
        return StreamUtil.firstOrNull(statuses, x -> x.getSessionId().equals(sessionId));
    }

    public List<TransmissionSessionStatus> getSessionStatuses() {
        return statuses;
    }

    public List<TransmissionSessionStatus> getActiveSessionStatuses() {
        return statuses.stream().filter(x -> x.getIsActive()).collect(Collectors.toList());
    }

    private class SessionOpenedPublisher extends ApiPublisherBase<SessionOpenedEvent> {
    }

    private class SessionClosedPublisher extends ApiPublisherBase<SessionClosedEvent> {
    }

    private class StatisticsReceivedPublisher extends ApiPublisherBase<StatisticsReceivedEvent> {
    }

    private class ImageReceivedPublisher extends ApiPublisherBase<ImageReceivedEvent> {
    }

    protected SessionOpenedPublisher sessionOpenedPublisher = new SessionOpenedPublisher();
    protected SessionClosedPublisher sessionClosedPublisher = new SessionClosedPublisher();
    protected StatisticsReceivedPublisher statisticsReceivedPublisher = new StatisticsReceivedPublisher();
    protected ImageReceivedPublisher imageReceivedPublisher = new ImageReceivedPublisher();

    /**
     * Publishes the given event to the ApiEventBus instance.
     * 
     * @param <T>   The type of the event
     * @param event The event to publish
     */
    public <T extends IApiEvent> void publish(T event) {
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
