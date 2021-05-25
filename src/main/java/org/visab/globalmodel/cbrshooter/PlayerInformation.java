package org.visab.globalmodel.cbrshooter;

public class PlayerInformation {

    private int health;
    private int magazineAmmunition;
    private String name;
    private String plan;

    private Vector2 position;

    private float relativeHealth;

    private PlayerStatistics statistics;

    private String weapon;

    private boolean isCBR;
    private boolean isHumanControlled;

    public int getHealth() {
        return health;
    }

    public boolean getIsCBR() {
        return isCBR;
    }

    public void setIsCBR(boolean isCBR) {
        this.isCBR = isCBR;
    }

    public boolean getIsHumanControlled() {
        return isHumanControlled;
    }

    public void setIsHumanControlled(boolean isHumanControlled) {
        this.isHumanControlled = isHumanControlled;
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