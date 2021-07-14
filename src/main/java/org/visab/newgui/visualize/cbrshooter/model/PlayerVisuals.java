package org.visab.newgui.visualize.cbrshooter.model;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * This class is responsible for providing the visuals for a specific player.
 * 
 * @author leonr
 *
 */
public class PlayerVisuals {

    private ImageView playerIcon;

    private ImageView playerDeath;

    private ImageView playerPlanChange;

    private Color playerColor;

    public PlayerVisuals(ImageView playerIcon, ImageView playerDeath, ImageView playerPlanChange, Color playerColor) {
        this.playerIcon = playerIcon;
        this.playerDeath = playerDeath;
        this.playerPlanChange = playerPlanChange;
        this.playerColor = playerColor;

        // Default is false because they will only be shown if necessary
        this.playerDeath.setVisible(false);
        this.playerPlanChange.setVisible(false);
    }

    public ImageView getPlayerIcon() {
        return playerIcon;
    }

    public void setPlayerIcon(ImageView playerIcon) {
        this.playerIcon = playerIcon;
    }

    public ImageView getPlayerDeath() {
        return playerDeath;
    }

    public void setPlayerDeath(ImageView playerDeath) {
        this.playerDeath = playerDeath;
    }

    public ImageView getPlayerPlanChange() {
        return playerPlanChange;
    }

    public void setPlayerPlanChange(ImageView playerPlanChange) {
        this.playerPlanChange = playerPlanChange;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

}
