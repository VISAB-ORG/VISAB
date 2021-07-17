package org.visab.newgui.visualize.cbrshooter.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.globalmodel.cbrshooter.WeaponInformation;
import org.visab.newgui.visualize.cbrshooter.viewmodel.CBRShooterMetaViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
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
    TableColumn<String, String> playerNameColumn;
    
    @FXML
    TableColumn<String, String> playerTypeColumn;

    @FXML
    TableView<WeaponInformation> weaponInformationTable;

    @InjectViewModel
    CBRShooterMetaViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
    }

    
}
