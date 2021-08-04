package org.visab.globalmodel.cbrshooter;

import java.util.ArrayList;
import java.util.List;

import org.visab.globalmodel.IStatistics;
import org.visab.globalmodel.IntVector2;

/**
 * The CBRShooterStatistics class, representing the information given by the
 * CBRShooter.
 *
 * @author moritz
 *
 */
public class CBRShooterStatistics implements IStatistics {

    private IntVector2 ammunitionPosition;
    private List<PlayerInformation> players = new ArrayList<>();
    private IntVector2 healthPosition;
    private boolean isAmmunitionCollected;
    private boolean isHealthCollected;
    private boolean isWeaponCollected;
    private int round;
    private float roundTime;
    private IntVector2 weaponPosition;
    private float totalTime;

    public IntVector2 getAmmunitionPosition() {
        return ammunitionPosition;
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

    public IntVector2 getHealthPosition() {
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

    public IntVector2 getWeaponPosition() {
        return weaponPosition;
    }

    public void setAmmunitionPosition(IntVector2 ammunitionPosition) {
        this.ammunitionPosition = ammunitionPosition;
    }

    public void setHealthPosition(IntVector2 healthPosition) {
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

    public void setWeaponPosition(IntVector2 weaponPosition) {
        this.weaponPosition = weaponPosition;
    }
}
