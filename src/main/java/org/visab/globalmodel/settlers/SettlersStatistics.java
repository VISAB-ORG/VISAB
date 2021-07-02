package org.visab.globalmodel.settlers;

import java.util.List;

import org.visab.globalmodel.IStatistics;

/**
 * The SettlersStatistics class, representing the information given by the Unity
 * Settlers of Catan Game.
 *
 * @author leonr
 *
 */
public class SettlersStatistics implements IStatistics {

    private int turn;
    private String turnTimeStamp;
    private List<PlayerInformation> players = null;

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

    public List<PlayerInformation> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerInformation> players) {
        this.players = players;
    }

}
