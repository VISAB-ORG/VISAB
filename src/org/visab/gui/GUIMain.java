package org.visab.gui;

import java.io.File;
import java.io.IOException;

import org.visab.api.WebApi;
import org.visab.processing.cbrshooter.CBRShooterFile;
import org.visab.processing.cbrshooter.model.CBRShooterStatistics;
import org.visab.util.Settings;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GUIMain extends Application {

    private WebApi webApi;
    private Stage primaryStage;

    public Stage getPrimaryStage() {
	return primaryStage;
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

    private void startApiServer() {
	new CBRShooterFile(null);
	try {
	    webApi = new WebApi(Settings.API_PORT);
	    webApi.start();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return;
    }

    public void mainWindow() {
	try {

	    FXMLLoader loader = new FXMLLoader(GUIMain.class.getResource("MainWindow.fxml"));
	    AnchorPane pane = loader.load();

	    primaryStage.setMinHeight(1000.00);
	    primaryStage.setMinWidth(1200.00);
	    primaryStage.getIcons().add((new Image("file:img/visabLogo.png")));
	    primaryStage.setTitle("VisAB");

	    MainWindowController mainWindowController = loader.getController();
	    mainWindowController.setMain(this);

	    Scene scene = new Scene(pane);
	    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(scene);
	    primaryStage.show();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void pathViewerWindow() {
	try {

	    FXMLLoader loader = new FXMLLoader(GUIMain.class.getResource("PathViewerWindow.fxml"));
	    AnchorPane pane = loader.load();

	    primaryStage.setMinHeight(1000.00);
	    primaryStage.setMinWidth(1200.00);
	    primaryStage.getIcons().add((new Image("file:img/visabLogo.png")));
	    primaryStage.setTitle("VisAB");

	    PathViewerWindowController pathWindowController = loader.getController();
	    ObservableList<String> filesComboBox = loadFilesFromDatabase();
	    pathWindowController.updatePage(filesComboBox);
	    pathWindowController.setMain(this);

	    Scene scene = new Scene(pane);
	    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(scene);
	    primaryStage.show();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void statisticsWindow() {
	try {

	    FXMLLoader loader = new FXMLLoader(GUIMain.class.getResource("StatisticsWindow.fxml"));
	    AnchorPane pane = loader.load();

	    primaryStage.setMinHeight(1000.00);
	    primaryStage.setMinWidth(1200.00);
	    primaryStage.getIcons().add((new Image("file:img/visabLogo.png")));
	    primaryStage.setTitle("VisAB");

	    StatisticsWindowController statisticsWindowController = loader.getController();
	    ObservableList<String> filesComboBox = loadFilesFromDatabase();
	    statisticsWindowController.updatePage(filesComboBox);
	    statisticsWindowController.setMain(this);

	    Scene scene = new Scene(pane);
	    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(scene);
	    primaryStage.show();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void aboutWindow() {
	try {
	    FXMLLoader loader = new FXMLLoader(GUIMain.class.getResource("AboutWindow.fxml"));
	    AnchorPane pane = loader.load();

	    primaryStage.setMinHeight(1000.00);
	    primaryStage.setMinWidth(1200.00);
	    primaryStage.getIcons().add((new Image("file:img/visabLogo.png")));
	    primaryStage.setTitle("VisAB");

	    AboutWindowController aboutWindowController = loader.getController();
	    aboutWindowController.setMain(this);

	    Scene scene = new Scene(pane);
	    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(scene);
	    primaryStage.show();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void helpWindow() {
	try {

	    FXMLLoader loader = new FXMLLoader(GUIMain.class.getResource("HelpWindow.fxml"));
	    AnchorPane pane = loader.load();

	    primaryStage.setMinHeight(1000.00);
	    primaryStage.setMinWidth(1200.00);
	    primaryStage.getIcons().add((new Image("file:img/visabLogo.png")));
	    primaryStage.setTitle("VisAB");

	    HelpWindowController helpWindowController = loader.getController();
	    helpWindowController.setMain(this);

	    Scene scene = new Scene(pane);
	    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(scene);
	    primaryStage.show();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void stop() {
	webApi.shutdown();
    }

    public static void main(String[] args) {
	launch(args);
    }

    private ObservableList<String> loadFilesFromDatabase() {
	// Read database for Combobox
	File folder = new File("data");
	File[] listOfFiles = folder.listFiles();

	ObservableList<String> filesComboBox = FXCollections.observableArrayList();

	for (int i = 0; i < listOfFiles.length; i++) {
	    if (listOfFiles[i].isFile()) {
		filesComboBox.add(listOfFiles[i].getName());
	    }
	}
	return filesComboBox;
    }
}
