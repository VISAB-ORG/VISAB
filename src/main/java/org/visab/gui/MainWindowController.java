package org.visab.gui;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.visab.util.VISABUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

/**
 * The Controlle for the MainWindow that is shown upon startup.
 *
 * @author VISAB 1.0 group
 *
 */
public class MainWindowController {

    // Views
    @FXML
    private Button browseFile;

    private File file;

    public GUIMain main;
    @FXML
    private Button pathViewerButton;
    @FXML
    private Button statisticsViewButton;

    @FXML
    private Label warningMessage;

    public File getFile() {
	return file;
    }

    @FXML
    public void handleBrowseFile() throws URISyntaxException {

	warningMessage.setText("");

	FileChooser fileChooser = new FileChooser();
	fileChooser.setTitle("Open Resource File");

	file = fileChooser.showOpenDialog(null);

	// If file is selected
	if (file != null) {

	    // Get Current Filename
	    Path currentFileName = Paths.get("", file.getName());

	    String loadedFilePath = file.getAbsolutePath();
	    String content = VISABUtil.readFile(loadedFilePath.toString());

	    boolean externalFileAccepted = false;
	    boolean visabFileAccepted = false;

	    if (currentFileName.toString().endsWith(".visab")) {
		// show success message & write to Database
		VISABUtil.writeFileToDatabase(currentFileName.toString(), content);

		visabFileAccepted = true;

	    } else {
		for (int i = 0; i < VISABUtil.getAcceptedExternalDataEndings().length; i++) {
		    if (currentFileName.toString().endsWith(VISABUtil.getAcceptedExternalDataEndings()[i])) {
			externalFileAccepted = true;
		    }
		}
	    }
	    if (externalFileAccepted) {
		VISABUtil.writeFileToDatabase(currentFileName.toString(), content);
		warningMessage.setText("The file is not a visab-file!\nTherefore PathViewer won't be available, "
			+ file.getName() + " was saved anyway.");
		warningMessage.setStyle("-fx-text-fill: orange;");
	    } else {
		warningMessage.setText("This file ending is not accepted!\nThe following ending/s is/are accepted: "
			+ VISABUtil.getAcceptedExternalDataEndingsAsString() + ", .visab");
		warningMessage.setStyle("-fx-text-fill: red;");
	    }
	    if (visabFileAccepted) {
		warningMessage.setStyle("-fx-text-fill: green;");
		warningMessage.setText(file.getName() + " successfully saved");
	    }

	} else {
	    warningMessage.setStyle("-fx-text-fill: red;");
	    warningMessage.setText("File already exists! Therefore it was not saved.\nPlease change the file name.");
	}

    }

    @FXML
    public void handlePathViewerButton() throws URISyntaxException {
	main.pathViewerWindow();
    }

    @FXML
    public void handleStatisticsViewButton() throws URISyntaxException {
	main.statisticsWindow();
    }

    public void setFile(File file) {
	this.file = file;
    }

    public void setMain(GUIMain main) {
	this.main = main;
    }
}
