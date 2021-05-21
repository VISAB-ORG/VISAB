package org.visab.newgui.statistics.cbrshooter.model;

import org.visab.generalmodelchangeme.cbrshooter.Vector2;

public class CBRShooterStatisticsRow {

    private Vector2 playerOnePosition;

    public CBRShooterStatisticsRow(Vector2 playerOnePosition) {
        this.playerOnePosition = playerOnePosition;
    }

    public Vector2 getPlayerOnePosition() {
        return this.playerOnePosition;
    }
}