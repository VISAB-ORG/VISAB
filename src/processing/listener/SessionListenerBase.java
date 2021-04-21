package processing.listener;

import java.util.UUID;

import api.WebApi;
import eventbus.ISubscriber;
import eventbus.event.SessionClosedEvent;
import eventbus.event.StatisticsReceivedEvent;
import eventbus.subscriber.SubscriberBase;
import processing.ISessionListener;
import processing.ISessionListenerWithStatistics;

public abstract class SessionListenerBase<TStatistics>
	implements ISessionListener, ISessionListenerWithStatistics<TStatistics> {

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
