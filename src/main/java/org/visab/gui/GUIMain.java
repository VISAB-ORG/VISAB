package org.visab.gui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.api.WebApi;
import org.visab.util.Settings;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The entry point for the FXML Application. Responsible for initializing the
 * different Windows and their respective controllers.
 *
 * @author VISAB 1.0 group
 *
 */
public class GUIMain extends Application {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(GUIMain.class);

    // src/main/resources is part of the classpath, so further subdirectories need
    // to be concerned here
    private static String ABOUT_FXML_PATH = "/AboutWindow.fxml";
    private static String HELP_FXML_PATH = "/HelpWindow.fxml";
    private static String MAIN_FXML_PATH = "/MainWindow.fxml";
    private static String PATHVIEWER_FXML_PATH = "/PathViewerWindow.fxml";
    private static String STATISCTICS_FXML_PATH = "/StatisticsWindow.fxml";

    private Stage primaryStage;

    public void aboutWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(GUIMain.class.getResource(ABOUT_FXML_PATH));
            AnchorPane pane = loader.load();

            primaryStage.setMinHeight(1000.00);
            primaryStage.setMinWidth(1200.00);
            primaryStage.getIcons().add((new Image(Settings.IMAGE_PATH + "visabLogo.png")));
            primaryStage.setTitle("VisAB");

            AboutWindowController aboutWindowController = loader.getController();
            aboutWindowController.setMain(this);

            Scene scene = new Scene(pane);
            scene.getStylesheets().add(getClass().getResource(Settings.CSS_PATH).toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void helpWindow() {
        try {

            FXMLLoader loader = new FXMLLoader(GUIMain.class.getResource(HELP_FXML_PATH));
            AnchorPane pane = loader.load();

            primaryStage.setMinHeight(1000.00);
            primaryStage.setMinWidth(1200.00);
            primaryStage.getIcons().add((new Image(Settings.IMAGE_PATH + "visabLogo.png")));
            primaryStage.setTitle("VisAB");

            HelpWindowController helpWindowController = loader.getController();
            helpWindowController.setMain(this);

            Scene scene = new Scene(pane);
            scene.getStylesheets().add(getClass().getResource(Settings.CSS_PATH).toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for retreiving the files located in the
     * location-specific database.
     * 
     * @return an observable list of file names that are displayed in the GUI.
     */
    private ObservableList<String> loadFilesFromDatabase() {

        File database = new File(Settings.DATA_PATH);
        File[] visabFiles = database.listFiles();
        ObservableList<String> filesComboBox = FXCollections.observableArrayList();

        // Check if there are files in the database or the database does even exist
        if (visabFiles != null) {
            for (int i = 0; i < visabFiles.length; i++) {
                if (visabFiles[i].isFile()) {
                    filesComboBox.add(visabFiles[i].getName());
                }
            }
        }
        return filesComboBox;
    }

    public void mainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(GUIMain.class.getResource(MAIN_FXML_PATH));
            AnchorPane pane = loader.load();

            primaryStage.setMinHeight(1000.00);
            primaryStage.setMinWidth(1200.00);
            primaryStage.getIcons().add((new Image(Settings.IMAGE_PATH + "visabLogo.png")));
            primaryStage.setTitle("VisAB");

            MainWindowController mainWindowController = loader.getController();
            mainWindowController.setMain(this);

            Scene scene = new Scene(pane);
            scene.getStylesheets().add(getClass().getResource(Settings.CSS_PATH).toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pathViewerWindow() {
        try {

            FXMLLoader loader = new FXMLLoader(GUIMain.class.getResource(PATHVIEWER_FXML_PATH));
            AnchorPane pane = loader.load();

            primaryStage.setMinHeight(1000.00);
            primaryStage.setMinWidth(1200.00);
            primaryStage.getIcons().add((new Image(Settings.IMAGE_PATH + "visabLogo.png")));
            primaryStage.setTitle("VisAB");

            PathViewerWindowController pathWindowController = loader.getController();
            ObservableList<String> filesComboBox = loadFilesFromDatabase();
            pathWindowController.updatePage(filesComboBox);
            pathWindowController.setMain(this);

            Scene scene = new Scene(pane);
            scene.getStylesheets().add(getClass().getResource(Settings.CSS_PATH).toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        startApiServer();
        mainWindow();
    }

    /**
     * Starts the WebApi. Does nothing if another WebApi instance is currently
     * running on the Default port.
     */
    private void startApiServer() {
        try {
            WebApi.instance.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return;
    }

    public void statisticsWindow() throws URISyntaxException {
        try {

            FXMLLoader loader = new FXMLLoader(GUIMain.class.getResource(STATISCTICS_FXML_PATH));
            AnchorPane pane = loader.load();

            primaryStage.setMinHeight(1000.00);
            primaryStage.setMinWidth(1200.00);
            primaryStage.getIcons().add((new Image(Settings.IMAGE_PATH + "visabLogo.png")));
            primaryStage.setTitle("VisAB");

            StatisticsWindowController statisticsWindowController = loader.getController();
            ObservableList<String> filesComboBox = loadFilesFromDatabase();
            statisticsWindowController.updatePage(filesComboBox);
            statisticsWindowController.setMain(this);

            Scene scene = new Scene(pane);
            scene.getStylesheets().add(getClass().getResource(Settings.CSS_PATH).toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Is called when the Application is closed.
     */
    @Override
    public void stop() {
        WebApi.instance.shutdown();
    }
}
