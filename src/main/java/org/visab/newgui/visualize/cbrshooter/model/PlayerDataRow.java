package org.visab.newgui.visualize.cbrshooter.model;

import org.visab.globalmodel.cbrshooter.PlayerInformation;

/**
 * Simple POJO-style class that is used to fill the table on the replay view
 * with data for the participating players in the CBRShooter.
 * 
 * @author leonr
 *
 */
public class PlayerDataRow {

    private PlayerInformation playerInfo;

    public PlayerDataRow(PlayerInformation playerInfo) {
        this.playerInfo = playerInfo;
    }

    public String getPlayerName() {
        return this.playerInfo.getName();
    }

    public String getPlayerHealth() {
        return String.valueOf(this.playerInfo.getHealth());
    }

    public String getPlayerMagazineAmmunition() {
        return String.valueOf(this.playerInfo.getMagazineAmmunition());
    }

    public String getPlayerPlan() {
        return this.playerInfo.getPlan();
    }

    public String getPlayerPosition() {
        return this.playerInfo.getPosition().toString();
    }

    public String getPlayerRelativeHealth() {
        return String.valueOf(this.playerInfo.getRelativeHealth());
    }

    public String getPlayerTotalAmmunition() {
        return String.valueOf(this.playerInfo.getTotalAmmunition());
    }

    public String getPlayerWeapon() {
        return this.playerInfo.getWeapon();
    }

}
