package org.visab.newgui.visualize.cbrshooter.model;

import org.visab.globalmodel.Vector2;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Player {

    // Statistics table
    private String name;

    // Table + Replay view
    private IntegerProperty healthProperty = new SimpleIntegerProperty();
    private FloatProperty relativeHealthProperty = new SimpleFloatProperty();
    private StringProperty planProperty = new SimpleStringProperty();
    private ObjectProperty<Vector2> positionProperty = new SimpleObjectProperty<>();
    private StringProperty weaponProperty = new SimpleStringProperty();
    private IntegerProperty magaizeAmmuProperty = new SimpleIntegerProperty();
    private IntegerProperty totalAmmuProperty = new SimpleIntegerProperty();
    private IntegerProperty fragsProperty = new SimpleIntegerProperty();
    private IntegerProperty deathsProperty = new SimpleIntegerProperty();

    private ObjectProperty<Color> playerColorProperty = new SimpleObjectProperty<>();

    // Visual table
    private BooleanProperty showPlayerProperty = new SimpleBooleanProperty(true);
    private BooleanProperty showIconProperty = new SimpleBooleanProperty(true);
    private BooleanProperty showPathProperty = new SimpleBooleanProperty(true);
    private BooleanProperty showDeathProperty = new SimpleBooleanProperty(true);
    private BooleanProperty showPlanChangeProperty = new SimpleBooleanProperty(true);

    private Image playerIcon;
    private Image playerPlanChange;
    private Image playerDeath;

    public Player(String name, Color playerColor, Image playerIcon, Image playerPlanChange, Image playerDeath) {
        this.name = name;
        this.playerIcon = playerIcon;
        this.playerPlanChange = playerPlanChange;
        this.playerDeath = playerDeath;
        this.playerColorProperty.set(playerColor);
    }

    /**
     * Based on a provided PlayerInformation object retrieved from the underlying
     * file data, this replay-view-specific Player object can be updated
     * accordingly.
     * 
     * @param playerInfo the PlayerInformation object from the VISAB file.
     */
    public void updatePlayerData(org.visab.globalmodel.cbrshooter.Player playerInfo) {
        // Update the values of the fields
        this.healthProperty.set(playerInfo.getHealth());
        this.relativeHealthProperty.set(playerInfo.getRelativeHealth());
        this.planProperty.set(playerInfo.getPlan());
        this.positionProperty.set(playerInfo.getPosition());
        this.weaponProperty.set(playerInfo.getWeapon());
        this.magaizeAmmuProperty.set(playerInfo.getMagazineAmmunition());
        this.totalAmmuProperty.set(playerInfo.getTotalAmmunition());
        this.fragsProperty.set(playerInfo.getStatistics().getFrags());
        this.deathsProperty.set(playerInfo.getStatistics().getDeaths());
    }

    public BooleanProperty showPlayerProperty() {
        return showPlayerProperty;
    }

    public BooleanProperty showIconProperty() {
        return showIconProperty;
    }

    public BooleanProperty showPathProperty() {
        return showPathProperty;
    }

    public BooleanProperty showDeathProperty() {
        return showDeathProperty;
    }

    public BooleanProperty showPlanChangeProperty() {
        return showPlanChangeProperty;
    }

    public String getName() {
        return name;
    }

    public IntegerProperty healthProperty() {
        return healthProperty;
    }

    public FloatProperty relativeHealthProperty() {
        return relativeHealthProperty;
    }

    public StringProperty planProperty() {
        return planProperty;
    }

    public ObjectProperty<Vector2> positionProperty() {
        return positionProperty;
    }

    public StringProperty weaponProperty() {
        return weaponProperty;
    }

    public IntegerProperty magaizeAmmuProperty() {
        return magaizeAmmuProperty;
    }

    public IntegerProperty totalAmmuProperty() {
        return totalAmmuProperty;
    }

    public IntegerProperty fragsProperty() {
        return fragsProperty;
    }

    public IntegerProperty deathsProperty() {
        return deathsProperty;
    }

    public ObjectProperty<Color> playerColorProperty() {
        return playerColorProperty;
    }

    public Image getPlayerIcon() {
        return playerIcon;
    }

    public Image getPlayerPlanChange() {
        return playerPlanChange;
    }

    public Image getPlayerDeath() {
        return playerDeath;
    }
}