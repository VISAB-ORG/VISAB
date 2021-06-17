package org.visab.globalmodel.cbrshooter;

import org.visab.globalmodel.IMetaInformation;

// TODO: Decide what to put here.
// We could also decide to not send relative coordinates from the CBRShooter, but have the map size inside of here for example.
public class CBRShooterMetaInformation implements IMetaInformation {

    private String game;

    public String setGame() {
        return game;
    }

    @Override
    public String getGame() {
        return game;
    }

}
