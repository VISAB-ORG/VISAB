package org.visab.globalmodel.settlers;

import org.visab.globalmodel.IMetaInformation;

public class SettlersMetaInformation implements IMetaInformation {

    private String game;

    public String setGame() {
        return game;
    }

    @Override
    public String getGame() {
        return game;
    }
}
