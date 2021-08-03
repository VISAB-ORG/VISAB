package org.visab.oldgui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.util.VISABUtil;
import org.visab.workspace.DatabaseManager;

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

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(MainWindowController.class);

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
            String content = readFile(loadedFilePath.toString());

            boolean externalFileAccepted = false;
            boolean visabFileAccepted = false;

            if (currentFileName.toString().endsWith(".visab")) {
                // show success message & write to Database
                writeFileToDatabase(currentFileName.toString(), content);

                visabFileAccepted = true;

            } else {
                for (int i = 0; i < getAcceptedExternalDataEndings().length; i++) {
                    if (currentFileName.toString().endsWith(getAcceptedExternalDataEndings()[i])) {
                        externalFileAccepted = true;
                    }
                }
            }
            if (externalFileAccepted) {
                writeFileToDatabase(currentFileName.toString(), content);
                warningMessage.setText("The file is not a visab-file!\nTherefore PathViewer won't be available, "
                        + file.getName() + " was saved anyway.");
                warningMessage.setStyle("-fx-text-fill: orange;");
            } else {
                warningMessage.setText("This file ending is not accepted!\nThe following ending/s is/are accepted: "
                        + getAcceptedExternalDataEndingsAsString() + ", .visab");
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

    public static String[] getAcceptedExternalDataEndings() {
        return new String[] { ".txt" };
    }

    public static String getAcceptedExternalDataEndingsAsString() {
        var output = new StringBuilder();
        for (String acceptedExternalDataEnding : getAcceptedExternalDataEndings())
            output.append(acceptedExternalDataEnding);
        return output.toString();
    }

    public static String readFile(String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public static void writeFileToDatabase(String fileName, String content) throws URISyntaxException {

        File databaseDir = new File(DatabaseManager.DATABASE_PATH);
        databaseDir.mkdirs();
        File saveIntoDatabase = new File(DatabaseManager.DATABASE_PATH + fileName);

        BufferedWriter writer;
        try {

            writer = new BufferedWriter(new FileWriter(saveIntoDatabase));
            writer.write(content);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
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
