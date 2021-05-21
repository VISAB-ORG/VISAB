package org.visab.newgui.statistics.cbrshooter.model;

public class CBRShooterStatisticsRow {

    private Vector2 playerOnePosition;

    public CBRShooterStatisticsRow(Vector2 playerOnePosition) {
        this.playerOnePosition = playerOnePosition;
    }

    public Vector2 getPlayerOnePosition() {
        return this.playerOnePosition;
    }
}