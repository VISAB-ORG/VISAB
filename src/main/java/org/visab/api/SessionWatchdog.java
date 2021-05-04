package org.visab.api;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.SessionOpenedEvent;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.eventbus.publisher.PublisherBase;
import org.visab.eventbus.subscriber.SubscriberBase;
import org.visab.util.Settings;

/**
 * Class for administering the current transmission sessions. Holds a reference
 * to the current sessions and checks them for timeout. Publishes the
 * SessionOpenedEvent and SessionClosedEvent to the Eventbus.
 *
 * @author moritz
 *
 */
public class SessionWatchdog extends SubscriberBase<StatisticsReceivedEvent> {

    private class SessionClosedPublisher extends PublisherBase<SessionClosedEvent> {
        private void publishSessionClosed(UUID sessionId, boolean closedByTimeout) {
            publish(new SessionClosedEvent(sessionId, closedByTimeout));
        }
    }

    private class SessionOpenedPublisher extends PublisherBase<SessionOpenedEvent> {
        private void publishSessionOpened(UUID sessionId, String game) {
            publish(new SessionOpenedEvent(sessionId, game));
        }
    }

    private static Map<UUID, String> activeSessions = new HashMap<>();

    /**
     * Contains the last times statistics data was sent for the respective session.
     * Used for closing sessions via timeout.
     */
    private static Map<UUID, LocalTime> statisticsSentTimes = new HashMap<>();

    /**
     * Returns the currently active sessions. Warning: Returns a copy, not the
     * reference so don't try modifying this.
     *
     * @return A Map of the currently active transmission sessions and their
     *         respective games
     */
    public static Map<UUID, String> getActiveSessions() {
        return new HashMap<UUID, String>(activeSessions);
    }

    public static String getGame(UUID sessionId) {
        if (activeSessions.containsKey(sessionId))
            return activeSessions.get(sessionId);

        return "";
    }

    public static boolean isSessionActive(UUID sessionId) {
        return activeSessions.containsKey(sessionId);
    }

    private boolean checkTimeouts = true;

    private SessionClosedPublisher closedPublisher = new SessionClosedPublisher();

    private SessionOpenedPublisher openedPublisher = new SessionOpenedPublisher();

    public SessionWatchdog() {
        super(StatisticsReceivedEvent.class);
        WebApi.getEventBus().subscribe(this);

    }

    /**
     * Checks whether one of the current sessions should be timeouted. If that is
     * the case, removes the sessions from activeSessions and statisticsSentTimes.
     * After that a SessionClosedEvent is published.
     */
    private void checkSessionTimeouts() {
        var entries = new ArrayList<Entry<UUID, LocalTime>>();
        entries.addAll(statisticsSentTimes.entrySet());

        for (var entry : entries) {
            var elapsedSeconds = Duration.between(entry.getValue(), LocalTime.now()).toSeconds();
            if (elapsedSeconds >= Settings.SESSION_TIMEOUT) {
                var sessionId = entry.getKey();
                closeSession(sessionId, true);
            }
        }
    }

    public void closeSession(UUID sessionId, boolean closedByTimeout) {
        activeSessions.remove(sessionId);

        // Also remove the session from timeout check
        statisticsSentTimes.remove(sessionId);

        // Publish the SessionClosedEvent event
        closedPublisher.publishSessionClosed(sessionId, closedByTimeout);
    }

    @Override
    public void notify(StatisticsReceivedEvent event) {
        statisticsSentTimes.put(event.getSessionId(), LocalTime.now());
    }

    public void openSession(UUID sessionId, String game) {
        activeSessions.put(sessionId, game);

        // Publish the SessionOpenedEvent
        openedPublisher.publishSessionOpened(sessionId, game);
    }

    /**
     * Starts the infinite timeout checking loop on a different thread.
     */
    public void StartTimeoutLoop() {
        new Thread(() -> {
            try {
                while (checkTimeouts) {
                    checkSessionTimeouts();
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stopTimeoutLoop() {
        checkTimeouts = false;
    }

}
