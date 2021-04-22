package application;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class AboutWindowController {

	@FXML
	private MenuItem browseFileMenu;
	@FXML
	private MenuItem pathViewerMenu;
	@FXML
	private MenuItem statisticsMenu;
	@FXML
	private MenuItem helpMenu;
	@FXML
	private MenuItem aboutMenu;

	public GUIMain main;

	public void setMain(GUIMain main) {
		this.main = main;
	}

	@FXML
	public void handleBrowseFileMenu() {
		main.mainWindow();
	}

	@FXML
	public void handlePathViewerMenu() {
		main.pathViewerWindow();
	}

	@FXML
	public void handleStatisticsMenu() {
		main.statisticsWindow();
	}

	@FXML
	public void handleHelpMenu() {
		main.helpWindow();
	}

	@FXML
	public void handleAboutMenu() {
		// DO NOTHING
	}

}
