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
    private List<Player> players;
    private int diceNumberRolled;

    public int getTurn() {
        return turn;
    }

    public int getDiceNumberRolled() {
        return diceNumberRolled;
    }

    public void setDiceNumberRolled(int diceNumberRolled) {
        this.diceNumberRolled = diceNumberRolled;
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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

}
