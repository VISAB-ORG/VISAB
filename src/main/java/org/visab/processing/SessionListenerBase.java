package org.visab.processing;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.visab.api.WebApi;
import org.visab.eventbus.ISubscriber;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.eventbus.subscriber.SubscriberBase;
import org.visab.generalmodelchangeme.IStatistics;
import org.visab.repository.DatabaseRepository;
import org.visab.workspace.DatabaseManager;
import org.visab.workspace.Workspace;

/**
 * The base SessionListener class, that should be implemented by all session
 * listeners. TODO: Should have their own logger instance or configuration, such
 * that they will always write their identification (sessionid + game) before
 * messages
 * 
 * @author moritz
 *
 * @param <TStatistics> The statistics type, that will be processed by the
 *                      listener
 */
public abstract class SessionListenerBase<TStatistics extends IStatistics> implements ISessionListener<TStatistics> {

    /**
     * The SessionClosedSubscriber, that subscribes to the SessionClosedEvent event.
     * This has to be a nested class, due to implementing two generic
     * Interfaces/Classes not being allowed.
     *
     * @author moritz
     *
     */
    private class SessionClosedSubscriber extends SubscriberBase<SessionClosedEvent> {

        public SessionClosedSubscriber() {
            super(SessionClosedEvent.class);
        }

        @Override
        public void notify(SessionClosedEvent event) {
            if (event.getSessionId().equals(sessionId)) {
                SessionListenerAdministration.removeListener(SessionListenerBase.this);

                for (var sub : subscribers)
                    WebApi.getEventBus().unsubscribe(sub);

                isActive = false;
                onSessionClosed();
            }
        }
    }

    /**
     * The StatisticsSubscriber, that subscribes to the StatisticsReceivedEvent.
     * This has to be a nested class, due to implementing two generic
     * Interfaces/Classes not being allowed.
     *
     * @author moritz
     *
     */
    private class StatisticsSubscriber extends SubscriberBase<StatisticsReceivedEvent> {

        public StatisticsSubscriber() {
            super(StatisticsReceivedEvent.class);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void notify(StatisticsReceivedEvent event) {
            if (event.getSessionId().equals(sessionId)) {
                lastReceived = LocalTime.now();

                var statistics = event.getStatistics();
                if (statistics == null)
                    // TODO: Log here!
                    System.out.println("Received Statistics was null!");
                else
                    processStatistics((TStatistics) statistics);
            }
        }
    }

    protected String game;
    protected boolean isActive = true;

    /**
     * The time at which the last statistics object was received for the session.
     */
    protected LocalTime lastReceived = LocalTime.now();

    protected DatabaseManager manager = Workspace.instance.getDatabaseManager();

    protected UUID sessionId;

    /**
     * List of all subscribers. All subscribers in this list will be unsubscribed on
     * the SessionClosedEvent.
     */
    protected List<ISubscriber<?>> subscribers = new ArrayList<>();

    public SessionListenerBase(String game, UUID sessionId) {
        this.game = game;
        this.sessionId = sessionId;

        var statisticsSubscriber = new StatisticsSubscriber();
        var sessionClosedSubscriber = new SessionClosedSubscriber();

        WebApi.getEventBus().subscribe(statisticsSubscriber);
        WebApi.getEventBus().subscribe(sessionClosedSubscriber);

        subscribers.add(statisticsSubscriber);
        subscribers.add(sessionClosedSubscriber);
    }

    @Override
    public String getGame() {
        return game;
    }

    @Override
    public LocalTime getLastReceived() {
        return lastReceived;
    }

    @Override
    public UUID getSessionId() {
        return sessionId;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public abstract void onSessionClosed();

    @Override
    public abstract void onSessionStarted();

    /**
     * Called upon reciving statistics for the current session. Is only called if
     * the received statistics object was not null.
     * 
     * @param statistics A TStatistics object
     */
    public abstract void processStatistics(TStatistics statistics);
}
