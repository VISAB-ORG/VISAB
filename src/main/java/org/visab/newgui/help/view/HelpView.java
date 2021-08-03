package org.visab.newgui.help.view;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import com.dansoftware.pdfdisplayer.PDFDisplayer;
import com.dansoftware.pdfdisplayer.PdfJSVersion;

import org.visab.newgui.AppMain;
import org.visab.newgui.help.viewmodel.HelpViewModel;
import org.visab.workspace.config.ConfigManager;

import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

public class HelpView implements FxmlView<HelpViewModel>, Initializable {

    @FXML
    private WebView showPDF;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PDFDisplayer displayer = new PDFDisplayer(PdfJSVersion._2_2_228);
        AppMain.getPrimaryStage().setScene(new Scene(displayer.toNode()));
        AppMain.getPrimaryStage().show();
        try {
            displayer.loadPDF(new URL(
                    "file:///C:/Users/moritz/Desktop/Visualisierung%20Dokumentation/Literatur/visab_documentation.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // TODO Auto-generated method stub
        showPDF.getEngine().load("http://www.google.com");

        // String url =
        // getClass().getResource("/pdf/visab_documentation.html").toExternalForm();
        // System.out.println(url);

        // File file = new File(url);

        // showPDF.getEngine().load(file.toURI().toString());

        // Would work for our online hosted documentation
        // showPDF.getEngine().load("https://visab-org.github.io/index.html");

    }
}
