package org.visab.api;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.eventbus.publisher.PublisherBase;
import org.visab.eventbus.subscriber.SubscriberBase;
import org.visab.util.Settings;

/**
 * Class for administering the current transmission sessions. Holds a reference
 * to the current transmission sessions and checks them for timeout. Publishes
 * the SessionOpenedEvent and SessionClosedEvent to the Eventbus.
 *
 * @author moritz
 *
 */
public class SessionWatchdog extends SubscriberBase<StatisticsReceivedEvent> {

    /**
     * The SessionClosedPublisher that publishes the SessionClosedEvent
     */
    private class SessionClosedPublisher extends PublisherBase<SessionClosedEvent> {
    }

    /**
     * The SessionOpenedPublisher that publishes the SessionOpenedEvent
     */
    private class SessionOpenedPublisher extends PublisherBase<SessionOpenedEvent> {
    }

    // Logger needs .class for each class to use for log traces
    private Logger logger = LogManager.getLogger(SessionWatchdog.class);

    /**
     * The currently active tranmission sessions.
     */
    private Map<UUID, String> activeSessions = new HashMap<>();

    /**
     * Contains the last time statistics data was sent from the transmission
     * session. Used for closing sessions via timeout.
     */
    private Map<UUID, LocalTime> sessionSentTimes = new HashMap<>();

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

    private SessionClosedPublisher closedPublisher = new SessionClosedPublisher();

    private SessionOpenedPublisher openedPublisher = new SessionOpenedPublisher();

    private boolean shouldCheckTimeouts;

    public SessionWatchdog() {
        super(StatisticsReceivedEvent.class);
    }

    /**
     * Checks whether one of the current sessions should be timeouted. If that is
     * the case, removes the sessions from activeSessions and statisticsSentTimes.
     * After that a SessionClosedEvent is published.
     */
    private void checkSessionTimeouts() {
        // Make a copy because of potential modification during iteration
        var entries = new ArrayList<Entry<UUID, LocalTime>>();
        entries.addAll(sessionSentTimes.entrySet());

        for (var entry : entries) {
            var elapsedSeconds = Duration.between(entry.getValue(), LocalTime.now()).toSeconds();
            if (elapsedSeconds >= Settings.SESSION_TIMEOUT) {
                var sessionId = entry.getKey();
                closeSession(sessionId, true);
            }
        }
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

        // Also remove the session from timeout check
        sessionSentTimes.remove(sessionId);

        // Publish the SessionClosedEvent event
        closedPublisher.publish(new SessionClosedEvent(sessionId, closedByTimeout));
    }

    @Override
    public void notify(StatisticsReceivedEvent event) {
        sessionSentTimes.put(event.getSessionId(), LocalTime.now());
    }

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

        var event = new SessionOpenedEvent(sessionId, game, remoteCallerIp, remoteCallerHostName);

        // Publish the SessionOpenedEvent
        openedPublisher.publish(event);
    }

    /**
     * Starts the infinite timeout checking loop on a different thread. Can only be
     * terminated by calling stopTimeoutLoop.
     */
    public void StartTimeoutLoop() {
        shouldCheckTimeouts = true;
        new Thread(() -> {
            try {
                while (shouldCheckTimeouts) {
                    checkSessionTimeouts();
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
