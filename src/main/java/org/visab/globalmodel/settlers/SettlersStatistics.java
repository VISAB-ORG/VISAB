package org.visab.globalmodel.settlers;

import org.visab.globalmodel.IStatistics;

/**
 * The SettlersStatistics class, representing the information given by the Unity
 * Settlers of Catan Game.
 *
 * @author leonr
 *
 */
public class SettlersStatistics implements IStatistics {

    private PlayerInformation player1;

    public PlayerInformation getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerInformation player1) {
        this.player1 = player1;
    }

    public PlayerInformation getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerInformation player2) {
        this.player2 = player2;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getTurnTimeStamp() {
        return turnTimeStamp;
    }

    public void setTurnTimeStamp(String turnTimeStamp) {
        this.turnTimeStamp = turnTimeStamp;
    }

    public void setGame(String game) {
        this.game = game;
    }

    private PlayerInformation player2;

    private int turn;

    private String turnTimeStamp;

    private String game;

    @Override
    public String getGame() {
        // TODO Auto-generated method stub
        return game;
    }

}
