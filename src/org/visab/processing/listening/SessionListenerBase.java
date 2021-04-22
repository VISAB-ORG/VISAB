package org.visab.processing.listening;

import java.util.UUID;

import org.visab.api.WebApi;
import org.visab.eventbus.ISubscriber;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.eventbus.subscriber.SubscriberBase;
import org.visab.processing.ISessionListener;
import org.visab.processing.ISessionListenerWithStatistics;

/**
 * The base SessionListener class, that should be implemented by all session
 * listeners.
 *
 * @author moritz
 *
 * @param <TStatistics> The statistics type, that will be processed by the
 *                      listeners
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
	    super(new SessionClosedEvent(null).getClass().getSimpleName());
	}

	@Override
	public void invoke(SessionClosedEvent event) {
	    SessionListenerFactory.removeListener((ISessionListener) SessionListenerBase.this);

	    WebApi.getEventBus().unsubscribe((ISubscriber) statisticsSubscriber);
	    WebApi.getEventBus().unsubscribe((ISubscriber) sessionClosedSubscriber);

	    SessionListenerBase.this.onSessionClosed();
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
	    SessionListenerBase.this.processStatistics((TStatistics) event.getStatistics());
	}
    }

    private String game;
    private SessionClosedSubscriber sessionClosedSubscriber;
    private UUID sessionId;
    private StatisticsSubscriber statisticsSubscriber;

    public SessionListenerBase(String game, UUID sessionId) {
	this.game = game;
	this.sessionId = sessionId;
	this.statisticsSubscriber = new StatisticsSubscriber();
	this.sessionClosedSubscriber = new SessionClosedSubscriber();

	WebApi.getEventBus().subscribe((ISubscriber) statisticsSubscriber);
	WebApi.getEventBus().subscribe((ISubscriber) sessionClosedSubscriber);

	onSessionStarted();
    }

    @Override
    public String getGame() {
	return game;
    }

    @Override
    public UUID getSessionId() {
	return sessionId;
    }

    @Override
    public abstract void onSessionClosed();

    @Override
    public abstract void onSessionStarted();

    @Override
    public abstract void processStatistics(TStatistics statistics);
}
