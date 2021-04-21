package eventbus.event;

import java.util.UUID;

import processing.IStatistics;

public class StatisticsReceivedEvent extends EventBase {

    private String game;
    private IStatistics statistics;

    public StatisticsReceivedEvent(String game, UUID sessionId, IStatistics statistics) {
	super(sessionId);
	this.game = game;
	this.statistics = statistics;
    }

    public String getGame() {
	return game;
    }

    public IStatistics getStatistics() {
	return statistics;
    }
}
