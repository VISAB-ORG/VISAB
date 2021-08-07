package org.visab.globalmodel.starter;

import org.visab.globalmodel.IImageContainer;

public class DefaultImage implements IImageContainer {

    private String json;

    public DefaultImage(String json) {
        this.json = json;
    }

    public String getJson() {
        return json;
    }

}
