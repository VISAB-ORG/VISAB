package processing;

import java.util.UUID;
import api.WebApi;
import eventbus.ISubscriber;
import eventbus.SessionClosedEvent;
import eventbus.SessionListenerFactory;
import eventbus.StatisticsReceivedEvent;

public abstract class SessionListener<TStatistics>
{
	private String game;
	private UUID sessionId;
	private StatisticsSubscriber statisticsSubscriber;
	private SessionClosedSubscriber sessionClosedSubscriber;
	
	public SessionListener(String game, UUID sessionId) {
		this.game = game;
		this.sessionId = sessionId;
		this.statisticsSubscriber = new StatisticsSubscriber();
		this.sessionClosedSubscriber = new SessionClosedSubscriber();
		
		WebApi.getEventBus().subscribe(statisticsSubscriber);
		WebApi.getEventBus().subscribe(sessionClosedSubscriber);
	}
	
	public abstract void processStatistics(TStatistics statistics);
	
    private class StatisticsSubscriber implements ISubscriber<StatisticsReceivedEvent<TStatistics>> {

		public void invoke(StatisticsReceivedEvent<TStatistics> event) {
			SessionListener.this.processStatistics(event.getStatistics());
		}
    }
    
    private class SessionClosedSubscriber implements ISubscriber<SessionClosedEvent> {

		public void invoke(SessionClosedEvent event) {
			SessionListenerFactory.removeListener(SessionListener.this);
		}
    }
}
