package org.visab.gui.visualize.cbrshooter.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.globalmodel.cbrshooter.WeaponInformation;
import org.visab.gui.UiHelper;
import org.visab.gui.visualize.PlayerInformation;
import org.visab.gui.visualize.cbrshooter.viewmodel.CBRShooterMetaViewModel;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

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
    Label winner;

    @FXML
    ImageView sessionImage;

    @FXML
    ImageView fileImage;

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
        winner.textProperty().bind(viewModel.winnerProperty());

        gameSpeed.setText(viewModel.getGameSpeed().toString());
        creationDate.setText(viewModel.getCreationDate().toString());
        fileFormatVersion.setText(viewModel.getFileFormatVersion());
        game.setText(viewModel.getGame());

        if (Workspace.getInstance().getConfigManager().isDarkModeOn()) {
            sessionImage.setImage(UiHelper.recolorImage(sessionImage.getImage(), Color.WHITE));
            fileImage.setImage(UiHelper.recolorImage(fileImage.getImage(), Color.WHITE));
        }

        weaponInformationTable.setItems(viewModel.getWeaponInformation());
        playerInformationTable.setItems(viewModel.getPlayerInformation());
    }
}
