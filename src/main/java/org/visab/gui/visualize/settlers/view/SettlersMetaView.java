package org.visab.gui.visualize.settlers.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.gui.visualize.PlayerInformation;
import org.visab.gui.visualize.settlers.viewmodel.SettlersMetaViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class SettlersMetaView implements FxmlView<SettlersMetaViewModel>, Initializable {

    @FXML
    Label rounds;

    @FXML
    Label creationDate;

    @FXML
    Label fileFormatVersion;

    @FXML
    Label game;

    @FXML
    Label winner;

    @FXML
    TableView<PlayerInformation> playerInformationTable;

    @InjectViewModel
    SettlersMetaViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rounds.textProperty().bind(viewModel.getRoundsProperty().asString());
        winner.textProperty().bind(viewModel.winnerProperty());

        creationDate.setText(viewModel.getCreationDate().toString());
        fileFormatVersion.setText(viewModel.getFileFormatVersion());
        game.setText(viewModel.getGame());

        playerInformationTable.setItems(viewModel.getPlayerInformation());
    }
}
