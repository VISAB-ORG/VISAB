package org.visab.newgui.help.view;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.help.viewmodel.HelpViewModel;
import org.visab.workspace.config.ConfigManager;

import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;

public class HelpView implements FxmlView<HelpViewModel>, Initializable{
	
	@FXML
    private WebView showPDF;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		showPDF.getEngine().load("http://www.google.com");
		
		//String url = getClass().getResource("/pdf/visab_documentation.html").toExternalForm();
		//System.out.println(url);
		
		//File file = new File(url);

		//showPDF.getEngine().load(file.toURI().toString());

		// Would work for our online hosted documentation
        //showPDF.getEngine().load("https://visab-org.github.io/index.html");
		
	

       


	}
}

