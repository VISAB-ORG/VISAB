package org.visab.newgui.visualize.cbrshooter.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.newgui.visualize.cbrshooter.viewmodel.CBRShooterReplayViewModel;
import org.visab.util.VISABUtil;
import org.visab.workspace.config.ConfigManager;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;

/**
 * View that is associated with the respective fxml as a controller to represent
 * its data in terms of the chosen MVVM GUI pattern.
 * 
 * @author leonr
 *
 */
public class CBRShooterReplayView implements FxmlView<CBRShooterReplayViewModel>, Initializable {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(CBRShooterReplayView.class);

    // ----- IMAGE VIEWS -------

    @FXML
    private ImageView healthImage;
    @FXML
    private ImageView ammuImage;
    @FXML
    private ImageView weaponImage;
    @FXML
    private ImageView playImageView;
    @FXML
    private ImageView pauseImageView;

    // ----- VISIBILITY CHECKS -----
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

    // ----- CONTROLS ----
    @FXML
    private Slider frameSlider;
    @FXML
    private Slider veloSlider;
    @FXML
    private ToggleButton playPauseButton;

    // ----- PANES / BOXES -----
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Pane drawPane;
    @FXML
    private VBox vBoxView;
    @FXML
    private Pane panePlan;
    @FXML
    private HBox hBoxScriptPath;
    @FXML
    private HBox hBoxScriptDeaths;
    @FXML
    private HBox hBoxScriptPlayer;

    // ----- LABELS ------
    @FXML
    private Label frameLabel;
    @FXML
    private Label labelCurrentPlanCBR;
    @FXML
    private Label labelCurrentPlanScript;
    @FXML
    private Label veloLabel;

    // --- FXML IMAGES ----

    @FXML
    private Image playImage;
    @FXML
    private Image pauseImage;

    // Images / Icons
    private Image imageScriptBot = new Image(ConfigManager.IMAGE_PATH + "scriptBot.png");
    private Image deathImage = new Image(ConfigManager.IMAGE_PATH + "deathScript.png");
    private Image deathImageCBR = new Image(ConfigManager.IMAGE_PATH + "deadCBR.png");
    private Image imageCbrBot = new Image(ConfigManager.IMAGE_PATH + "cbrBot.png");
    private Image changePlanCBRImage = new Image(ConfigManager.IMAGE_PATH + "changePlan.png");
    private Image changePlanScriptImage = new Image(ConfigManager.IMAGE_PATH + "changePlan.png");

    private ImageView cbrbotImageView = new ImageView(imageCbrBot);
    private ImageView deathImageView = new ImageView(deathImage);
    private ImageView deathImageViewCBR = new ImageView(deathImageCBR);
    private ImageView scriptbotImageView = new ImageView(imageScriptBot);

    // Helper variables
    public static int masterIndex;
    public static int sleepTimer;

    @InjectViewModel
    CBRShooterReplayViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // Dummy Handle Method for frame slider
    @FXML
    public void handleFrameSlider() {
        // TODO: Add call to underlying viewmodel to change data?
        System.out.println("Frame Slider moved to value: " + frameSlider.getValue());
    }

    public void handleVeloSlider() {
        // TODO: Add call to underlying viewmodel to speed up?
        System.out.println("Velocity Slider moved! " + veloSlider.getValue());
        viewModel.setUpdateInterval(1 / veloSlider.getValue()).execute();
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

    @FXML
    public void handlePlayPause(ActionEvent event) {
        if (playPauseButton.isSelected()) {
            viewModel.playData().execute();
        } else {
            viewModel.pauseData().execute();
        }
    }

    @SuppressWarnings({ "unchecked", "resource" })
    private void loadVisabStatistics(String content) {

        // Convert
        List<List<String>> rawData = VISABUtil.convertStringToList(content);

        // Lists for Path Viewer
        List<List<Double>> coordinatesCBRBotList = new ArrayList<List<Double>>();
        List<List<Double>> coordinatesScriptBotList = new ArrayList<List<Double>>();

        List<String> statisticsScriptBotList = new ArrayList<String>();
        List<String> statisticsCBRBotList = new ArrayList<String>();

        List<String> planCBRBotList = new ArrayList<String>();
        List<String> planScriptBotList = new ArrayList<String>();

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
        playImage = new Image(ConfigManager.IMAGE_PATH + "play.png");
        playImageView = new ImageView(playImage);
        pauseImage = new Image(ConfigManager.IMAGE_PATH + "pause.png");
        pauseImageView = new ImageView(pauseImage);
        playPauseButton.setVisible(true);
        playPauseButton.setGraphic(playImageView);

        // Get only coordinates from data
        for (int i = 0; i < rawData.size(); i++) {
            List<String> rawDataRow = rawData.get(i);

            // loop for extracting data
            for (int j = 0; j < rawDataRow.size(); j += 2) {

                // TODO: (Skalierbarkeit)
                // Analoge Schleifeneintr�ge erstellen (s.u.)
                // Dabei muss der entsprechende Attributsname abgeglichen werden.

                // TODO: (Skalierbarkeit)
                // Koordinaten extrahieren (s.u.)
                // F�r Extrahierung der Koordinaten kann der untenstehende Schleifeneintrag
                // kopiert und angepasst werden.

                // extract coordinates
                if (rawDataRow.get(j).contains("coordinatesCBRBot")
                        || rawDataRow.get(j).contains("coordinatesScriptBot")) {

                    // Format Coordinates: remove brackets and change commas to dots
                    String coordinatesRaw = rawDataRow.get(j + 1);
                    coordinatesRaw = coordinatesRaw.replaceAll(",", ".").replaceAll("\\(", "").replaceAll("\\)", "");

//                              System.out.println(coordinatesRaw);
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

        // TODO: (Skalierbarkeit)
        // Methode zur Erstellung einer neuen Tabelle hinzuf�gen (s.o.)
        // Der Inhalt der Methode kann kopiert und angepasst werden.

        // preparated Lists for Path Viewer
        ArrayList<Double> coordinatesCBRBotListPrep = new ArrayList<Double>();
        ArrayList<Double> coordinatesScriptBotListPrep = new ArrayList<Double>();

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
        // Analoge Vorbereitungsliste f�r die Koordinaten bef�llen (s.o.)
        // Draws the map on the first frame
        drawMap(coordinatesCBRBotListPrep, coordinatesScriptBotListPrep, statisticsCBRBotList, statisticsScriptBotList,
                1, ammuPositionList, weaponPositionList, healthPositionList, roundCounterList, false, planCBRBotList,
                planScriptBotList, checkBoxCBRBotPath.isSelected(), checkBoxScriptBotPath.isSelected());
        // TODO: (Skalierbarkeit)
        // Paramater f�r weitere Spieler (Bots) �bergeben (s.o)

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
                                        logger.error("CAUGHT [" + e + "] on Thread.sleep() - stacktrace:");
                                        logger.error(e.getStackTrace().toString());
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
    // Anpassen der Methode f�r weitere Spieler bzgl. Funktionen und Parametern
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
        // Hinzuf�gen und formatieren von Imageviews f�r neue Spieler (s.u.)
        ImageView changePlanCBRImageView = new ImageView(changePlanCBRImage);
        ImageView changePlanScriptImageView = new ImageView(changePlanScriptImage);

        // TODO: (Skalierbarkeit)
        // Analoge Listen f�r neue Spieler initialisieren(s.u.)
        // Initialize Lists for death overviews for both bots
        List<Double> cbrdeathListx = new ArrayList<Double>();
        List<Double> cbrdeathListy = new ArrayList<Double>();
        List<Double> scriptdeathListx = new ArrayList<Double>();
        List<Double> scriptdeathListy = new ArrayList<Double>();
        List<ImageView> cbrDeathImageViews = new ArrayList<ImageView>();
        List<ImageView> scriptDeathImageViews = new ArrayList<ImageView>();

        // Formatting ImageViews for the change of a plan
        // TODO: (Skalierbarkeit)
        // Hinzuf�gen und formatieren von Imageviews f�r neue Spieler (s.u.)
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
        // Schleife f�r Erstellung des Pfades und Visualisierung wesentlicher
        // Spielaspekte
        // f�r neue Spieler erstellen(s.u.)
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
        // Schleife f�r Erstellung des Pfades und Visualisierung wesentlicher
        // Spielaspekte
        // f�r neue Spieler erstellen(s.u.)
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
        // Changelistener f�r neue Checkboxen erstellen und analog einbauen f�r neue
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
        // Alle neuen Elemente der UI m�ssen dem drawPane hinzugef�gt werden(s.u)
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
}
