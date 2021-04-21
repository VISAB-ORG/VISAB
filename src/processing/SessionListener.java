package processing;

import java.util.UUID;

import api.WebApi;
import eventbus.IEvent;
import eventbus.ISubscriber;
import eventbus.SessionClosedEvent;
import eventbus.SessionListenerFactory;
import eventbus.StatisticsReceivedEvent;
import eventbus.SubscriberBase;

public abstract class SessionListener<TStatistics> {

    private class SessionClosedSubscriber extends SubscriberBase<SessionClosedEvent> {

	public SessionClosedSubscriber() {
	    super(new SessionClosedEvent(null).getClass().getSimpleName());
	}

	@Override
	@SuppressWarnings("unchecked")
	public void invoke(SessionClosedEvent event) {
	    SessionListenerFactory.removeListener(SessionListener.this);

	    WebApi.getEventBus().unsubscribe((ISubscriber<IEvent>) (Object) statisticsSubscriber);
	    WebApi.getEventBus().unsubscribe((ISubscriber<IEvent>) (Object) sessionClosedSubscriber);

	    SessionListener.this.onSessionClosed();
	}
    }

    private class StatisticsSubscriber extends SubscriberBase<StatisticsReceivedEvent> {

	public StatisticsSubscriber() {
	    super(new StatisticsReceivedEvent(null, null, null).getClass().getSimpleName());
	}

	@Override
	@SuppressWarnings("unchecked")
	public void invoke(StatisticsReceivedEvent event) {
	    SessionListener.this.processStatistics((TStatistics) event.getStatistics());
	}
    }

    private String game;
    private SessionClosedSubscriber sessionClosedSubscriber;
    private UUID sessionId;
    private StatisticsSubscriber statisticsSubscriber;

    @SuppressWarnings("unchecked")
    public SessionListener(String game, UUID sessionId) {
	this.game = game;
	this.sessionId = sessionId;
	this.statisticsSubscriber = new StatisticsSubscriber();
	this.sessionClosedSubscriber = new SessionClosedSubscriber();

	WebApi.getEventBus().subscribe((ISubscriber<IEvent>) (Object) statisticsSubscriber);
	WebApi.getEventBus().subscribe((ISubscriber<IEvent>) (Object) sessionClosedSubscriber);

	onSessionStarted();
    }

    public String getGame() {
	return game;
    }

    public UUID getSessionId() {
	return sessionId;
    }

    public abstract void onSessionClosed();

    public abstract void onSessionStarted();

    public abstract void processStatistics(TStatistics statistics);
}
