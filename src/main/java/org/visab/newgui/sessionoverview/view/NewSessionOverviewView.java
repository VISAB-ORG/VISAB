package org.visab.newgui.sessionoverview.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.sessionoverview.viewmodel.NewSessionOverviewViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;


public class NewSessionOverviewView implements FxmlView<NewSessionOverviewViewModel>, Initializable {

    @FXML
    private Button closeSessionButton;

    @InjectViewModel
    private NewSessionOverviewViewModel viewModel;

    @FXML
    public void closeSessionAction() {
        viewModel.closeSessionCommand().execute();
    }

    @FXML
    public void openLiveViewAction() {
        viewModel.openLiveViewCommand().execute();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}


}
