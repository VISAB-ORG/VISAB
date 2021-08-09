package org.visab.newgui.visualize.settlers.model;

import java.util.List;

import org.visab.globalmodel.Vector2;
import org.visab.globalmodel.settlers.PlayerResources;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * 
 * 
 *
 */
public class Player {

    // Statistics table
    private String name;

    // Table + Replay view
    private IntegerProperty villageCountProperty = new SimpleIntegerProperty();
    private ObjectProperty<List<Vector2>> villagePositionsProperty = new SimpleObjectProperty<List<Vector2>>();
    private IntegerProperty streetCountProperty = new SimpleIntegerProperty();
    private ObjectProperty<List<Vector2>> streetPositionsProperty = new SimpleObjectProperty<List<Vector2>>();
    private IntegerProperty cityCountProperty = new SimpleIntegerProperty();
    private ObjectProperty<List<Vector2>> cityPositionsProperty = new SimpleObjectProperty<List<Vector2>>();
    private ObjectProperty<PlayerResources> resourcesProperty = new SimpleObjectProperty<PlayerResources>();
    private BooleanProperty hasLongestRoadProperty = new SimpleBooleanProperty();
    private IntegerProperty longestRoadProperty = new SimpleIntegerProperty();
    private ObjectProperty<List<String>> planActionsProperty = new SimpleObjectProperty<List<String>>();
    private IntegerProperty victoryPointsProperty = new SimpleIntegerProperty();
    private ObjectProperty<PlayerResources> resourcesGainedProperty = new SimpleObjectProperty<PlayerResources>();
    private BooleanProperty isMyTurnProperty = new SimpleBooleanProperty();
    private ObjectProperty<Color> playerColorProperty = new SimpleObjectProperty<>();

    // Visual table
    private BooleanProperty showPlayerProperty = new SimpleBooleanProperty(true);
    private BooleanProperty showRoadProperty = new SimpleBooleanProperty(true);
    private BooleanProperty showVillagesProperty = new SimpleBooleanProperty(true);
    private BooleanProperty showCitiesProperty = new SimpleBooleanProperty(true);
    private BooleanProperty showPlanChangeProperty = new SimpleBooleanProperty(true);

    private Image playerVillage;
    private Image playerCity;
    private Image playerRoad;

    public Player(String name) {
        this.name = name;
    }

    public void initializeVisuals(Color playerColor, Image playerRoad, Image playerVillage, Image playerCity) {
        this.playerColorProperty.set(playerColor);
        this.playerRoad = playerRoad;
        this.playerVillage = playerVillage;
        this.playerCity = playerCity;
    }

    /**
     * Based on a provided PlayerInformation object retrieved from the underlying
     * file data, this replay-view-specific Player object can be updated
     * accordingly.
     * 
     * @param playerInfo the Player object from the VISAB file.
     */
    public void updatePlayerData(org.visab.globalmodel.settlers.Player playerInfo) {

        villageCountProperty.set(playerInfo.getVillageCount());
        villagePositionsProperty.set(playerInfo.getVillagePositions());
        streetCountProperty.set(playerInfo.getStreetCount());
        streetPositionsProperty.set(playerInfo.getStreetPositions());
        cityCountProperty.set(playerInfo.getCityCount());
        cityPositionsProperty.set(playerInfo.getCityPositions());
        resourcesProperty.set(playerInfo.getResources());
        hasLongestRoadProperty.set(playerInfo.isHasLongestRoad());
        longestRoadProperty.set(playerInfo.getLongestRoad());
        planActionsProperty.set(playerInfo.getPlanActions());
        victoryPointsProperty.set(playerInfo.getVictoryPoints());
        resourcesGainedProperty.set(playerInfo.getResourcesGained());
        isMyTurnProperty.set(playerInfo.isMyTurn());
    }

    public String getName() {
        return name;
    }

    public IntegerProperty villageCountProperty() {
        return villageCountProperty;
    }

    public ObjectProperty<List<Vector2>> villagePositionsProperty() {
        return villagePositionsProperty;
    }

    public IntegerProperty streetCountProperty() {
        return streetCountProperty;
    }

    public ObjectProperty<List<Vector2>> streetPositionsProperty() {
        return streetPositionsProperty;
    }

    public BooleanProperty showVillagesProperty() {
        return showVillagesProperty;
    }

    public BooleanProperty showCitiesProperty() {
        return showCitiesProperty;
    }

    public IntegerProperty cityCountProperty() {
        return cityCountProperty;
    }

    public ObjectProperty<List<Vector2>> cityPositionsProperty() {
        return cityPositionsProperty;
    }

    public ObjectProperty<PlayerResources> resourcesProperty() {
        return resourcesProperty;
    }

    public BooleanProperty hasLongestRoadProperty() {
        return hasLongestRoadProperty;
    }

    public IntegerProperty longestRoadProperty() {
        return longestRoadProperty;
    }

    public ObjectProperty<List<String>> planActionsProperty() {
        return planActionsProperty;
    }

    public IntegerProperty victoryPointsProperty() {
        return victoryPointsProperty;
    }

    public ObjectProperty<PlayerResources> resourcesGainedProperty() {
        return resourcesGainedProperty;
    }

    public BooleanProperty isMyTurnProperty() {
        return isMyTurnProperty;
    }

    public ObjectProperty<Color> playerColorProperty() {
        return playerColorProperty;
    }

    public BooleanProperty showPlayerProperty() {
        return showPlayerProperty;
    }

    public BooleanProperty showRoadProperty() {
        return showRoadProperty;
    }

    public BooleanProperty showPlanChangeProperty() {
        return showPlanChangeProperty;
    }

    public Image getPlayerVillage() {
        return playerVillage;
    }

    public Image getPlayerCity() {
        return playerCity;
    }

    public Image getPlayerRoad() {
        return playerRoad;
    }

}
