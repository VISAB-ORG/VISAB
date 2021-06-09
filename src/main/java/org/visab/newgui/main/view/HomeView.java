package org.visab.newgui.main.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.main.viewmodel.HomeViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.MenuBar;

import javafx.scene.control.ListView;

import javafx.scene.control.Menu;

public class HomeView implements FxmlView<HomeViewModel>, Initializable {
	
    @FXML
	private MenuBar menuBar;
	@FXML
	private Menu homeMenu;
	@FXML
	private Menu homeMenu1;
	@FXML
	private Menu apiMenu;
	@FXML
	private Menu apiMenu1;
	@FXML
	private Menu settingsMenu;
	@FXML
	private Menu aboutMenu;
	@FXML
	private Menu aboutMenu1;
	@FXML
	private Menu helpMenu;
	@FXML
	private Menu helpMenu1;
	@FXML
	private ListView workspaceList;
	@FXML
	private Button uploadButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button visualizeButton;
	
    @InjectViewModel
    private HomeViewModel viewModel; 
    
	@FXML
    public void openApi() {
        viewModel.openApi().execute();
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	    
	
		
	}

}
