package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.eventbus.APIEventBase;
import org.visab.globalmodel.IStatistics;
import org.visab.globalmodel.SessionStatus;

/**
 * The StatisticsReceivedEvent, that occurs when new statistics are received by
 * the VISAB api.
 */
public class StatisticsReceivedEvent extends APIEventBase {

    private String game;
    private IStatistics statistics;

    public StatisticsReceivedEvent(UUID sessionId, SessionStatus status, String game, IStatistics statistics) {
        super(sessionId, status);
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
