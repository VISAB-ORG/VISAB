package org.visab.generalmodelchangeme.starter;

import org.visab.processing.IMapImage;

public class DefaultMapImage implements IMapImage {

    private String json;
    private String game;

    public DefaultMapImage(String game, String json) {
        this.game = game;
        this.json = json;
    }

    public String getGame() {
        return game;
    }

    public String getJson() {
        return json;
    }

}
