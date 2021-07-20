package org.visab.newgui.visualize.cbrshooter.model;

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
    private HBox playerIconHBox;
    private HBox playerPathHBox;
    private HBox playerPlanChangeHBox;
    private HBox playerDeathHBox;

    public PlayerVisualsRow(String playerName, ImageView playerIcon, ImageView playerPlanChange, ImageView playerDeath,
            Color playerColor) {

        this.playerHBox = new HBox();
        this.playerIconHBox = new HBox();
        this.playerPathHBox = new HBox();
        this.playerPlanChangeHBox = new HBox();
        this.playerDeathHBox = new HBox();

        // Contents for each row
        Label playerNameLabel = new Label(playerName);
        playerNameLabel.setStyle("");
        CheckBox showPlayer = new CheckBox();
        CheckBox showPlayerIcon = new CheckBox();
        CheckBox showPlayerPath = new CheckBox();
        Pane pathPane = new Pane();
        pathPane.setPrefWidth(16);
        pathPane.setPrefHeight(16);
        pathPane.setBackground(new Background(new BackgroundFill(playerColor, CornerRadii.EMPTY, Insets.EMPTY)));
        CheckBox showPlayerPlanChange = new CheckBox();
        CheckBox showPlayerDeath = new CheckBox();

        showPlayer.setSelected(true);
        showPlayerIcon.setSelected(true);
        showPlayerPath.setSelected(true);
        showPlayerPlanChange.setSelected(true);
        showPlayerDeath.setSelected(true);

        this.playerHBox.getChildren().addAll(showPlayer, playerNameLabel);
        this.playerIconHBox.getChildren().addAll(showPlayerIcon, playerIcon);
        this.playerPathHBox.getChildren().addAll(showPlayerPath, pathPane);
        this.playerPlanChangeHBox.getChildren().addAll(showPlayerPlanChange, playerPlanChange);
        this.playerDeathHBox.getChildren().addAll(showPlayerDeath, playerDeath);

    }

    public HBox getPlayerHBox() {
        return playerHBox;
    }

    public HBox getPlayerIconHBox() {
        return playerIconHBox;
    }

    public HBox getPlayerPathHBox() {
        return playerPathHBox;
    }

    public HBox getPlayerPlanChangeHBox() {
        return playerPlanChangeHBox;
    }

    public HBox getPlayerDeathHBox() {
        return playerDeathHBox;
    }

    public CheckBox getShowPlayerCheckBox() {
        return (CheckBox) this.playerHBox.getChildren().get(0);
    }

    public CheckBox getShowPlayerIconCheckBox() {
        return (CheckBox) this.playerIconHBox.getChildren().get(0);
    }

    public CheckBox getShowPlayerPathCheckBox() {
        return (CheckBox) this.playerPathHBox.getChildren().get(0);
    }

    public CheckBox getShowPlayerPlanChangeCheckBox() {
        return (CheckBox) this.playerPlanChangeHBox.getChildren().get(0);
    }

    public CheckBox getShowPlayerDeathCheckBox() {
        return (CheckBox) this.playerDeathHBox.getChildren().get(0);
    }

}