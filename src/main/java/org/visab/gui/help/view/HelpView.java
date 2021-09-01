package org.visab.gui.help.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.dansoftware.pdfdisplayer.PDFDisplayer;
import com.dansoftware.pdfdisplayer.PdfJSVersion;

import org.visab.gui.help.viewmodel.HelpViewModel;

import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;

public class HelpView implements FxmlView<HelpViewModel>, Initializable {

    @FXML
    private Pane showPDF;

    @FXML
    private WebView showVideo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PDFDisplayer displayer = new PDFDisplayer(PdfJSVersion._2_2_228);

        try {
            displayer.loadPDF(new URL(getClass().getResource("/pdf/visab_documentation.pdf").toExternalForm()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        WebView displayerAsPane = (WebView) displayer.toNode();
        displayerAsPane.setPrefWidth(960);
        displayerAsPane.setPrefHeight(630);
        showPDF.getChildren().add(displayerAsPane);
        showVideo.getEngine().load("https://www.youtube.com/embed/znG2ZtcAfqA");
    }
}
