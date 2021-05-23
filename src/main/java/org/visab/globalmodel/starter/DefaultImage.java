package org.visab.globalmodel.starter;

import org.visab.globalmodel.IImage;

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
