package org.visab.gui;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

/**
 * Controller for the AboutWindow.
 *
 * @author VISAB 1.0 group
 *
 */
public class AboutWindowController {

	@FXML
	private MenuItem aboutMenu;
	@FXML
	private MenuItem browseFileMenu;
	@FXML
	private MenuItem helpMenu;
	public GUIMain main;
	@FXML
	private MenuItem pathViewerMenu;

	@FXML
	private MenuItem statisticsMenu;

	@FXML
	public void handleAboutMenu() {
		// DO NOTHING
	}

	@FXML
	public void handleBrowseFileMenu() {
		main.mainWindow();
	}

	@FXML
	public void handleHelpMenu() {
		main.helpWindow();
	}

	@FXML
	public void handlePathViewerMenu() {
		main.pathViewerWindow();
	}

	@FXML
	public void handleStatisticsMenu() {
		main.statisticsWindow();
	}

	public void setMain(GUIMain main) {
		this.main = main;
	}

}
