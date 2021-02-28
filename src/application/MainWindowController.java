package application;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import util.VisABUtil;

public class MainWindowController {

	// Views
	@FXML
	private Button browseFile;
	@FXML
	private Button statisticsViewButton;
	@FXML
	private Button pathViewerButton;
	@FXML
	private Label warningMessage;
	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Main main;

	public void setMain(Main main) {
		this.main = main;
	}

	@FXML
	public void handleBrowseFile() {

		warningMessage.setText("");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");

		file = fileChooser.showOpenDialog(null);

		File folder = new File("data");
		File[] listOfFiles = folder.listFiles();

		// If file is selected
		if (file != null) {

			// Get Current Filename
			Path currentFileName = Paths.get("", file.getName());

			// Check if file exists
			boolean fileExists = false;

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					if (listOfFiles[i].getName().equals(currentFileName.toString())) {
						fileExists = true;
					}
				}
			}

			if (!fileExists) {

				String loadedFilePath = file.getAbsolutePath();
				String content = VisABUtil.readFile(loadedFilePath.toString());

				boolean externalFileAccepted = false;
				boolean visabFileAccepted = false;

				if (currentFileName.toString().endsWith(".visab")) {
					// show success message & write to Database
					VisABUtil.writeFileToDatabase(currentFileName.toString(), content);

					visabFileAccepted = true;

				} else {
					for (int i = 0; i < VisABUtil.getAcceptedExternalDataEndings().length; i++) {
						if (currentFileName.toString().endsWith(VisABUtil.getAcceptedExternalDataEndings()[i])) {
							externalFileAccepted = true;
						}
					}
				}
				if (externalFileAccepted) {
					VisABUtil.writeFileToDatabase(currentFileName.toString(), content);
					warningMessage.setText("The file is not a visab-file!\nTherefore PathViewer won't be available, "
							+ file.getName() + " was saved anyway.");
					warningMessage.setStyle("-fx-text-fill: orange;");
				} else {
					warningMessage.setText("This file ending is not accepted!\nThe following ending/s is/are accepted: "
							+ VisABUtil.getAcceptedExternalDataEndingsAsString() + ", .visab");
					warningMessage.setStyle("-fx-text-fill: red;");
				}
				if (visabFileAccepted) {
					warningMessage.setStyle("-fx-text-fill: green;");
					warningMessage.setText(file.getName() + " successfully saved");
				}

			} else {
				warningMessage.setStyle("-fx-text-fill: red;");
				warningMessage
						.setText("File already exists! Therefore it was not saved.\nPlease change the file name.");
			}

		} else {
			warningMessage.setStyle("-fx-text-fill: orange;");
			warningMessage.setText("Please select file");
		}
	}

	@FXML
	public void handleStatisticsViewButton() {
		main.statisticsWindow();
	}

	@FXML
	public void handlePathViewerButton() {
		main.pathViewerWindow();
	}
}
