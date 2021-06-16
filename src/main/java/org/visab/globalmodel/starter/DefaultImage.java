package org.visab.globalmodel.starter;

import org.visab.globalmodel.IImage;

public class DefaultImage implements IImage {

    private String json;

    public DefaultImage(String json) {
        this.json = json;
    }

    public String getJson() {
        return json;
    }

}
