package org.visab.newgui.visualize.cbrshooter.model;

import org.visab.globalmodel.cbrshooter.PlayerInformation;

import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;

/**
 * Simple POJO-style class that is used to fill the table on the replay view
 * with data for the participating players in the CBRShooter.
 * 
 * @author leonr
 *
 */
public class PlayerDataRow {

    private ImageView playerVisual;
    private String name;
    private int health;
    private float relativeHealth;
    private String plan;
    private String position;
    private String weapon;
    private int magazineAmmu;
    private int totalAmmu;
    private int frags;
    private int deaths;
    private CheckBox showCheckBox;

    public PlayerDataRow(PlayerInformation playerInfo) {
        this.name = playerInfo.getName();
        this.health = playerInfo.getHealth();
        this.relativeHealth = playerInfo.getRelativeHealth();
        this.plan = playerInfo.getPlan();
        this.position = playerInfo.getPosition().getX() + ", " + playerInfo.getPosition().getY();
        this.weapon = playerInfo.getWeapon();
        this.magazineAmmu = playerInfo.getMagazineAmmunition();
        this.totalAmmu = playerInfo.getTotalAmmunition();
        this.frags = playerInfo.getStatistics().getFrags();
        this.deaths = playerInfo.getStatistics().getDeaths();
        CheckBox showCheck = new CheckBox();
        showCheck.setSelected(true);
        this.showCheckBox = showCheck;
    }

    public ImageView getPlayerVisual() {
        return playerVisual;
    }

    public void setPlayerVisual(ImageView playerVisual) {
        this.playerVisual = playerVisual;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getRelativeHealth() {
        return relativeHealth;
    }

    public void setRelativeHealth(float relativeHealth) {
        this.relativeHealth = relativeHealth;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public int getMagazineAmmu() {
        return magazineAmmu;
    }

    public void setMagazineAmmu(int magazineAmmu) {
        this.magazineAmmu = magazineAmmu;
    }

    public int getTotalAmmu() {
        return totalAmmu;
    }

    public void setTotalAmmu(int totalAmmu) {
        this.totalAmmu = totalAmmu;
    }

    public int getFrags() {
        return frags;
    }

    public void setFrags(int frags) {
        this.frags = frags;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public CheckBox getShowCheckBox() {
        return showCheckBox;
    }

    public void setShowCheckBox(CheckBox showCheckBox) {
        this.showCheckBox = showCheckBox;
    }

}