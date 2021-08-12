package org.visab.gui.visualize.settlers.model;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PlayerVisualsRow {

    private HBox playerHBox;
    private HBox playerRoadHBox;
    private HBox playerVillagesHBox;
    private HBox playerCitiesHBox;

    public PlayerVisualsRow(String playerName, ImageView playerRoad, ImageView playerVillage, ImageView playerCity,
            Color playerColor) {

        this.playerHBox = new HBox();
        this.playerRoadHBox = new HBox();
        this.playerVillagesHBox = new HBox();
        this.playerCitiesHBox = new HBox();

        // Contents for each row
        Label playerNameLabel = new Label(playerName);
        playerNameLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
        CheckBox showPlayer = new CheckBox();
        CheckBox showPlayerRoad = new CheckBox();
        CheckBox showPlayerVillages = new CheckBox();
        CheckBox showPlayerCities = new CheckBox();
        Pane colorPane = new Pane();
        colorPane.setPrefWidth(16);
        colorPane.setPrefHeight(16);
        colorPane.setBackground(new Background(new BackgroundFill(playerColor, CornerRadii.EMPTY, Insets.EMPTY)));

        showPlayer.setSelected(true);
        showPlayerRoad.setSelected(true);
        showPlayerVillages.setSelected(true);
        showPlayerCities.setSelected(true);

        this.playerHBox.getChildren().addAll(showPlayer, colorPane, playerNameLabel);
        this.playerRoadHBox.getChildren().addAll(showPlayerRoad, playerRoad);
        this.playerVillagesHBox.getChildren().addAll(showPlayerVillages, playerVillage);
        this.playerCitiesHBox.getChildren().addAll(showPlayerCities, playerCity);
    }

    public HBox getPlayerHBox() {
        return playerHBox;
    }

    public HBox getPlayerRoadHBox() {
        return playerRoadHBox;
    }

    public HBox getPlayerVillagesHBox() {
        return playerVillagesHBox;
    }

    public HBox getPlayerCitiesHBox() {
        return playerCitiesHBox;
    }

    public CheckBox getShowPlayerCheckBox() {
        return (CheckBox) this.playerHBox.getChildren().get(0);
    }

    public CheckBox getShowPlayerRoadCheckBox() {
        return (CheckBox) this.playerRoadHBox.getChildren().get(0);
    }

    public CheckBox getShowPlayerVillagesCheckBox() {
        return (CheckBox) this.playerVillagesHBox.getChildren().get(0);
    }

    public CheckBox getShowPlayerCitiesCheckBox() {
        return (CheckBox) this.playerCitiesHBox.getChildren().get(0);
    }

}