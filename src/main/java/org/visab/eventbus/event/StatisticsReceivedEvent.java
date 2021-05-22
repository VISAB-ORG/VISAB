package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.globalmodel.IStatistics;

/**
 * The StatisticsReceivedEvent, that occurs when new statistics are received by
 * the VISAB api.
 *
 * @author moritz
 *
 */
public class StatisticsReceivedEvent extends EventBase {

    private String game;
    private IStatistics statistics;

    public StatisticsReceivedEvent(UUID sessionId, String game, IStatistics statistics) {
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
