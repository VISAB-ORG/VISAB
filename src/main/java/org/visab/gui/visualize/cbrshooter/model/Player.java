package org.visab.gui.visualize.cbrshooter.model;

import java.util.List;

import org.visab.globalmodel.Vector2;
import org.visab.gui.visualize.CoordinateHelper;

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
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * 
 * 
 *
 */
public class Player {

    // Statistics table
    private String name;

    // Table + Replay view
    private IntegerProperty healthProperty = new SimpleIntegerProperty();
    private FloatProperty relativeHealthProperty = new SimpleFloatProperty();
    private StringProperty planProperty = new SimpleStringProperty();
    private ObjectProperty<Vector2<Double>> positionProperty = new SimpleObjectProperty<>();
    private StringProperty weaponProperty = new SimpleStringProperty();
    private IntegerProperty magazineAmmuProperty = new SimpleIntegerProperty();
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
    private Path playerPath;

    public Player(String name) {
        this.name = name;
    }

    public void initializeVisuals(Color playerColor, Image playerIcon, Image playerPlanChange, Image playerDeath,
            Path playerPath) {
        this.playerIcon = playerIcon;
        this.playerPlanChange = playerPlanChange;
        this.playerDeath = playerDeath;
        this.playerColorProperty.set(playerColor);
        this.playerPath = playerPath;
        this.playerPath.setStroke(playerColor);
        this.playerPath.setStrokeWidth(2);
        this.playerPath.setVisible(showPathProperty.get());
    }

    /**
     * Based on a provided PlayerInformation object retrieved from the underlying
     * file data, this replay-view-specific Player object can be updated
     * accordingly.
     * 
     * @param playerInfo the Player object from the VISAB file.
     */
    public void updatePlayerData(org.visab.globalmodel.cbrshooter.Player playerInfo) {
        // Update the values of the fields
        this.healthProperty.set(playerInfo.getHealth());
        this.relativeHealthProperty.set(playerInfo.getRelativeHealth());
        this.planProperty.set(playerInfo.getPlan());
        this.positionProperty.set(playerInfo.getPosition());
        this.weaponProperty.set(playerInfo.getWeapon());
        this.magazineAmmuProperty.set(playerInfo.getMagazineAmmunition());
        this.totalAmmuProperty.set(playerInfo.getTotalAmmunition());
        this.fragsProperty.set(playerInfo.getStatistics().getFrags());
        this.deathsProperty.set(playerInfo.getStatistics().getDeaths());
    }

    public void updatePlayerCoordinates(CoordinateHelper coordinateHelper) {
        var translated = coordinateHelper.translateAccordingToMap(positionProperty.get(), false);
        var coordX = translated.getX();
        var coordY = translated.getY();

        if (this.playerPath.getElements().size() == 0) {
            this.playerPath.getElements().add(new MoveTo(coordX, coordY));
        } else {
            this.playerPath.getElements().add(new LineTo(coordX, coordY));
        }
        this.playerPath.setVisible(this.showPathProperty.get());
    }

    public void resetPath() {
        this.playerPath.getElements().clear();
    }

    public void redrawPath(List<Vector2<Double>> positionList, CoordinateHelper coordinateHelper) {
        this.playerPath.getElements().clear();

        if (positionList.size() > 0) {
            var coordXMoveTo = coordinateHelper.translateAccordingToMap(positionList.get(0), false).getX();
            var coordYMoveTo = coordinateHelper.translateAccordingToMap(positionList.get(0), false).getY();
            this.playerPath.getElements().add(new MoveTo(coordXMoveTo, coordYMoveTo));

            for (int i = 1; i < positionList.size(); i++) {
                var coordXLineTo = coordinateHelper.translateAccordingToMap(positionList.get(i), false).getX();
                var coordYLineTo = coordinateHelper.translateAccordingToMap(positionList.get(i), false).getY();
                this.playerPath.getElements().add(new LineTo(coordXLineTo, coordYLineTo));
            }
        }
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

    public ObjectProperty<Vector2<Double>> positionProperty() {
        return positionProperty;
    }

    public StringProperty weaponProperty() {
        return weaponProperty;
    }

    public IntegerProperty magazineAmmuProperty() {
        return magazineAmmuProperty;
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

    public Path getPlayerPath() {
        return playerPath;
    }

}
