package org.visab.newgui.visualize.cbrshooter.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;

/**
 * This class is responsible for providing the visuals for a specific player.
 * 
 * @author leonr
 *
 */
public class PlayerVisuals {

    private Image playerIcon;

    private Image playerDeath;

    private Image playerPlanChange;

    private Color playerColor;

    private Path playerPath;

    public PlayerVisuals(Image playerIcon, Image playerDeath, Image playerPlanChange, Color playerColor) {

        this.playerIcon = playerIcon;
        this.playerDeath = playerDeath;
        this.playerPlanChange = playerPlanChange;
        this.playerColor = playerColor;
        this.playerPath = new Path();
        this.playerPath.setStroke(this.playerColor);
    }

    public Image getPlayerIcon() {
        return playerIcon;
    }

    public void setPlayerIcon(Image playerIcon) {
        this.playerIcon = playerIcon;
    }

    public Image getPlayerDeath() {
        return playerDeath;
    }

    public void setPlayerDeath(Image playerDeath) {
        this.playerDeath = playerDeath;
    }

    public Image getPlayerPlanChange() {
        return playerPlanChange;
    }

    public void setPlayerPlanChange(Image playerPlanChange) {
        this.playerPlanChange = playerPlanChange;
    }

    public Color getPlayerColor() {
        return this.playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    public Path getPlayerPath() {
        return playerPath;
    }

    public void setPlayerPath(Path playerPath) {
        this.playerPath = playerPath;
    }

}
