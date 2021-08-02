package org.visab.globalmodel.cbrshooter;

import org.visab.globalmodel.Vector2;

public class Player {

    private int health;
    private int magazineAmmunition;
    private int totalAmmunition;
    private String name;
    private String plan;

    private Vector2 position;

    private float relativeHealth;

    private PlayerStatistics statistics;

    private String weapon;

    public int getHealth() {
        return health;
    }

    public int getTotalAmmunition() {
        return totalAmmunition;
    }

    public void setTotalAmmunition(int totalAmmunition) {
        this.totalAmmunition = totalAmmunition;
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

    public Vector2 getPosition() {
        return position;
    }

    public float getRelativeHealth() {
        return relativeHealth;
    }

    public PlayerStatistics getStatistics() {
        return statistics;
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

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setRelativeHealth(float relativeHealth) {
        this.relativeHealth = relativeHealth;
    }

    public void setStatistics(PlayerStatistics statistics) {
        this.statistics = statistics;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

}