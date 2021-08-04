package org.visab.newgui.visualize.cbrshooter.model;

import org.visab.globalmodel.Vector2;
import org.visab.globalmodel.cbrshooter.PlayerInformation;

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

    // Visual table
    private BooleanProperty showPlayerProperty = new SimpleBooleanProperty(true);
    private BooleanProperty showIconProperty = new SimpleBooleanProperty(true);
    private BooleanProperty showPathProperty = new SimpleBooleanProperty(true);
    private BooleanProperty showDeathProperty = new SimpleBooleanProperty(true);
    private BooleanProperty showPlanChangeProperty = new SimpleBooleanProperty(true);

    public Player(String name) {
        this.name = name;
    }

    /**
     * Based on a provided PlayerInformation object retrieved from the underlying
     * file data, this replay-view-specific Player object can be updated
     * accordingly.
     * 
     * @param playerInfo the PlayerInformation object from the VISAB file.
     */
    public void updatePlayerData(PlayerInformation playerInfo) {
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

}
