package org.visab.processing.starter.model;

import org.visab.processing.IStatistics;

public class DefaultStatistics implements IStatistics {

    private String json;
    private String game;

    public DefaultStatistics(String game, String json) {
        this.game = game;
        this.json = json;
    }

    @Override
    public String getGame() {
        return game;
    }

    public String getJson() {
        return json;
    }
}
