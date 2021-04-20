package eventbus;

import java.util.UUID;

public class StatisticsReceivedEvent<T> implements IEvent {

	private String game;
	private UUID sessionId;
	private T statistics;
	
	public StatisticsReceivedEvent(String game, UUID sessionId, T statistics) {
		this.game = game;
		this.sessionId = sessionId;
		this.statistics = statistics;
	}

	public String getGame() {
		return game;
	}

	public UUID getSessionId() {
		return sessionId;
	}

	public T getStatistics() {
		return statistics;
	}
}
