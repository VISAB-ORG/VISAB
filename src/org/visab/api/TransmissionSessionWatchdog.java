package org.visab.api;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.visab.eventbus.IPublisher;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.eventbus.subscriber.SubscriberBase;
import org.visab.util.Settings;

/**
 * Class for administering the current transmission sessions. Holds a reference to the current sessions and checks them for timeout.
 * 
 * @author moritz
 * 
 */
public class TransmissionSessionWatchdog extends SubscriberBase<StatisticsReceivedEvent>
        implements IPublisher<SessionClosedEvent> {

    /**
     * Contains the last times statistics data was sent for the respective session.
     * Used for closing sessions via timeout.
     */
    private static Map<UUID, LocalTime> statisticsSentTimes = new HashMap<>();

    private static Map<UUID, String> activeSessions = new HashMap<>();

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

    public static boolean isSessionActive(UUID sessionId) {
        return activeSessions.containsKey(sessionId);
    }

    public static String getGame(UUID sessionId) {
        if (activeSessions.containsKey(sessionId))
            return activeSessions.get(sessionId);

        return "";
    }

    public static void addSession(UUID sessionId, String game) {
        activeSessions.put(sessionId, game);
    }

    public static void removeSession(UUID sessionId) {
        activeSessions.remove(sessionId);

        // Also remove the session from timeout check
        statisticsSentTimes.remove(sessionId);
    }

    public TransmissionSessionWatchdog() {
        super(new StatisticsReceivedEvent(null, null, null).getClass().getSimpleName());
        WebApi.getEventBus().subscribe(this);

        // Starts the infinite timeout checking loop on a different thread.
        new Thread(() -> {
            try {
                // TODO: Check if loop should break
                while (true) {
                    checkSessionTimeouts();
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("CAUGHT [" + e + "] when running the timeout loop!");
            }
        }).start();
    }

    /**
     * Checks whether one of the current sessions should be timeouted.
     * If that is the case, removes the sessions from activeSessions and statisticsSentTimes.
     * After that a SessionClosedEvent is published.
     */
    private void checkSessionTimeouts() {
        var entries = new ArrayList<Entry<UUID, LocalTime>>();
        entries.addAll(statisticsSentTimes.entrySet());

        for (var entry : entries) {
            var elapsedSeconds = Duration.between(entry.getValue(), LocalTime.now()).toSeconds();
            if (elapsedSeconds >= Settings.SESSION_TIMEOUT) {
                var sessionId = entry.getKey();
                removeSession(sessionId);

                // Invoke the SessionClosedEvent event manually.
                publish(new SessionClosedEvent(sessionId, true));
            }
        }
    }

    @Override
    public void invoke(StatisticsReceivedEvent event) {
        statisticsSentTimes.put(event.getSessionId(), LocalTime.now());
    }

    @Override
    public void publish(SessionClosedEvent event) {
        WebApi.getEventBus().publish(event);
    }

}
