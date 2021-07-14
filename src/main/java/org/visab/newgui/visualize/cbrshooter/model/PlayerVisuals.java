package org.visab.newgui.visualize.cbrshooter.model;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;

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

    private Path playerPath;

    public PlayerVisuals(ImageView playerIcon, ImageView playerDeath, ImageView playerPlanChange, Color playerColor) {
        this.playerIcon = playerIcon;
        this.playerDeath = playerDeath;
        this.playerPlanChange = playerPlanChange;
        this.playerPath = new Path();
        this.playerPath.setStroke(playerColor);

        // Default is false because they will only be shown if necessary
        this.playerDeath.setVisible(false);
        this.playerPlanChange.setVisible(false);
    }

    public void hide() {
        this.playerIcon.setVisible(false);
        this.playerDeath.setVisible(false);
        this.playerPlanChange.setVisible(false);
        this.playerPath.setVisible(false);
    }

    public void show() {
        this.playerIcon.setVisible(true);
        this.playerDeath.setVisible(true);
        this.playerPlanChange.setVisible(true);
        this.playerPath.setVisible(true);
    }

    public boolean isVisible() {
        if (this.playerIcon.isVisible() && this.playerPath.isVisible()) {
            return true;
        } else {
            return false;
        }
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
        return (Color) playerPath.getStroke();
    }

    public void setPlayerColor(Color playerColor) {
        this.playerPath.setStroke(playerColor);
    }

    public Path getPlayerPath() {
        return playerPath;
    }

    public void setPlayerPath(Path playerPath) {
        this.playerPath = playerPath;
    }

}
