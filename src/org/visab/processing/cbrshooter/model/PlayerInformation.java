package org.visab.processing.cbrshooter.model;

import java.util.Vector;

public class PlayerInformation {

    private int health;
    private int magazineAmmunition;
    private String name;
    private String plan;

    private Vector<Float> position;

    private float relativeHealth;

    private PlayerStatistics Statistics;

    private String weapon;

    public int getHealth() {
	return health;
    }

    public int getMagazineAmmunition() {
	return magazineAmmunition;
    }

    public String getName() {
	return name;
    }

    public String getPlan() {
	return plan;
    }

    public Vector<Float> getPosition() {
	return position;
    }

    public float getRelativeHealth() {
	return relativeHealth;
    }

    public PlayerStatistics getStatistics() {
	return Statistics;
    }

    public String getWeapon() {
	return weapon;
    }

    public void setHealth(int health) {
	this.health = health;
    }

    public void setMagazineAmmunition(int magazineAmmunition) {
	this.magazineAmmunition = magazineAmmunition;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setPlan(String plan) {
	this.plan = plan;
    }

    public void setPosition(Vector<Float> position) {
	this.position = position;
    }

    public void setRelativeHealth(float relativeHealth) {
	this.relativeHealth = relativeHealth;
    }

    public void setStatistics(PlayerStatistics statistics) {
	Statistics = statistics;
    }

    public void setWeapon(String weapon) {
	this.weapon = weapon;
    }

}