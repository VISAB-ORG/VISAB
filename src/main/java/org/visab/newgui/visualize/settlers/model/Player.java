package org.visab.newgui.visualize.settlers.model;

import org.visab.globalmodel.Vector2;
import org.visab.globalmodel.settlers.PlayerResources;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
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
    private IntegerProperty villageCountPropery = new SimpleIntegerProperty();
    private ListProperty<Vector2> villagePositionsProperty = new SimpleListProperty<Vector2>();
    private IntegerProperty streetCountPropery = new SimpleIntegerProperty();
    private ListProperty<Vector2> streetPositionsProperty = new SimpleListProperty<Vector2>();
    private IntegerProperty cityCountProperty = new SimpleIntegerProperty();
    private ListProperty<Vector2> cityPositionsProperty = new SimpleListProperty<Vector2>();
    private ObjectProperty<PlayerResources> resourcesProperty = new SimpleObjectProperty<PlayerResources>();
    private BooleanProperty hasLongestRoadProperty = new SimpleBooleanProperty();
    private IntegerProperty longestRoadProperty = new SimpleIntegerProperty();
    private ListProperty<String> planActionsProperty = new SimpleListProperty<String>();
    private IntegerProperty victoryPointsProperty = new SimpleIntegerProperty();
    private ObjectProperty<PlayerResources> resourcesGainedProperty = new SimpleObjectProperty<PlayerResources>();
    private BooleanProperty isMyTurnProperty = new SimpleBooleanProperty();
    private ObjectProperty<PlayerResources> villageResourcesGainedProperty = new SimpleObjectProperty<PlayerResources>();
    private ObjectProperty<Color> playerColorProperty = new SimpleObjectProperty<>();

    // Visual table
    private BooleanProperty showPlayerProperty = new SimpleBooleanProperty(true);
    private BooleanProperty showRoadsPropery = new SimpleBooleanProperty(true);
    private BooleanProperty showPlanChangeProperty = new SimpleBooleanProperty(true);

    public Player(String name) {
        this.name = name;
    }

    public void initializeVisuals(Color playerColor) {
        this.playerColorProperty.set(playerColor);
    }

    /**
     * Based on a provided PlayerInformation object retrieved from the underlying
     * file data, this replay-view-specific Player object can be updated
     * accordingly.
     * 
     * @param playerInfo the Player object from the VISAB file.
     */
    public void updatePlayerData(org.visab.globalmodel.settlers.Player playerInfo) {

        villageCountPropery.set(playerInfo.getVillageCount());
        villagePositionsProperty.set((ObservableList<Vector2>) playerInfo.getVillagePositions());
        streetCountPropery.set(playerInfo.getStreetCount());
        streetPositionsProperty.set((ObservableList<Vector2>) playerInfo.getStreetPositions());
        cityCountProperty.set(playerInfo.getCityCount());
        cityPositionsProperty.set((ObservableList<Vector2>) playerInfo.getCityPositions());
        resourcesProperty.set(playerInfo.getResources());
        hasLongestRoadProperty.set(playerInfo.isHasLongestRoad());
        longestRoadProperty.set(playerInfo.getLongestRoad());
        planActionsProperty.set((ObservableList<String>) playerInfo.getPlanActions());
        victoryPointsProperty.set(playerInfo.getVictoryPoints());
        resourcesGainedProperty.set(playerInfo.getResourcesGained());
        isMyTurnProperty.set(playerInfo.isMyTurn());
        villageResourcesGainedProperty.set(playerInfo.getVillageResourcesGained());
    }

    public String getName() {
        return name;
    }

    public IntegerProperty villageCountPropery() {
        return villageCountPropery;
    }

    public ListProperty<Vector2> villagePositionsProperty() {
        return villagePositionsProperty;
    }

    public IntegerProperty streetCountPropery() {
        return streetCountPropery;
    }

    public ListProperty<Vector2> streetPositionsProperty() {
        return streetPositionsProperty;
    }

    public IntegerProperty cityCountProperty() {
        return cityCountProperty;
    }

    public ListProperty<Vector2> cityPositionsProperty() {
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

    public ListProperty<String> planActionsProperty() {
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

    public ObjectProperty<PlayerResources> villageResourcesGainedProperty() {
        return villageResourcesGainedProperty;
    }

    public ObjectProperty<Color> playerColorProperty() {
        return playerColorProperty;
    }

    public BooleanProperty showPlayerProperty() {
        return showPlayerProperty;
    }

    public BooleanProperty showRoadsPropery() {
        return showRoadsPropery;
    }

    public BooleanProperty showPlanChangeProperty() {
        return showPlanChangeProperty;
    }

}
