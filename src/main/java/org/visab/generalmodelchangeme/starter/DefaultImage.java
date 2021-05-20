package org.visab.generalmodelchangeme.starter;

import org.visab.processing.IImage;

public class DefaultImage implements IImage {

    private String json;
    private String game;

    public DefaultImage(String game, String json) {
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
