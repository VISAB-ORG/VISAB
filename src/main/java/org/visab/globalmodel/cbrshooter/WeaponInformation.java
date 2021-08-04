package org.visab.globalmodel.cbrshooter;

/**
 * Contains the information on all weapons eligable for the players within the
 * session.
 */
public class WeaponInformation {

    private String name;
    private int damage;
    private float fireRate;
    private int magazineSize;
    private int maximumAmmunition;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getFireRate() {
        return this.fireRate;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public int getMagazineSize() {
        return this.magazineSize;
    }

    public void setMagazineSize(int magazineSize) {
        this.magazineSize = magazineSize;
    }

    public int getMaximumAmmunition() {
        return this.maximumAmmunition;
    }

    public void setMaximumAmmunition(int maximumAmmunition) {
        this.maximumAmmunition = maximumAmmunition;
    }

}
