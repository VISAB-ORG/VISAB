package org.visab.processing.cbrshooter.model;

import org.visab.processing.IStatistics;

/**
 * The CBRShooterStatistics class, representing the information given by the
 * CBRShooter.
 *
 * @author moritz
 *
 */
public class CBRShooterStatistics implements IStatistics {

    private Vector2 ammunitionPosition;
    private PlayerInformation CBRPlayer;
    private String game;
    private Vector2 healthPosition;
    private boolean isAmmunitionCollected;
    private boolean isHealthCollected;
    private boolean isWeaponCollected;
    private int round;
    private float roundTime;
    private PlayerInformation scriptPlayer;
    private Vector2 weaponPosition;

    public Vector2 getAmmunitionPosition() {
	return ammunitionPosition;
    }

    public PlayerInformation getCBRPlayer() {
	return CBRPlayer;
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

    public PlayerInformation getScriptPlayer() {
	return scriptPlayer;
    }

    public Vector2 getWeaponPosition() {
	return weaponPosition;
    }

    public void setAmmunitionPosition(Vector2 ammunitionPosition) {
	this.ammunitionPosition = ammunitionPosition;
    }

    public void setCBRPlayer(PlayerInformation cBRPlayer) {
	CBRPlayer = cBRPlayer;
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

    public void setScriptPlayer(PlayerInformation scriptPlayer) {
	this.scriptPlayer = scriptPlayer;
    }

    public void setWeaponPosition(Vector2 weaponPosition) {
	this.weaponPosition = weaponPosition;
    }
}
