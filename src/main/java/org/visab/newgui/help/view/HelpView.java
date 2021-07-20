package org.visab.newgui.help.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.help.viewmodel.HelpViewModel;

import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/*
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
*/
public class HelpView implements FxmlView<HelpViewModel>, Initializable{
	
	@FXML
    //private WebView showPDF;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		//showPDF.getEngine().load("http://google.com");
	}

}
