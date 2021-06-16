package org.visab.globalmodel.starter;

import org.visab.globalmodel.IMetaInformation;

public class DefaultMetaInformation implements IMetaInformation {

    private String game;

    public String setGame() {
        return game;
    }

    @Override
    public String getGame() {
        return game;
    }

}
