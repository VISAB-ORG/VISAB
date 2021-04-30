package org.visab.processing;

import java.time.LocalTime;
import java.util.UUID;

import org.visab.api.WebApi;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.eventbus.subscriber.SubscriberBase;
import org.visab.repository.VISABRepository;

/**
 * The base SessionListener class, that should be implemented by all session
 * listeners.
 *
 * @author moritz
 *
 * @param <TStatistics> The statistics type, that will be processed by the
 *                      listener
 */
public abstract class SessionListenerBase<TStatistics>
        implements ISessionListener, ISessionListenerWithStatistics<TStatistics> {

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
            super(new SessionClosedEvent(null, false).getClass().getSimpleName());
        }

        @Override
        public void invoke(SessionClosedEvent event) {
            if (event.getSessionId().equals(SessionListenerBase.this.sessionId)) {
                SessionListenerAdministration.removeListener(SessionListenerBase.this);

                WebApi.getEventBus().unsubscribe(statisticsSubscriber);
                WebApi.getEventBus().unsubscribe(sessionClosedSubscriber);

                isActive = false;

                SessionListenerBase.this.onSessionClosed();
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
            super(new StatisticsReceivedEvent(null, null, null).getClass().getSimpleName());
        }

        @Override
        @SuppressWarnings("unchecked")
        public void invoke(StatisticsReceivedEvent event) {
            if (event.getSessionId().equals(SessionListenerBase.this.sessionId)) {
                lastReceived = LocalTime.now();
                SessionListenerBase.this.processStatistics((TStatistics) event.getStatistics());
            }
        }
    }

    protected String game;
    protected boolean isActive = true;

    /**
     * The time at which the last statistics object was received for the session.
     */
    protected LocalTime lastReceived = LocalTime.now();
    protected VISABRepository repo = new VISABRepository();

    private SessionClosedSubscriber sessionClosedSubscriber;
    protected UUID sessionId;

    private StatisticsSubscriber statisticsSubscriber;

    public SessionListenerBase(String game, UUID sessionId) {
        this.game = game;
        this.sessionId = sessionId;
        this.statisticsSubscriber = new StatisticsSubscriber();
        this.sessionClosedSubscriber = new SessionClosedSubscriber();

        WebApi.getEventBus().subscribe(statisticsSubscriber);
        WebApi.getEventBus().subscribe(sessionClosedSubscriber);

        onSessionStarted();
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

    @Override
    public abstract void processStatistics(TStatistics statistics);
}
