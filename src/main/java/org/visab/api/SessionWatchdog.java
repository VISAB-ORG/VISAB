package org.visab.api;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.dynamic.DynamicSerializer;
import org.visab.eventbus.event.ImageReceivedEvent;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.globalmodel.TransmissionSessionStatus;
import org.visab.util.Settings;

/**
 * Class for administering the current transmission sessions. Holds a reference
 * to the current transmission sessions and checks them for timeout. Publishes
 * all api related events.
 *
 * @author moritz
 *
 */
public class SessionWatchdog extends ApiEventPublisher {

    // Logger needs .class for each class to use for log traces
    private Logger logger = LogManager.getLogger(SessionWatchdog.class);

    /**
     * The currently active tranmission sessions.
     */
    private Map<UUID, String> activeSessions = new HashMap<>();

    /**
     * An overview of transmission session specific data. Used for closing sessions
     * via timeout.
     */
    private Map<UUID, TransmissionSessionStatus> sessionStatus = new HashMap<>();

    private boolean shouldCheckTimeouts;

    /**
     * Opens a new transmission session and publishes a SessionOpenedEvent.
     * 
     * @param sessionId            The sessionId to open a session for
     * @param game                 The game of the session
     * @param remoteCallerIp       The ip of the device that made the api call
     * @param remoteCallerHostName The hostname of the device that made the api call
     */
    public void openSession(UUID sessionId, String game, String remoteCallerIp, String remoteCallerHostName) {
        activeSessions.put(sessionId, game);
        var status = new TransmissionSessionStatus(sessionId, game, true, LocalTime.now(), LocalTime.now(), null, 0, 0,
                1, remoteCallerHostName, remoteCallerIp);
        sessionStatus.put(sessionId, status);

        var event = new SessionOpenedEvent(sessionId, status, game);

        // Publish the SessionOpenedEvent
        publish(event);
    }

    /**
     * Closes a given session and publishes a SessionClosedEvent.
     * 
     * @param sessionId       The sessionId to remove the transmission session of
     * @param closedByTimeout Whether the session was closed from the timeout loop
     *                        or by api call
     */
    public void closeSession(UUID sessionId, boolean closedByTimeout) {
        activeSessions.remove(sessionId);

        // Also disable the session from timeout check
        var status = sessionStatus.get(sessionId);
        status.setIsActive(false);
        status.setLastRequest(LocalTime.now());
        status.setSessionClosed(LocalTime.now());
        status.setTotalRequests(status.getTotalRequests() + 1);

        // Publish the SessionClosedEvent event
        publish(new SessionClosedEvent(sessionId, status, closedByTimeout));
    }

    public void imageReceived(UUID sessionId, String game, String imageJson) {
        // Set the status
        var status = sessionStatus.get(sessionId);
        status.setReceivedImages(status.getReceivedImages() + 1);
        status.setLastRequest(LocalTime.now());
        status.setTotalRequests(status.getTotalRequests() + 1);

        var event = new ImageReceivedEvent(sessionId, status, game,
                DynamicSerializer.deserializeImage(imageJson, game));
        publish(event);
    }

    public void statisticsReceived(UUID sessionId, String game, String statisticsJson) {
        // Set the status
        var status = sessionStatus.get(sessionId);
        status.setReceivedStatistics(status.getReceivedStatistics() + 1);
        status.setLastRequest(LocalTime.now());
        status.setTotalRequests(status.getTotalRequests() + 1);

        var event = new StatisticsReceivedEvent(sessionId, status, game,
                DynamicSerializer.deserializeStatistics(statisticsJson, game));
        publish(event);
    }

    /**
     * Returns a copy of the currently active sessions. Warning: Returns a copy, not
     * the reference so dont try modifying this.
     *
     * @return A Map of the currently active transmission sessions and their
     *         respective games
     */
    public Map<UUID, String> getActiveSessions() {
        return new HashMap<UUID, String>(activeSessions);
    }

    /**
     * Gets the corresponding game for a given sessionId.
     * 
     * @param sessionId The sessionId
     * @return The game if session is active, "" else
     */
    public String getGame(UUID sessionId) {
        if (activeSessions.containsKey(sessionId))
            return activeSessions.get(sessionId);

        return "";
    }

    /**
     * Gets whether a transmission session is active.
     * 
     * @param sessionId The sessionId to check
     * @return True if session is active
     */
    public boolean isSessionActive(UUID sessionId) {
        return activeSessions.containsKey(sessionId);
    }

    public TransmissionSessionStatus getStatus(UUID sessionId) {
        return sessionStatus.get(sessionId);
    }

    public List<TransmissionSessionStatus> getAllSessionStatus() {
        // Decide if only return active here.
        return new ArrayList<TransmissionSessionStatus>(sessionStatus.values());
    }

    /**
     * Starts the infinite timeout checking loop on a different thread. Can only be
     * terminated by calling stopTimeoutLoop.
     */
    public void StartTimeoutLoop() {
        shouldCheckTimeouts = true;
        new Thread(() -> {
            try {
                /**
                 * Checks whether one of the current sessions should be timeouted. If that is
                 * the case, removes the sessions from activeSessions and sessionRequestTimes.
                 * After that a SessionClosedEvent is published.
                 */
                while (shouldCheckTimeouts) {
                    for (var status : sessionStatus.values()) {
                        System.out.println(status.getIsActive());
                        if (!status.getIsActive())
                            continue;

                        var elapsedSeconds = Duration.between(status.getLastRequest(), LocalTime.now()).toSeconds();

                        // If nothing was sent yet (only session openend) wait 30 more seconds until
                        // timeout.
                        var timeoutSeconds = Settings.SESSION_TIMEOUT;
                        if (status.getTotalRequests() == 1)
                            timeoutSeconds = Settings.SESSION_TIMEOUT + 30;

                        // Close session
                        if (elapsedSeconds >= timeoutSeconds) {
                            closeSession(status.getSessionId(), true);
                        }
                    }
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    /**
     * Stops the timeout loop
     */
    public void stopTimeoutLoop() {
        shouldCheckTimeouts = false;
    }

}
