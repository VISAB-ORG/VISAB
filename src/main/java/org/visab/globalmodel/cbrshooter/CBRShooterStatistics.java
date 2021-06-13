package org.visab.globalmodel.cbrshooter;

import java.util.ArrayList;
import java.util.List;

import org.visab.globalmodel.IStatistics;

/**
 * The CBRShooterStatistics class, representing the information given by the
 * CBRShooter.
 *
 * @author moritz
 *
 */
public class CBRShooterStatistics implements IStatistics {

    private Vector2 ammunitionPosition;
    private List<PlayerInformation> players = new ArrayList<>();
    private String game;
    private Vector2 healthPosition;
    private boolean isAmmunitionCollected;
    private boolean isHealthCollected;
    private boolean isWeaponCollected;
    private int round;
    private float roundTime;
    private Vector2 weaponPosition;
    private float totalTime;
    private float speed;

    public Vector2 getAmmunitionPosition() {
        return ammunitionPosition;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(float totalTime) {
        this.totalTime = totalTime;
    }

    public List<PlayerInformation> getPlayers() {
        return players;
    }

    @Override
    public String getGame() {
        return game;
    }

    public Vector2 getHealthPosition() {
        return healthPosition;
    }

    public boolean getIsAmmunitionCollected() {
        return isAmmunitionCollected;
    }

    public boolean getIsHealthCollected() {
        return isHealthCollected;
    }

    public boolean getIsWeaponCollected() {
        return isWeaponCollected;
    }

    public int getRound() {
        return round;
    }

    public float getRoundTime() {
        return roundTime;
    }

    public Vector2 getWeaponPosition() {
        return weaponPosition;
    }

    public void setAmmunitionPosition(Vector2 ammunitionPosition) {
        this.ammunitionPosition = ammunitionPosition;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setHealthPosition(Vector2 healthPosition) {
        this.healthPosition = healthPosition;
    }

    public void setIsAmmunitionCollected(boolean isAmmunitionCollected) {
        this.isAmmunitionCollected = isAmmunitionCollected;
    }

    public void setIsHealthCollected(boolean isHealthCollected) {
        this.isHealthCollected = isHealthCollected;
    }

    public void setIsWeaponCollected(boolean isWeaponCollected) {
        this.isWeaponCollected = isWeaponCollected;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setRoundTime(float roundTime) {
        this.roundTime = roundTime;
    }

    public void setWeaponPosition(Vector2 weaponPosition) {
        this.weaponPosition = weaponPosition;
    }
}
