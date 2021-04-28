package org.visab.gui;

import java.io.File;
import java.net.URISyntaxException;

import org.visab.util.Settings;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * Controller for the HelpWindow.
 *
 * @author VISAB 1.0 group
 *
 */
public class HelpWindowController extends Application {

    @FXML
    private MenuItem aboutMenu;
    @FXML
    private MenuItem browseFileMenu;
    @FXML
    private MenuItem helpMenu;
    @FXML
    private Button loadButton;
    public GUIMain main;
    @FXML
    private MenuItem pathViewerMenu;

    @FXML
    private MenuItem statisticsMenu;

    @FXML
    public void handleAboutMenu() {
	main.aboutWindow();
    }

    @FXML
    public void handleBrowseFileMenu() {
	main.mainWindow();
    }

    @FXML
    public void handleHelpMenu() {
	// DO NOTHING
    }

    @FXML
    public void handleLoadButton() {
	File file = null;
	try {
	    file = new File(GUIMain.class.getResource(Settings.VISAB_DOC_PATH).toURI());
	} catch (URISyntaxException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	HostServices hostServices = getHostServices();
	hostServices.showDocument(file.getAbsolutePath());
    }

    @FXML
    public void handlePathViewerMenu() {
	main.pathViewerWindow();
    }

    @FXML
    public void handleStatisticsMenu() throws URISyntaxException {
	main.statisticsWindow();
    }

    public void setMain(GUIMain main) {
	this.main = main;
    }

    @Override
    public void start(Stage arg0) throws Exception {
	// DO NOTHING
    }

}