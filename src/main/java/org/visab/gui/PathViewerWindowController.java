package org.visab.gui;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.visab.gui.model.TableEntryCBRBot;
import org.visab.gui.model.TableEntryScriptBot;
import org.visab.util.Settings;
import org.visab.util.VISABUtil;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;

/**
 * The controller for the PathViewerWindow. TODO: This abomination should
 * probably just be thrown away and only kept for reference.
 * 
 * @author VISAB 1.0 group
 *
 */
public class PathViewerWindowController {

    /*
     * Initializing of variables used for the PathViewer Window
     */

    // Initializing FXML variables
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
    private Line line;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableView statisticsTableCBRBot;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableView statisticsTableScriptBot;
    @FXML
    private Button showCoordinates;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Label infoLabel;
    @FXML
    private Pane drawPane;
    @FXML
    private Label frameLabel;
    @FXML
    private Slider frameSlider;
    @FXML
    private ImageView healthImage;
    @FXML
    private ImageView ammuImage;
    @FXML
    private ImageView weaponImage;
    @FXML
    private Label botTypeLabel1;
    @FXML
    private Label botTypeLabel2;
    @FXML
    private Label botNameLabel1;
    @FXML
    private Label botNameLabel2;
    @FXML
    private Image playImage;
    @FXML
    private Image pauseImage;
    @FXML
    private ImageView playImageView;
    @FXML
    private ImageView pauseImageView;
    @FXML
    private ToggleButton playPauseButton;
    @FXML
    private VBox vBoxView;
    @FXML
    private CheckBox checkBoxScriptBotPath;
    @FXML
    private CheckBox checkBoxScriptBotDeaths;
    @FXML
    private CheckBox checkBoxScriptBotPlayer;
    @FXML
    private CheckBox checkBoxCBRBotPath;
    @FXML
    private CheckBox checkBoxCBRBotDeaths;
    @FXML
    private CheckBox checkBoxCBRBotPlayer;
    @FXML
    private CheckBox checkBoxWeapons;
    @FXML
    private CheckBox checkBoxAmmu;
    @FXML
    private CheckBox checkBoxHealth;
    @FXML
    private Label labelVBOX;
    @FXML
    private Label labelLegend;
    @FXML
    private Pane panePlan;
    @FXML
    private Label labelPlanCBR;
    @FXML
    private Label labelCurrentPlanCBR;
    @FXML
    private Label labelPlanScript;
    @FXML
    private Label labelCurrentPlanScript;
    @FXML
    private Label veloLabel;
    @FXML
    private Slider veloSlider;
    @FXML
    private HBox hBoxScriptPath;
    @FXML
    private HBox hBoxScriptDeaths;
    @FXML
    private HBox hBoxScriptPlayer;

    // Initializing additional varibales
    private Image imageScriptBot = new Image(Settings.IMAGE_PATH + "scriptBot.png");

    private Image deathImage = new Image(Settings.IMAGE_PATH + "deathScript.png");

    private Image deathImageCBR = new Image(Settings.IMAGE_PATH + "deadCBR.png");

    private Image imageCbrBot = new Image(Settings.IMAGE_PATH + "cbrBot.png");

    private Image changePlanCBRImage = new Image(Settings.IMAGE_PATH + "changePlan.png");

    private Image changePlanScriptImage = new Image(Settings.IMAGE_PATH + "changePlan.png");

    private ImageView cbrbotImageView = new ImageView(imageCbrBot);

    private ImageView deathImageView = new ImageView(deathImage);

    private ImageView deathImageViewCBR = new ImageView(deathImageCBR);

    private ImageView scriptbotImageView = new ImageView(imageScriptBot);

    public Task<Void> task;

    public static int masterIndex;

    public static int sleepTimer;

    public GUIMain main;

    public void setMain(GUIMain main) {
	this.main = main;
    }

    // Handle Method for menu navigation
    @FXML
    public void handleBrowseFileMenu() {

	main.mainWindow();
    }

    // Handle Method for menu navigation
    @FXML
    public void handlePathViewerMenu() {
	// DO NOTHING

    }

    // Handle Method for menu navigation
    @FXML
    public void handleStatisticsMenu() throws URISyntaxException {

	main.statisticsWindow();
    }

    // Handle Method for menu navigation
    @FXML
    public void handleHelpMenu() {
	main.helpWindow();
    }

    // Handle Method for menu navigation
    @FXML
    public void handleAboutMenu() {
	main.aboutWindow();
    }

    // Dummy Handle Method for frame slider
    @FXML
    public void handleFrameSlider() {

    }

    // Handle Method for user Selection regarding visability of the Script Bot
    // Player Icon
    @FXML
    public void handleCheckBoxScriptPlayer(ActionEvent event) {
	if (checkBoxScriptBotPlayer.isSelected()) {
	    scriptbotImageView.setVisible(true);
	} else if (checkBoxScriptBotPlayer.isSelected() == false) {
	    scriptbotImageView.setVisible(false);
	}
    }

    // Handle Method for user Selection regarding visability of the CBR Bot Player
    // Icon
    @FXML
    public void handleCheckBoxCBRPlayer(ActionEvent event) {
	if (checkBoxCBRBotPlayer.isSelected()) {
	    cbrbotImageView.setVisible(true);
	} else if (checkBoxCBRBotPlayer.isSelected() == false) {
	    cbrbotImageView.setVisible(false);
	}
    }

    // Handle Method for the show coordinates option including additional core
    // features like the path viewer and its content
    public void handleShowCoordinates() throws URISyntaxException {

	infoLabel.setText("Loading...");
	infoLabel.setStyle("-fx-text-fill: orange;");

	masterIndex = 0;
	checkBoxScriptBotPath.setSelected(true);
	checkBoxScriptBotDeaths.setSelected(true);
	checkBoxScriptBotPlayer.setSelected(true);
	checkBoxCBRBotPath.setSelected(true);
	checkBoxCBRBotDeaths.setSelected(true);
	checkBoxCBRBotPlayer.setSelected(true);
	checkBoxWeapons.setSelected(true);
	checkBoxAmmu.setSelected(true);
	checkBoxHealth.setSelected(true);

	// Remove old Paths
	drawPane.getChildren().clear();
	String fileNameFromComboBox = comboBox.getValue();

	URL res = GUIMain.class.getResource(Settings.DATA_PATH);
	File file = Paths.get(res.toURI()).toFile();
	String absolutePath = file.getAbsolutePath();
	// Read file
	Path filePath = Paths.get("", absolutePath + "/" + fileNameFromComboBox);
	String content = VISABUtil.readFile(filePath.toString());

	boolean externalFileAccepted = false;

	if (fileNameFromComboBox == null) {
	    // Set InfoLabel
	    infoLabel.setText("Please select a file name first!");
	} else if (fileNameFromComboBox.endsWith(".visab")) {
	    // Path Viewer Action with visab file
	    try {
		loadVisabStatistics(content);
	    } catch (Exception e) {
		infoLabel.setText("Visab file corrupted. Please check its content!");
	    }

	    // Set bot type labels (in case of adding bots: create new labels)
	    botTypeLabel1.setText("Type: CBR-Bot");
	    botTypeLabel2.setText("Type: Script-Bot");

	} else {
	    for (int i = 0; i < VISABUtil.getAcceptedExternalDataEndings().length; i++) {
		if (fileNameFromComboBox.endsWith(VISABUtil.getAcceptedExternalDataEndings()[i])) {
		    externalFileAccepted = true;
		}
	    }

	}
	if (externalFileAccepted) {
	    loadExternalStatistics();
	}

    }

    public void updatePage(ObservableList<String> filesComboBox) {
	comboBox.getItems().addAll(filesComboBox);
	comboBox.getSelectionModel().selectFirst();
    }

    @SuppressWarnings({ "unchecked", "resource" })
    private void loadVisabStatistics(String content) {

	// Convert
	List<List<String>> rawData = VISABUtil.convertStringToList(content);

	// Lists for Path Viewer
	List<List<Double>> coordinatesCBRBotList = new ArrayList<List<Double>>();
	List<List<Double>> coordinatesScriptBotList = new ArrayList<List<Double>>();

	List<String> healthScriptBotList = new ArrayList<String>();
	List<String> healthCBRBotList = new ArrayList<String>();

	List<String> weaponScriptBotList = new ArrayList<String>();
	List<String> weaponCBRBotList = new ArrayList<String>();

	List<String> statisticsScriptBotList = new ArrayList<String>();
	List<String> statisticsCBRBotList = new ArrayList<String>();

	List<String> nameScriptBotList = new ArrayList<String>();
	List<String> nameCBRBotList = new ArrayList<String>();

	List<String> planCBRBotList = new ArrayList<String>();
	List<String> planScriptBotList = new ArrayList<String>();

	List<String> weaponMagAmmuCBRBotList = new ArrayList<String>();
	List<String> weaponMagAmmuScriptBotList = new ArrayList<String>();

	List<String> healthPositionList = new ArrayList<String>();
	List<String> weaponPositionList = new ArrayList<String>();
	List<String> ammuPositionList = new ArrayList<String>();

	List<String> roundCounterList = new ArrayList<String>();

	// TODO: (Skalierbarkeit)
	// Analoge Listen erstellen (s.o.)

	// Set Values for relevant global variables
	vBoxView.setVisible(true);
	panePlan.setVisible(true);
	frameSlider.setVisible(true);
	frameLabel.setVisible(true);
	frameLabel.setText("Selected Frame: " + 0);
	sleepTimer = 1000;
	frameSlider.setValue(0);
	playImage = new Image(Settings.IMAGE_PATH + "play.png");
	playImageView = new ImageView(playImage);
	pauseImage = new Image(Settings.IMAGE_PATH + "pause.png");
	pauseImageView = new ImageView(pauseImage);
	playPauseButton.setVisible(true);
	playPauseButton.setGraphic(playImageView);

	// Get only coordinates from data
	for (int i = 0; i < rawData.size(); i++) {
	    List<String> rawDataRow = rawData.get(i);

	    // loop for extracting data
	    for (int j = 0; j < rawDataRow.size(); j += 2) {

		// TODO: (Skalierbarkeit)
		// Analoge Schleifeneinträge erstellen (s.u.)
		// Dabei muss der entsprechende Attributsname abgeglichen werden.

		// Create Lists for table
		// Health
		if (rawDataRow.get(j).contains("healthScriptBot")) {
		    String healthScriptBot = rawDataRow.get(j + 1);
		    healthScriptBotList.add(healthScriptBot);
		}

		if (rawDataRow.get(j).contains("healthCBRBot")) {
		    String healthCBRBot = rawDataRow.get(j + 1);
		    healthCBRBotList.add(healthCBRBot);
		}

		// Weapon
		if (rawDataRow.get(j).contains("weaponScriptBot")) {
		    String weaponScriptBot = rawDataRow.get(j + 1);
		    weaponScriptBotList.add(weaponScriptBot);
		}

		if (rawDataRow.get(j).contains("weaponCBRBot")) {
		    String weaponCBRBot = rawDataRow.get(j + 1);
		    weaponCBRBotList.add(weaponCBRBot);
		}

		// K/D-Ratio aka Statistics
		if (rawDataRow.get(j).contains("statisticScriptBot")) {
		    String statisticScriptBot = rawDataRow.get(j + 1);
		    statisticsScriptBotList.add(statisticScriptBot);
		}

		if (rawDataRow.get(j).contains("statisticCBRBot")) {
		    String statisticCBRBot = rawDataRow.get(j + 1);
		    statisticsCBRBotList.add(statisticCBRBot);
		}

		// Name of each player
		if (rawDataRow.get(j).contains("nameScriptBot")) {
		    String nameScriptBot = rawDataRow.get(j + 1);
		    nameScriptBotList.add(nameScriptBot);
		    botNameLabel1.setText("Name: " + nameScriptBot);
		}

		if (rawDataRow.get(j).contains("nameCBRBot")) {
		    String nameCBRBot = rawDataRow.get(j + 1);
		    nameCBRBotList.add(nameCBRBot);
		    botNameLabel2.setText("Name: " + nameCBRBot);
		}

		// Executed Plans of CBR-Bot & Script-Bot
		if (rawDataRow.get(j).contains("planCBRBot")) {
		    String planCBRBot = rawDataRow.get(j + 1);
		    planCBRBotList.add(planCBRBot);
		}

		if (rawDataRow.get(j).contains("planScriptBot")) {
		    String planScriptBot = rawDataRow.get(j + 1);
		    planScriptBotList.add(planScriptBot);
		}

		// Current ammunition
		if (rawDataRow.get(j).contains("weaponMagAmmuCBRBot")) {
		    String weaponMagAmmuCBRBot = rawDataRow.get(j + 1);
		    weaponMagAmmuCBRBotList.add(weaponMagAmmuCBRBot);
		}

		if (rawDataRow.get(j).contains("weaponMagAmmuScriptBot")) {
		    String weaponMagAmmuCBRBot = rawDataRow.get(j + 1);
		    weaponMagAmmuScriptBotList.add(weaponMagAmmuCBRBot);
		}

		// Position of Health/Weapon/Ammu Spawn
		if (rawDataRow.get(j).contains("healthPosition")) {
		    String healthPosition = rawDataRow.get(j + 1);
		    healthPositionList.add(healthPosition);
		}

		if (rawDataRow.get(j).contains("weaponPosition")) {
		    String weaponPosition = rawDataRow.get(j + 1);
		    weaponPositionList.add(weaponPosition);
		}

		if (rawDataRow.get(j).contains("ammuPosition")) {
		    String ammuPosition = rawDataRow.get(j + 1);
		    ammuPositionList.add(ammuPosition);
		}

		// Round Counter
		if (rawDataRow.get(j).contains("roundCounter")) {
		    String roundCounter = rawDataRow.get(j + 1);
		    roundCounterList.add(roundCounter);
		}

		// TODO: (Skalierbarkeit)
		// Koordinaten extrahieren (s.u.)
		// Für Extrahierung der Koordinaten kann der untenstehende Schleifeneintrag
		// kopiert und angepasst werden.

		// extract coordinates
		if (rawDataRow.get(j).contains("coordinatesCBRBot")
			|| rawDataRow.get(j).contains("coordinatesScriptBot")) {

		    // Format Coordinates: remove brackets and change commas to dots
		    String coordinatesRaw = rawDataRow.get(j + 1);
		    coordinatesRaw = coordinatesRaw.replaceAll(",", ".").replaceAll("\\(", "").replaceAll("\\)", "");

//								System.out.println(coordinatesRaw);
		    // create list for coordinates
		    List<String> coordinatesList = new ArrayList<String>();

		    // further formatting & adding to list
		    Scanner scanner = new Scanner(coordinatesRaw).useDelimiter(". ");
		    scanner.useLocale(Locale.US);
		    while (scanner.hasNext()) {
			coordinatesList.add(scanner.next().replaceAll("\\s+", ""));
		    }
		    // close the scanner
		    scanner.close();
		    for (String s : coordinatesList) {

			// add Coordinates to list for CBR Bot
			if (rawDataRow.get(j).contains("coordinatesCBRBot")) {
			    List<Double> temp = new ArrayList<Double>();
			    temp.add(Double.parseDouble(s));
			    coordinatesCBRBotList.add(temp);
			}
			// add Coordinates to list for Script Bot
			if (rawDataRow.get(j).contains("coordinatesScriptBot")) {
			    List<Double> temp = new ArrayList<Double>();
			    temp.add(Double.parseDouble(s));
			    coordinatesScriptBotList.add(temp);
			}
		    }

		}
	    }
	}

	// Create Table for both bots
	createTableCBRBot();
	createTableScriptBot();

	// TODO: (Skalierbarkeit)
	// Methode zur Erstellung einer neuen Tabelle hinzufügen (s.o.)
	// Der Inhalt der Methode kann kopiert und angepasst werden.

	// preparated Lists for Path Viewer
	ArrayList<Double> coordinatesCBRBotListPrep = new ArrayList<Double>();
	ArrayList<Double> coordinatesScriptBotListPrep = new ArrayList<Double>();

	// TODO: (Skalierbarkeit)
	// Analoge Vorbereitungsliste für die Koordinaten erstellen (s.o.)

	int frameCountCBR = 0;
	int frameCountScript = 0;

	// TODO: (Skalierbarkeit)
	// Vorbereitungsliste befüllen und Tabelleneinträge erstellen (s.u.)

	// lists with coordinates
	for (int i = 0; i < coordinatesCBRBotList.size(); i += 3) {
	    TableEntryCBRBot tableEntryCBRbot = new TableEntryCBRBot();
	    coordinatesCBRBotListPrep.add(coordinatesCBRBotList.get(i + 2).get(0));
	    coordinatesCBRBotListPrep.add(coordinatesCBRBotList.get(i).get(0));
	    // Fill CBR bot Table with information
	    tableEntryCBRbot.setBotType("CBR-Bot");
	    tableEntryCBRbot.setCoordinatesCBRBot(
		    coordinatesCBRBotList.get(i + 2).get(0) + ", " + coordinatesCBRBotList.get(i).get(0));
	    tableEntryCBRbot.setFrame(frameCountCBR);
	    tableEntryCBRbot.setHealthCBRBot(healthCBRBotList.get(frameCountCBR));
	    tableEntryCBRbot.setNameCBRBot(nameCBRBotList.get(frameCountCBR));
	    tableEntryCBRbot.setPlanCBRBot(planCBRBotList.get(frameCountCBR));
	    tableEntryCBRbot.setStatisticCBRBot(statisticsCBRBotList.get(frameCountCBR));
	    tableEntryCBRbot.setWeaponCBRBot(weaponCBRBotList.get(frameCountCBR));
	    tableEntryCBRbot.setWeaponMagAmmuCBRBot(weaponMagAmmuCBRBotList.get(frameCountCBR));
	    tableEntryCBRbot.setHealthPosition(healthPositionList.get(frameCountCBR));
	    tableEntryCBRbot.setWeaponPosition(weaponPositionList.get(frameCountCBR));
	    tableEntryCBRbot.setAmmuPosition(ammuPositionList.get(frameCountCBR));
	    tableEntryCBRbot.setRoundCounter(roundCounterList.get(frameCountCBR));

	    frameCountCBR++;
	    statisticsTableCBRBot.getItems().add(tableEntryCBRbot);
	}

	for (int i = 0; i < coordinatesScriptBotList.size(); i += 3) {
	    TableEntryScriptBot tableEntryScriptBot = new TableEntryScriptBot();
	    coordinatesScriptBotListPrep.add(coordinatesScriptBotList.get(i + 2).get(0));
	    coordinatesScriptBotListPrep.add(coordinatesScriptBotList.get(i).get(0));
	    // Fill Script bot Table with information
	    tableEntryScriptBot.setBotType("Script-Bot");
	    tableEntryScriptBot.setCoordinatesScriptBot(
		    coordinatesScriptBotList.get(i + 2).get(0) + ", " + coordinatesScriptBotList.get(i).get(0));
	    tableEntryScriptBot.setFrame(frameCountScript);
	    tableEntryScriptBot.setHealthScriptBot(healthScriptBotList.get(frameCountScript));
	    tableEntryScriptBot.setNameScriptBot(nameScriptBotList.get(frameCountScript));
	    tableEntryScriptBot.setStatisticScriptBot(statisticsScriptBotList.get(frameCountScript));
	    tableEntryScriptBot.setWeaponScriptBot(weaponScriptBotList.get(frameCountScript));
	    tableEntryScriptBot.setWeaponMagAmmuScriptBot(weaponMagAmmuScriptBotList.get(frameCountScript));
	    tableEntryScriptBot.setHealthPosition(healthPositionList.get(frameCountScript));
	    tableEntryScriptBot.setWeaponPosition(weaponPositionList.get(frameCountScript));
	    tableEntryScriptBot.setAmmuPosition(ammuPositionList.get(frameCountScript));
	    tableEntryScriptBot.setRoundCounter(roundCounterList.get(frameCountScript));
	    tableEntryScriptBot.setPlanScriptBot(planScriptBotList.get(frameCountScript));

	    frameCountScript++;
	    statisticsTableScriptBot.getItems().add(tableEntryScriptBot);
	}

	// Preparing List for drawing the Paths
	for (int i = 0; i < coordinatesCBRBotListPrep.size(); i++) {
	    if (i % 2 == 0) {
		coordinatesCBRBotListPrep.set(i, coordinatesCBRBotListPrep.get(i) * 3.6986301);
	    } else {
		coordinatesCBRBotListPrep.set(i, coordinatesCBRBotListPrep.get(i) * 3.7209302);
	    }
	}
	for (int i = 0; i < coordinatesScriptBotListPrep.size(); i++) {
	    if (i % 2 == 0) {
		coordinatesScriptBotListPrep.set(i, coordinatesScriptBotListPrep.get(i) * 3.6986301);
	    } else {
		coordinatesScriptBotListPrep.set(i, coordinatesScriptBotListPrep.get(i) * 3.7209302);
	    }
	}
	// TODO: (Skalierbarkeit)
	// Analoge Vorbereitungsliste für die Koordinaten befüllen (s.o.)
	// Draws the map on the first frame
	drawMap(coordinatesCBRBotListPrep, coordinatesScriptBotListPrep, statisticsCBRBotList, statisticsScriptBotList,
		1, ammuPositionList, weaponPositionList, healthPositionList, roundCounterList, false, planCBRBotList,
		planScriptBotList, checkBoxCBRBotPath.isSelected(), checkBoxScriptBotPath.isSelected());
	// TODO: (Skalierbarkeit)
	// Paramater für weitere Spieler (Bots) übergeben (s.o)

	frameSlider.setMax(coordinatesScriptBotListPrep.size() / 2 - 1);

	// Listener of the Slider
	frameSlider.valueProperty().addListener(new ChangeListener<Number>() {
	    @Override
	    public void changed(ObservableValue<? extends Number> observable, //
		    Number oldValue, Number newValue) {
		// Initializes masterIndex
		masterIndex = (int) Math.round(newValue.doubleValue());
		// Sets text of the frame label
		frameLabel.setText("Selected Frame: " + masterIndex);
		if (statisticsScriptBotList.size() > masterIndex) {

		    int i = masterIndex;
		    // calls drawMap method for first or i frame
		    if (i == 0) {
			drawMap(coordinatesCBRBotListPrep, coordinatesScriptBotListPrep, statisticsCBRBotList,
				statisticsScriptBotList, 1, ammuPositionList, weaponPositionList, healthPositionList,
				roundCounterList, false, planCBRBotList, planScriptBotList,
				checkBoxCBRBotPath.isSelected(), checkBoxScriptBotPath.isSelected());
		    } else {
			i++;
			drawMap(coordinatesCBRBotListPrep, coordinatesScriptBotListPrep, statisticsCBRBotList,
				statisticsScriptBotList, i * 2, ammuPositionList, weaponPositionList,
				healthPositionList, roundCounterList, false, planCBRBotList, planScriptBotList,
				checkBoxCBRBotPath.isSelected(), checkBoxScriptBotPath.isSelected());
		    }
		}
	    }
	});

	// Listener of the velocity slider
	veloSlider.valueProperty().addListener(new ChangeListener<Number>() {
	    @Override
	    public void changed(ObservableValue<? extends Number> observable, //
		    Number oldValue, Number newValue) {

		// cases for different sleep values
		if ((int) Math.round(newValue.doubleValue()) == 0) {
		    sleepTimer = 1000;
		} else if ((int) Math.round(newValue.doubleValue()) == 2) {
		    sleepTimer = 500;
		} else if ((int) Math.round(newValue.doubleValue()) == 4) {
		    sleepTimer = 250;
		} else if ((int) Math.round(newValue.doubleValue()) == 6) {
		    sleepTimer = 125;
		} else if ((int) Math.round(newValue.doubleValue()) == 8) {
		    sleepTimer = 62;
		}
	    }
	});
	// setOnAction method of the play and pause button
	playPauseButton.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		if (playPauseButton.isSelected()) {
		    // Sets visibility of UI components
		    playPauseButton.setGraphic(pauseImageView);
		    frameSlider.setVisible(false);
		    veloLabel.setVisible(true);
		    veloSlider.setVisible(true);

		    // Starts a new Runnable task
		    Runnable task = new Runnable() {
			public void run() {
			    // Starts the frame loop and updates the frame label with the current frame
			    // position
			    while (masterIndex < coordinatesScriptBotListPrep.size() / 2) {
				if (playPauseButton.isSelected()) {
				    showCoordinates.setDisable(true);
				    int i = masterIndex;
				    i++;
				    drawMap(coordinatesCBRBotListPrep, coordinatesScriptBotListPrep,
					    statisticsCBRBotList, statisticsScriptBotList, i * 2, ammuPositionList,
					    weaponPositionList, healthPositionList, roundCounterList, true,
					    planCBRBotList, planScriptBotList, checkBoxCBRBotPath.isSelected(),
					    checkBoxScriptBotPath.isSelected());
				    masterIndex++;
				    Platform.runLater(new Runnable() {
					@Override
					public void run() {
					    int j = masterIndex;
					    j--;
					    frameLabel.setText("Selected Frame: " + j);
					}
				    });
				    // Triggers sleep after each loop run
				    try {
					Thread.sleep(sleepTimer);
				    } catch (InterruptedException e) {
					System.out.println("First interrupted");
				    }
				    // Interrupts the loop if toggle button pressed again
				} else {
				    break;
				}
			    }
			    // Updates UI compontens after hole loop
			    Platform.runLater(new Runnable() {
				@Override
				public void run() {
				    veloLabel.setVisible(false);
				    veloSlider.setVisible(false);
				    frameSlider.setValue(masterIndex);
				    playPauseButton.setGraphic(playImageView);
				    frameSlider.setVisible(true);
				    showCoordinates.setDisable(false);
				    veloSlider.setValue(0);
				}
			    });
			}
		    };
		    // Starts backgroundThread and activates daemon
		    Thread backgroundThread = new Thread(task);
		    backgroundThread.setDaemon(true);
		    backgroundThread.start();
		}
	    }
	});
	infoLabel.setText("Data successfully loaded!");
	infoLabel.setStyle("-fx-text-fill: green;");

    }

    /**
     * This method handles the drawing of the FPS components.
     * 
     * @param coordinatesCBRBotListPrep    List with x- and y coordinates of the CBR
     *                                     Bot.
     * @param coordinatesScriptBotListPrep List with x- and y coordinates of the
     *                                     script Bot.
     * @param statisticsCBRBotList         List with statistic points of the CBR
     *                                     Bot.
     * @param statisticsScriptBotList      List with statistic points of the script
     *                                     Bot..
     * @param limit                        Integer of the max frame value for the
     *                                     drawing process.
     * @param ammuPositionList             List with Strings of the ammu position.
     * @param weaponPositionList           List with Strings of the weapon position.
     * @param healthPositionList           List with Strings of the health position.
     * @param roundCounterlist             List with String for the rounds.
     * @param multithread                  Boolean if multithreading is active.
     * @param planCBRBotList               List with Strings of plans for the CBR
     *                                     Bot.
     * @param planScriptBotList            List with Strings of plans for the script
     *                                     Bot
     * @param cbrPathVisible               User selection for the visibility of the
     *                                     CBR Bots' Path
     * @param cbrPathVisible               User selection for the visibility of the
     *                                     scriptbots' Path
     * 
     */

    // TODO: (Skalierbarkeit)
    // Anpassen der Methode für weitere Spieler bzgl. Funktionen und Parametern
    // (s.u)
    // Genauere Infos in den Teilabschnitten
    private void drawMap(ArrayList<Double> coordinatesCBRBotListPrep, ArrayList<Double> coordinatesScriptBotListPrep,
	    List<String> statisticsCBRBotList, List<String> statisticsScriptBotList, int limit,
	    List<String> ammuPositionList, List<String> weaponPositionList, List<String> healthPositionList,
	    List<String> roundCounterlist, boolean multithread, List<String> planCBRBotList,
	    List<String> planScriptBotList, boolean cbrPathVisible, boolean scriptPathVisible) {

	int frameCountCBR = 0;
	int frameCountScript = 0;
	// Initialize ImageViews for the change of the actual plan of both bots
	// TODO: (Skalierbarkeit)
	// Hinzufügen und formatieren von Imageviews für neue Spieler (s.u.)
	ImageView changePlanCBRImageView = new ImageView(changePlanCBRImage);
	ImageView changePlanScriptImageView = new ImageView(changePlanScriptImage);

	// TODO: (Skalierbarkeit)
	// Analoge Listen für neue Spieler initialisieren(s.u.)
	// Initialize Lists for death overviews for both bots
	List<Double> cbrdeathListx = new ArrayList<Double>();
	List<Double> cbrdeathListy = new ArrayList<Double>();
	List<Double> scriptdeathListx = new ArrayList<Double>();
	List<Double> scriptdeathListy = new ArrayList<Double>();
	List<ImageView> cbrDeathImageViews = new ArrayList<ImageView>();
	List<ImageView> scriptDeathImageViews = new ArrayList<ImageView>();

	// Formatting ImageViews for the change of a plan
	// TODO: (Skalierbarkeit)
	// Hinzufügen und formatieren von Imageviews für neue Spieler (s.u.)
	changePlanCBRImageView.setScaleX(0.25);
	changePlanCBRImageView.setScaleY(0.25);
	changePlanCBRImageView.setRotate(+45.0);
	changePlanCBRImageView.setVisible(false);
	changePlanScriptImageView.setScaleX(0.25);
	changePlanScriptImageView.setScaleY(0.25);
	changePlanScriptImageView.setRotate(+45.0);
	changePlanScriptImageView.setVisible(false);
	// Formatting Imageviews representing items and Events while visualizing the
	// game
	cbrbotImageView.setScaleX(0.07);
	cbrbotImageView.setScaleY(0.07);
	cbrbotImageView.setRotate(+45.0);
	deathImageViewCBR.setScaleX(0.2);
	deathImageViewCBR.setScaleY(0.2);
	deathImageViewCBR.setRotate(+45.0);
	deathImageViewCBR.setVisible(false);
	deathImageView.setScaleX(0.2);
	deathImageView.setScaleY(0.2);
	deathImageView.setRotate(+45.0);
	deathImageView.setVisible(false);
	scriptbotImageView.setScaleX(0.07);
	scriptbotImageView.setScaleY(0.07);
	scriptbotImageView.setRotate(+45.0);
	healthImage.setVisible(false);
	ammuImage.setVisible(false);
	weaponImage.setVisible(false);
	// Initialize and format Paths for both Bots
	// TODO: (Skalierbarkeit)
	// Initialisieren und formatieren von neuen Spielerpfaden (s.u.)
	javafx.scene.shape.Path cbrPath = new javafx.scene.shape.Path();
	javafx.scene.shape.Path scriptPath = new javafx.scene.shape.Path();
	cbrPath.setVisible(cbrPathVisible);
	cbrPath.setStroke(Color.GREEN);
	cbrPath.setStrokeWidth(2);
	scriptPath.setVisible(scriptPathVisible);
	scriptPath.setStroke(Color.RED);
	scriptPath.setStrokeWidth(2);
	// Creating Path for CBR Bot with respect to the current frame and preparing
	// list for death overview
	// TODO: (Skalierbarkeit)
	// Schleife für Erstellung des Pfades und Visualisierung wesentlicher
	// Spielaspekte
	// für neue Spieler erstellen(s.u.)
	for (int i = 0; i < limit; i += 2) {
	    // Creating new Start of the CBR Path for the first frame and if a round ends in
	    // a draw or a kill appears
	    if (i == 0
		    || statisticsCBRBotList.get(frameCountCBR)
			    .equals(statisticsCBRBotList.get((frameCountCBR) - 1)) == false
		    || roundCounterlist.get(frameCountCBR).equals(roundCounterlist.get((frameCountCBR - 1))) == false) {
		// Case 1 for new start: First Frame(0)--> game Start
		if (i == 0) {
		    cbrPath.getElements()
			    .add(new MoveTo(coordinatesCBRBotListPrep.get(i), coordinatesCBRBotListPrep.get(i + 1)));
		    // Case 2 for new start: Statistics change --> kill appears in game
		} else if (statisticsCBRBotList.get(frameCountCBR)
			.equals(statisticsCBRBotList.get((frameCountCBR) - 1)) == false) {
		    String stringa = statisticsCBRBotList.get(frameCountCBR);
		    String stringb = statisticsCBRBotList.get((frameCountCBR - 1));
		    if (stringa.charAt(stringa.length() - 1) != stringb.charAt(stringb.length() - 1)) {
			// Adding x-Coordinate while dying
			cbrdeathListx.add(coordinatesCBRBotListPrep.get(i - 2));
			// Adding y-Coordinate while dying
			cbrdeathListy.add(coordinatesCBRBotListPrep.get(i - 1));
			// Create Imageview and add to List for death overview
			cbrDeathImageViews.add(new ImageView(deathImageCBR));
		    }
		    cbrPath.getElements()
			    .add(new MoveTo(coordinatesCBRBotListPrep.get(i), coordinatesCBRBotListPrep.get(i + 1)));
		    // Case 3 for new start: Round ends up with draw
		} else {
		    cbrPath.getElements()
			    .add(new MoveTo(coordinatesCBRBotListPrep.get(i), coordinatesCBRBotListPrep.get(i + 1)));
		}
		// Adding Path elements for the CBR Bot
	    } else {
		cbrPath.getElements()
			.add(new LineTo(coordinatesCBRBotListPrep.get(i), coordinatesCBRBotListPrep.get(i + 1)));
	    }
	    // Setting coordinates for change Plan alert
	    changePlanCBRImageView
		    .setX(coordinatesCBRBotListPrep.get(i) - ((changePlanCBRImage.getWidth() / 2) - 15.0));
	    changePlanCBRImageView
		    .setY(coordinatesCBRBotListPrep.get(i + 1) - ((changePlanCBRImage.getHeight() / 2) - 10.0));
	    // Setting coordinates for the visualization of the CBR bots' current Position
	    cbrbotImageView.setX(coordinatesCBRBotListPrep.get(i) - (imageCbrBot.getWidth() / 2));
	    cbrbotImageView.setY(coordinatesCBRBotListPrep.get(i + 1) - (imageCbrBot.getHeight() / 2));
	    // Adding death ImageViews for the CBR Bot with respect to the position while
	    // dying
	    for (int j = 0; j < cbrDeathImageViews.size(); j++) {
		cbrDeathImageViews.get(j).setX(cbrdeathListx.get(j) - (deathImageCBR.getWidth() / 2));
		cbrDeathImageViews.get(j).setY(cbrdeathListy.get(j) - (deathImageCBR.getHeight() / 2));
		cbrDeathImageViews.get(j).setRotate(+45.0);
		cbrDeathImageViews.get(j).setScaleX(0.2);
		cbrDeathImageViews.get(j).setScaleY(0.2);
		cbrDeathImageViews.get(j).setVisible(checkBoxCBRBotDeaths.isSelected());
	    }

	    frameCountCBR++;
	}
	/*
	 * Creating Path for Script Bot with respect to the current frame and preparing
	 * list for death overview additionally call methods for the visualization of
	 * items
	 */
	// TODO: (Skalierbarkeit)
	// Schleife für Erstellung des Pfades und Visualisierung wesentlicher
	// Spielaspekte
	// für neue Spieler erstellen(s.u.)
	for (int i = 0; i < limit; i += 2) {
	    getHealthPosition(healthPositionList.get(i / 2));
	    getAmmuPosition(ammuPositionList.get(i / 2));
	    getWeaponPosition(weaponPositionList.get(i / 2));
	    // Creating new Start of the Script Path for the first frame and if a round ends
	    // in a draw or a kill appears
	    if (i == 0
		    || statisticsScriptBotList.get(frameCountScript)
			    .equals(statisticsScriptBotList.get((frameCountScript) - 1)) == false
		    || roundCounterlist.get(frameCountScript)
			    .equals(roundCounterlist.get((frameCountScript - 1))) == false) {
		// Case 1 for new start: First Frame(0)--> game Start
		if (i == 0) {
		    scriptPath.getElements().add(
			    new MoveTo(coordinatesScriptBotListPrep.get(i), coordinatesScriptBotListPrep.get(i + 1)));
		}
		// Case 2 for new start: Statistics change --> kill appears in game
		else if (statisticsScriptBotList.get(frameCountScript)
			.equals(statisticsScriptBotList.get((frameCountScript) - 1)) == false) {
		    String string1 = statisticsScriptBotList.get(frameCountScript);
		    String string2 = statisticsScriptBotList.get((frameCountScript - 1));
		    if (string1.charAt(string1.length() - 1) != string2.charAt(string2.length() - 1)) {
			// adding x-Coordinate while dying
			scriptdeathListx.add(coordinatesScriptBotListPrep.get(i - 2));
			// adding y-Coordinate while dying
			scriptdeathListy.add(coordinatesScriptBotListPrep.get(i - 1));
			// creating imageView
			scriptDeathImageViews.add(new ImageView(deathImage));
		    }
		    scriptPath.getElements().add(
			    new MoveTo(coordinatesScriptBotListPrep.get(i), coordinatesScriptBotListPrep.get(i + 1)));
		    // Case 3 for new start: Round ends up with draw
		} else {

		    scriptPath.getElements().add(
			    new MoveTo(coordinatesScriptBotListPrep.get(i), coordinatesScriptBotListPrep.get(i + 1)));

		}
		// Adding Path elements for the Script Bot
	    } else {
		scriptPath.getElements()
			.add(new LineTo(coordinatesScriptBotListPrep.get(i), coordinatesScriptBotListPrep.get(i + 1)));
	    }
	    // Setting coordinates for change Plan alert
	    changePlanScriptImageView
		    .setX(coordinatesScriptBotListPrep.get(i) - ((changePlanCBRImage.getWidth() / 2) - 15.0));
	    changePlanScriptImageView
		    .setY(coordinatesScriptBotListPrep.get(i + 1) - ((changePlanScriptImage.getHeight() / 2) - 10.0));
	    // Setting coordinates for the visualization of the Script bots' current
	    // Position
	    scriptbotImageView.setX(coordinatesScriptBotListPrep.get(i) - (imageScriptBot.getWidth() / 2));
	    scriptbotImageView.setY(coordinatesScriptBotListPrep.get(i + 1) - (imageScriptBot.getHeight() / 2));
	    // Adding death ImageViews for the Script Bot with respect to the position while
	    // dying
	    for (int j = 0; j < scriptDeathImageViews.size(); j++) {
		scriptDeathImageViews.get(j).setX(scriptdeathListx.get(j) - (deathImage.getWidth() / 2));
		scriptDeathImageViews.get(j).setY(scriptdeathListy.get(j) - (deathImage.getHeight() / 2));
		scriptDeathImageViews.get(j).setRotate(+45.0);
		scriptDeathImageViews.get(j).setScaleX(0.2);
		scriptDeathImageViews.get(j).setScaleY(0.2);
		scriptDeathImageViews.get(j).setVisible(checkBoxScriptBotDeaths.isSelected());
	    }

	    frameCountScript++;
	}
	// TODO: (Skalierbarkeit)
	// Changelistener für neue Checkboxen erstellen und analog einbauen für neue
	// Spieler (s.u)
	// Changelistener for user selection regarding the visability of the death
	// overview from the CBR bot
	checkBoxCBRBotDeaths.selectedProperty().addListener(new ChangeListener<Boolean>() {

	    @Override
	    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
		    Boolean newValue) {
		for (ImageView imageView : cbrDeathImageViews) {
		    imageView.setVisible(newValue);
		}

	    }
	});
	// Changelistener for user selection regarding the visability of the death
	// overview from the Script bot
	checkBoxScriptBotDeaths.selectedProperty().addListener(new ChangeListener<Boolean>() {

	    @Override
	    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
		    Boolean newValue) {
		for (ImageView imageView : scriptDeathImageViews) {
		    imageView.setVisible(newValue);
		}

	    }
	});
	// Changelistener for user selection regarding the visability of the CBR bots'
	// Path
	checkBoxCBRBotPath.selectedProperty().addListener(new ChangeListener<Boolean>() {

	    @Override
	    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
		    Boolean newValue) {
		cbrPath.setVisible(newValue);

	    }
	});
	// Changelistener for user selection regarding the visability of the Script
	// bots' Path
	checkBoxScriptBotPath.selectedProperty().addListener(new ChangeListener<Boolean>() {

	    @Override
	    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
		    Boolean newValue) {
		scriptPath.setVisible(newValue);

	    }
	});
	// Changelistener for user selection regarding the visability of the CBR bots'
	// change plan alert
	labelCurrentPlanCBR.textProperty().addListener(new ChangeListener<String>() {

	    @Override
	    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
		if (oldValue != "") {
		    changePlanCBRImageView.setVisible(true);
		}

	    }
	});
	// Changelistener for user selection regarding the visability of the Script
	// bots' change plan alert
	labelCurrentPlanScript.textProperty().addListener(new ChangeListener<String>() {

	    @Override
	    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
		if (oldValue != "") {
		    changePlanScriptImageView.setVisible(true);
		}

	    }
	});
	// TODO: (Skalierbarkeit)
	// Alle neuen Elemente der UI müssen dem drawPane hinzugefügt werden(s.u)
	// Updates the drawPane in a thread or not
	if (multithread) {
	    Platform.runLater(new Runnable() {
		@Override
		public void run() {
		    int i = masterIndex;
		    i--;

		    // Clears current drawPane and adds new children
		    drawPane.getChildren().clear();
		    labelCurrentPlanScript.setText(planScriptBotList.get(i));
		    labelCurrentPlanCBR.setText(planCBRBotList.get(i));
		    drawPane.getChildren().addAll(scriptDeathImageViews);
		    drawPane.getChildren().addAll(cbrDeathImageViews);
		    drawPane.getChildren().addAll(scriptPath, cbrPath, cbrbotImageView, scriptbotImageView,
			    deathImageView, healthImage, deathImageViewCBR, ammuImage, weaponImage,
			    changePlanCBRImageView, changePlanScriptImageView);

		}
	    });
	} else {
	    // Clears current drawPane and adds new children
	    drawPane.getChildren().clear();
	    labelCurrentPlanScript.setText(planScriptBotList.get(masterIndex));
	    labelCurrentPlanCBR.setText(planCBRBotList.get(masterIndex));
	    drawPane.getChildren().addAll(scriptDeathImageViews);
	    drawPane.getChildren().addAll(cbrDeathImageViews);
	    drawPane.getChildren().addAll(scriptPath, cbrPath, cbrbotImageView, scriptbotImageView, deathImageView,
		    healthImage, deathImageViewCBR, ammuImage, weaponImage, changePlanCBRImageView,
		    changePlanScriptImageView);

	}
    }

    /**
     * This method initialized the ImageView for the item: Health. It sets the
     * visibility and the layout position of the ImageView for a specific spawn
     * point on the map.
     * 
     * @param String of the certain item.
     */
    private void getHealthPosition(String item) {

	if (item.contains("health")) {
	    if (checkBoxHealth.isSelected()) {
		if (item.contains("PointA")) {
		    healthImage.setVisible(checkBoxHealth.isSelected());
		    healthImage.setLayoutX(-620);
		    healthImage.setLayoutY(-70);
		    healthImage.setOpacity(100.0);
		} else {
		    healthImage.setVisible(checkBoxHealth.isSelected());
		    healthImage.setLayoutX(-40);
		    healthImage.setLayoutY(-80);
		    healthImage.setOpacity(100.0);
		}
	    } else {
		healthImage.setVisible(false);

	    }
	} else {
	    healthImage.setOpacity(0.0);
	}
	// Changelistener for user selection regarding the visability of the health
	// containers
	checkBoxHealth.selectedProperty().addListener(new ChangeListener<Boolean>() {

	    @Override
	    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
		    Boolean newValue) {
		if (item.contains("health")) {
		    healthImage.setVisible(newValue);

		}

	    }
	});

    }

    /**
     * This method initialized the ImageView for the item: Ammu. It sets the
     * visibility and the layout position of the ImageView for a specific spawn
     * point on the map.
     * 
     * @param String of the certain item.
     */
    private void getAmmuPosition(String item) {
	if (item.contains("ammu")) {
	    if (checkBoxAmmu.isSelected()) {
		if (item.contains("PointA")) {
		    ammuImage.setVisible(checkBoxAmmu.isSelected());
		    ammuImage.setLayoutX(-260);
		    ammuImage.setLayoutY(-220);
		    ammuImage.setOpacity(100.0);
		} else if (item.contains("PointB")) {
		    ammuImage.setVisible(checkBoxAmmu.isSelected());
		    ammuImage.setLayoutX(-420);
		    ammuImage.setLayoutY(60);
		    ammuImage.setOpacity(100.0);
		} else if (item.contains("PointC")) {
		    ammuImage.setVisible(checkBoxAmmu.isSelected());
		    ammuImage.setLayoutX(-335);
		    ammuImage.setLayoutY(165);
		    ammuImage.setOpacity(100.0);
		} else if (item.contains("PointD")) {
		    ammuImage.setVisible(checkBoxAmmu.isSelected());
		    ammuImage.setLayoutX(-310);
		    ammuImage.setLayoutY(-340);
		    ammuImage.setOpacity(100.0);
		} else if (item.contains("PointE")) {
		    ammuImage.setVisible(checkBoxAmmu.isSelected());
		    ammuImage.setLayoutX(-340);
		    ammuImage.setLayoutY(-60);
		    ammuImage.setOpacity(100.0);
		}
	    } else {
		ammuImage.setVisible(false);
	    }

	} else {
	    ammuImage.setOpacity(0.0);
	}
	// Changelistener for user selection regarding the visability of the ammu
	// containers
	checkBoxAmmu.selectedProperty().addListener(new ChangeListener<Boolean>() {

	    @Override
	    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
		    Boolean newValue) {
		if (item.contains("ammu")) {
		    ammuImage.setVisible(newValue);

		}

	    }
	});
    }

    /**
     * This method initialized the ImageView for the item: Weapon. It sets the
     * visibility and the layout position of the ImageView for a specific spawn
     * point on the map.
     * 
     * @param String of the certain item.
     */
    private void getWeaponPosition(String item) {

	if (item.contains("weapon")) {
	    if (checkBoxWeapons.isSelected()) {
		if (item.contains("PointA")) {
		    weaponImage.setVisible(checkBoxWeapons.isSelected());
		    weaponImage.setLayoutX(-420);
		    weaponImage.setLayoutY(85);
		    weaponImage.setOpacity(100.0);
		} else if (item.contains("PointB")) {
		    weaponImage.setVisible(checkBoxWeapons.isSelected());
		    weaponImage.setLayoutX(-255);
		    weaponImage.setLayoutY(-180);
		    weaponImage.setOpacity(100.0);
		}
	    } else {
		weaponImage.setVisible(false);

	    }
	} else {
	    weaponImage.setOpacity(0.0);

	}
	// Changelistener for user selection regarding the visability of the weapon
	// containers
	checkBoxWeapons.selectedProperty().addListener(new ChangeListener<Boolean>() {

	    @Override
	    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
		    Boolean newValue) {
		if (item.contains("weapon")) {
		    weaponImage.setVisible(newValue);

		}

	    }
	});

    }

    private void loadExternalStatistics() {
	// If file is external
	statisticsTableCBRBot.getItems().clear();
	statisticsTableScriptBot.getItems().clear();
	infoLabel.setText("PathViewer is not available!");
	infoLabel.setStyle("-fx-text-fill: orange;");
    }

    @SuppressWarnings("unchecked")
    private void createTableCBRBot() {
	// Clear to show entries only once
	VISABUtil.clearTable(statisticsTableCBRBot);

	// Create Table Columns
	@SuppressWarnings("rawtypes")
	TableColumn botType = new TableColumn("botType");
	botType.setCellValueFactory(new PropertyValueFactory<>("botType"));
//				botType.prefWidthProperty().bind(statisticsTable.widthProperty().divide(1.5));

	@SuppressWarnings("rawtypes")
	TableColumn frame = new TableColumn("frame");
	frame.setCellValueFactory(new PropertyValueFactory<>("frame"));
//				frame.prefWidthProperty().bind(statisticsTable.widthProperty().divide(3.06));

	@SuppressWarnings("rawtypes")
	TableColumn coordinatesCBRBot = new TableColumn("coordinatesCBRBot");
	coordinatesCBRBot.setCellValueFactory(new PropertyValueFactory<>("coordinatesCBRBot"));

	@SuppressWarnings("rawtypes")
	TableColumn healthCBRBot = new TableColumn("healthCBRBot");
	healthCBRBot.setCellValueFactory(new PropertyValueFactory<>("healthCBRBot"));

	@SuppressWarnings("rawtypes")
	TableColumn weaponCBRBot = new TableColumn("weaponCBRBot");
	weaponCBRBot.setCellValueFactory(new PropertyValueFactory<>("weaponCBRBot"));

	@SuppressWarnings("rawtypes")
	TableColumn statisticCBRBot = new TableColumn("statisticCBRBot");
	statisticCBRBot.setCellValueFactory(new PropertyValueFactory<>("statisticCBRBot"));

	@SuppressWarnings("rawtypes")
	TableColumn nameCBRBot = new TableColumn("nameCBRBot");
	nameCBRBot.setCellValueFactory(new PropertyValueFactory<>("nameCBRBot"));

	@SuppressWarnings("rawtypes")
	TableColumn planCBRBot = new TableColumn("planCBRBot");
	planCBRBot.setCellValueFactory(new PropertyValueFactory<>("planCBRBot"));

	@SuppressWarnings("rawtypes")
	TableColumn weaponMagAmmuCBRBot = new TableColumn("weaponMagAmmuCBRBot");
	weaponMagAmmuCBRBot.setCellValueFactory(new PropertyValueFactory<>("weaponMagAmmuCBRBot"));

	@SuppressWarnings("rawtypes")
	TableColumn healthPosition = new TableColumn("healthPosition");
	healthPosition.setCellValueFactory(new PropertyValueFactory<>("healthPosition"));

	@SuppressWarnings("rawtypes")
	TableColumn weaponPosition = new TableColumn("weaponPosition");
	weaponPosition.setCellValueFactory(new PropertyValueFactory<>("weaponPosition"));

	@SuppressWarnings("rawtypes")
	TableColumn ammuPosition = new TableColumn("ammuPosition");
	ammuPosition.setCellValueFactory(new PropertyValueFactory<>("ammuPosition"));

	@SuppressWarnings("rawtypes")
	TableColumn roundCounter = new TableColumn("roundCounter");
	roundCounter.setCellValueFactory(new PropertyValueFactory<>("roundCounter"));

	// All possible Table Columns
//		statisticsTableCBRBot.getColumns().addAll(botType, frame, coordinatesCBRBot, healthCBRBot, weaponCBRBot,
//				statisticCBRBot, nameCBRBot, planCBRBot, weaponMagAmmuCBRBot, healthPosition, weaponPosition,
//				ammuPosition, roundCounter);

	// Cleaned Up Table
	statisticsTableCBRBot.getColumns().addAll(frame, coordinatesCBRBot, healthCBRBot, weaponCBRBot, statisticCBRBot,
		planCBRBot);
    }

    @SuppressWarnings("unchecked")
    private void createTableScriptBot() {
	// Clear to show entries only once
	VISABUtil.clearTable(statisticsTableScriptBot);

	// Create Table
	@SuppressWarnings("rawtypes")
	TableColumn botType = new TableColumn("botType");
	botType.setCellValueFactory(new PropertyValueFactory<>("botType"));
//				botType.prefWidthProperty().bind(statisticsTable.widthProperty().divide(1.5));

	@SuppressWarnings("rawtypes")
	TableColumn frame = new TableColumn("frame");
	frame.setCellValueFactory(new PropertyValueFactory<>("frame"));
//				frame.prefWidthProperty().bind(statisticsTable.widthProperty().divide(3.06));

	@SuppressWarnings("rawtypes")
	TableColumn coordinatesScriptBot = new TableColumn("coordinatesScriptBot");
	coordinatesScriptBot.setCellValueFactory(new PropertyValueFactory<>("coordinatesScriptBot"));

	@SuppressWarnings("rawtypes")
	TableColumn healthScriptBot = new TableColumn("healthScriptBot");
	healthScriptBot.setCellValueFactory(new PropertyValueFactory<>("healthScriptBot"));

	@SuppressWarnings("rawtypes")
	TableColumn weaponScriptBot = new TableColumn("weaponScriptBot");
	weaponScriptBot.setCellValueFactory(new PropertyValueFactory<>("weaponScriptBot"));

	@SuppressWarnings("rawtypes")
	TableColumn statisticScriptBot = new TableColumn("statisticScriptBot");
	statisticScriptBot.setCellValueFactory(new PropertyValueFactory<>("statisticScriptBot"));

	@SuppressWarnings("rawtypes")
	TableColumn nameScriptBot = new TableColumn("nameScriptBot");
	nameScriptBot.setCellValueFactory(new PropertyValueFactory<>("nameScriptBot"));

	@SuppressWarnings("rawtypes")
	TableColumn planScriptBot = new TableColumn("planScriptBot");
	planScriptBot.setCellValueFactory(new PropertyValueFactory<>("planScriptBot"));

	@SuppressWarnings("rawtypes")
	TableColumn weaponMagAmmuScriptBot = new TableColumn("weaponMagAmmuScriptBot");
	weaponMagAmmuScriptBot.setCellValueFactory(new PropertyValueFactory<>("weaponMagAmmuScriptBot"));

	@SuppressWarnings("rawtypes")
	TableColumn healthPosition = new TableColumn("healthPosition");
	healthPosition.setCellValueFactory(new PropertyValueFactory<>("healthPosition"));

	@SuppressWarnings("rawtypes")
	TableColumn weaponPosition = new TableColumn("weaponPosition");
	weaponPosition.setCellValueFactory(new PropertyValueFactory<>("weaponPosition"));

	@SuppressWarnings("rawtypes")
	TableColumn ammuPosition = new TableColumn("ammuPosition");
	ammuPosition.setCellValueFactory(new PropertyValueFactory<>("ammuPosition"));

	@SuppressWarnings("rawtypes")
	TableColumn roundCounter = new TableColumn("roundCounter");
	roundCounter.setCellValueFactory(new PropertyValueFactory<>("roundCounter"));

	// All possible Table Columns
//		statisticsTableScriptBot.getColumns().addAll(botType, frame, coordinatesScriptBot, healthScriptBot, weaponScriptBot,
//				statisticScriptBot, nameScriptBot, weaponMagAmmuScriptBot, healthPosition, weaponPosition,
//				ammuPosition, roundCounter);

	// Cleaned Up Table
	statisticsTableScriptBot.getColumns().addAll(frame, coordinatesScriptBot, healthScriptBot, weaponScriptBot,
		statisticScriptBot, planScriptBot);
    }
}
