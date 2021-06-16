package org.visab.globalmodel.starter;

import org.visab.globalmodel.IMetaInformation;

public class DefaultMetaInformation implements IMetaInformation {

    private String json;

    public DefaultMetaInformation(String game, String json) {
        this.game = game;
        this.setJson(json);
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    private String game;

    public String setGame() {
        return game;
    }

    @Override
    public String getGame() {
        return game;
    }

}
