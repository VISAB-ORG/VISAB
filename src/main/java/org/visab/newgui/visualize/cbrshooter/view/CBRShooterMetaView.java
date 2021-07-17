package org.visab.newgui.visualize.cbrshooter.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.globalmodel.cbrshooter.WeaponInformation;
import org.visab.newgui.visualize.cbrshooter.model.PlayerInformation;
import org.visab.newgui.visualize.cbrshooter.viewmodel.CBRShooterMetaViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class CBRShooterMetaView implements FxmlView<CBRShooterMetaViewModel>, Initializable {

    @FXML
    Label rounds;

    @FXML
    Label statisticsPerSecond;

    @FXML
    Label gameSpeed;

    @FXML
    Label ingameTime;

    @FXML
    Label creationDate;

    @FXML
    Label fileFormatVersion;

    @FXML
    Label game;

    @FXML
    TableView<WeaponInformation> weaponInformationTable;

    @FXML
    TableView<PlayerInformation> playerInformationTable;

    @InjectViewModel
    CBRShooterMetaViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rounds.textProperty().bind(viewModel.getRoundsProperty().asString());
        statisticsPerSecond.textProperty().bind(viewModel.getStatisticsPerSecondProperty().asString());
        ingameTime.textProperty().bind(viewModel.getIngameTimeProperty().asString());

        gameSpeed.setText(viewModel.getGameSpeed().toString());
        creationDate.setText(viewModel.getCreationDate().toString());
        fileFormatVersion.setText(viewModel.getFileFormatVersion());
        game.setText(viewModel.getGame());

        weaponInformationTable.setItems(viewModel.getWeaponInformation());
        playerInformationTable.setItems(viewModel.getPlayerInformation());
    }

}
