package application;

import java.io.File;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class HelpWindowController extends Application {

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
	@FXML
	private Button loadButton;

	public GUIMain main;

	public void setMain(GUIMain main) {
		this.main = main;
	}

	@FXML
	public void handleLoadButton() {
		File file = new File("@../../pdf/visab_documentation.pdf");
		HostServices hostServices = getHostServices();
		hostServices.showDocument(file.getAbsolutePath());
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
		// DO NOTHING
	}

	@FXML
	public void handleAboutMenu() {
		main.aboutWindow();
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// DO NOTHING
	}

}
