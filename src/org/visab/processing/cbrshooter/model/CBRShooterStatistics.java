package org.visab.processing.cbrshooter.model;

import java.util.Vector;

import org.visab.processing.IStatistics;

/**
 * The CBRShooterStatistics class, representing the information given by the
 * CBRShooter.
 *
 * @author moritz
 *
 */
public class CBRShooterStatistics implements IStatistics {

    private Vector<Float> ammunitionPosition;
    private PlayerInformation CBRPlayer;
    private String game;
    private Vector<Float> healthPositon;
    private boolean isAmmunitionCollected;
    private boolean isHealthCollected;
    private boolean isWeaponCollected;
    private int round;
    private float roundTime;
    private PlayerInformation ScriptPlayer;
    private Vector<Float> weaponPosition;

    public Vector<Float> getAmmunitionPosition() {
	return ammunitionPosition;
    }

    public PlayerInformation getCBRPlayer() {
	return CBRPlayer;
    }

    @Override
    public String getGame() {
	return game;
    }

    public Vector<Float> getHealthPositon() {
	return healthPositon;
    }

    public int getRound() {
	return round;
    }

    public float getRoundTime() {
	return roundTime;
    }

    public PlayerInformation getScriptPlayer() {
	return ScriptPlayer;
    }

    public Vector<Float> getWeaponPosition() {
	return weaponPosition;
    }

    public boolean isAmmunitionCollected() {
	return isAmmunitionCollected;
    }

    public boolean isHealthCollected() {
	return isHealthCollected;
    }

    public boolean isWeaponCollected() {
	return isWeaponCollected;
    }

    public void setAmmunitionCollected(boolean isAmmunitionCollected) {
	this.isAmmunitionCollected = isAmmunitionCollected;
    }

    public void setAmmunitionPosition(Vector<Float> ammunitionPosition) {
	this.ammunitionPosition = ammunitionPosition;
    }

    public void setCBRPlayer(PlayerInformation cBRPlayer) {
	CBRPlayer = cBRPlayer;
    }

    public void setGame(String game) {
	this.game = game;
    }

    public void setHealthCollected(boolean isHealthCollected) {
	this.isHealthCollected = isHealthCollected;
    }

    public void setHealthPositon(Vector<Float> healthPositon) {
	this.healthPositon = healthPositon;
    }

    public void setRound(int round) {
	this.round = round;
    }

    public void setRoundTime(float roundTime) {
	this.roundTime = roundTime;
    }

    public void setScriptPlayer(PlayerInformation scriptPlayer) {
	ScriptPlayer = scriptPlayer;
    }

    public void setWeaponCollected(boolean isWeaponCollected) {
	this.isWeaponCollected = isWeaponCollected;
    }

    public void setWeaponPosition(Vector<Float> weaponPosition) {
	this.weaponPosition = weaponPosition;
    }
}
