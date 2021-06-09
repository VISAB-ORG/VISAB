package org.visab.gui;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.gui.model.TableEntry;
import org.visab.gui.model.TableEntryStatisticsVisab;
import org.visab.util.SystemSettings;
import org.visab.util.VISABUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * The controller for the StatisticsWindow. TODO: Should likely be thrown away
 * aswell.
 * 
 * @author VISAB 1.0 group
 *
 */
public class StatisticsWindowController {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(StatisticsWindowController.class);

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
    private Button loadStatistics;
    @SuppressWarnings("rawtypes")
    @FXML
    private TableView statisticsTable;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Label infoLabel;

    @FXML
    private StackedBarChart<String, Number> planChartCBRBot;
    @FXML
    private CategoryAxis xAxisPlanChartCBRBot;
    @FXML
    private NumberAxis yAxisPlanChartCBRBot;

    @FXML
    private StackedBarChart<String, Number> planChartScriptBot;
    @FXML
    private CategoryAxis xAxisPlanChartScriptBot;
    @FXML
    private NumberAxis yAxisPlanChartScriptBot;

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
        // DO NOTHING
    }

    @FXML
    public void handleHelpMenu() {
        main.helpWindow();
    }

    @FXML
    public void handleAboutMenu() {
        main.aboutWindow();
    }

    @FXML
    public void handleLoadStatistics() throws URISyntaxException {
        String fileNameFromComboBox = comboBox.getValue();

        boolean externalFileAccepted = false;

        String content = VISABUtil.readFile(SystemSettings.DATA_PATH + fileNameFromComboBox);

        if (fileNameFromComboBox == null) {
            // Set InfoLabel
            infoLabel.setText("Please select a file name first!");

        } else if (fileNameFromComboBox.endsWith(".visab")) {
            // If file is visab file
            try {
                loadVisabStatistics(content);
            } catch (Exception e) {
                infoLabel.setText("Visab file corrupted. Please check its content!");
            }

        } else {
            for (int i = 0; i < VISABUtil.getAcceptedExternalDataEndings().length; i++) {
                if (fileNameFromComboBox.endsWith(VISABUtil.getAcceptedExternalDataEndings()[i])) {
                    externalFileAccepted = true;
                }
            }

        }
        if (externalFileAccepted) {
            // If file is external
            loadExternalStatistics(content);
        }

    }

    public void updatePage(ObservableList<String> filesComboBox) {
        comboBox.getItems().addAll(filesComboBox);
        comboBox.getSelectionModel().selectFirst();
    }

    private void loadVisabStatistics(String content) {

        // Plan Counters for Charts
        int campCountCBR = 0;
        int collectItemCountCBR = 0;
        int moveToEnemyCountCBR = 0;
        int reloadCountCBR = 0;
        int seekCountCBR = 0;
        int shootCountCBR = 0;
        int switchWeaponCountCBR = 0;
        int useCoverCountCBR = 0;

        int campCountScript = 0;
        int collectItemCountScript = 0;
        int moveToEnemyCountScript = 0;
        int reloadCountScript = 0;
        int seekCountScript = 0;
        int shootCountScript = 0;
        int switchWeaponCountScript = 0;

        List<Integer> calculatedCounters = createTableFromContentVisab(content, campCountCBR, collectItemCountCBR,
                moveToEnemyCountCBR, reloadCountCBR, seekCountCBR, shootCountCBR, switchWeaponCountCBR,
                useCoverCountCBR, campCountScript, collectItemCountScript, moveToEnemyCountScript, reloadCountScript,
                seekCountScript, shootCountScript, switchWeaponCountScript);

        createPlanChartCBRBot(calculatedCounters);
        createPlanChartScriptBot(calculatedCounters);

        // Clear infoLabel
        infoLabel.setText("Data successfully loaded!");
        infoLabel.setStyle("-fx-text-fill: green;");
    }

    private void loadExternalStatistics(String content) {

        planChartCBRBot.getData().clear();
        planChartScriptBot.getData().clear();

        createTableFromContentExternal(content);

        infoLabel.setText("No Visab file selected! Path Viewer Menu and Plan Chart is not available.");
        infoLabel.setStyle("-fx-text-fill: orange;");
    }

    private List<Integer> createTableFromContentVisab(String content, int campCount, int collectItemCount,
            int moveToEnemyCount, int reloadCount, int seekCount, int shootCount, int switchWeaponCount,
            int useCoverCount, int campCountScript, int collectItemCountScript, int moveToEnemyCountScript,
            int reloadCountScript, int seekCountScript, int shootCountScript, int switchWeaponCountScript) {

        createTableVisabStatistics();

        List<Integer> counters = fillTableVisabStatistics(content, campCount, collectItemCount, moveToEnemyCount,
                reloadCount, seekCount, shootCount, switchWeaponCount, useCoverCount, campCountScript,
                collectItemCountScript, moveToEnemyCountScript, reloadCountScript, seekCountScript, shootCountScript,
                switchWeaponCountScript);

        return counters;

    }

    @SuppressWarnings("unchecked")
    private void createTableFromContentExternal(String content) {

        VISABUtil.clearTable(statisticsTable);
        // Convert
        List<List<String>> rawData = VISABUtil.convertStringToList(content);

        // Create Table
        @SuppressWarnings("rawtypes")
        TableColumn col1 = new TableColumn("Name");
        col1.setCellValueFactory(new PropertyValueFactory<>("Name"));
        col1.prefWidthProperty().bind(statisticsTable.widthProperty().divide(2));

        @SuppressWarnings("rawtypes")
        TableColumn col2 = new TableColumn("Value");
        col2.setCellValueFactory(new PropertyValueFactory<>("Value"));
        col2.prefWidthProperty().bind(statisticsTable.widthProperty().divide(2));

        statisticsTable.getColumns().addAll(col1, col2);

        for (int i = 0; i < rawData.size(); i++) {
            List<String> temp2 = rawData.get(i);
            TableEntry tableEntry = new TableEntry();
            for (int j = 0; j < temp2.size(); j += 2) {
                tableEntry.setName(temp2.get(j));
                tableEntry.setValue(temp2.get(j + 1));
            }
            statisticsTable.getItems().add(tableEntry);
        }
    }

    @SuppressWarnings("unchecked")
    private void createTableVisabStatistics() {

        VISABUtil.clearTable(statisticsTable);

        // Create Table
        @SuppressWarnings("rawtypes")
        TableColumn frame = new TableColumn("frame");
        frame.setCellValueFactory(new PropertyValueFactory<>("frame"));

        @SuppressWarnings("rawtypes")
        TableColumn coordinatesCBRBot = new TableColumn("coordinatesCBRBot");
        coordinatesCBRBot.setCellValueFactory(new PropertyValueFactory<>("coordinatesCBRBot"));

        @SuppressWarnings("rawtypes")
        TableColumn coordinatesScriptBot = new TableColumn("coordinatesScriptBot");
        coordinatesScriptBot.setCellValueFactory(new PropertyValueFactory<>("coordinatesScriptBot"));

        @SuppressWarnings("rawtypes")
        TableColumn healthCBRBot = new TableColumn("healthCBRBot");
        healthCBRBot.setCellValueFactory(new PropertyValueFactory<>("healthCBRBot"));

        @SuppressWarnings("rawtypes")
        TableColumn healthScriptBot = new TableColumn("healthScriptBot");
        healthScriptBot.setCellValueFactory(new PropertyValueFactory<>("healthScriptBot"));

        @SuppressWarnings("rawtypes")
        TableColumn weaponCBRBot = new TableColumn("weaponCBRBot");
        weaponCBRBot.setCellValueFactory(new PropertyValueFactory<>("weaponCBRBot"));

        @SuppressWarnings("rawtypes")
        TableColumn weaponScriptBot = new TableColumn("weaponScriptBot");
        weaponScriptBot.setCellValueFactory(new PropertyValueFactory<>("weaponScriptBot"));

        @SuppressWarnings("rawtypes")
        TableColumn statisticsCBRBot = new TableColumn("statisticsCBRBot");
        statisticsCBRBot.setCellValueFactory(new PropertyValueFactory<>("statisticsCBRBot"));

        @SuppressWarnings("rawtypes")
        TableColumn statisticScriptBot = new TableColumn("statisticScriptBot");
        statisticScriptBot.setCellValueFactory(new PropertyValueFactory<>("statisticScriptBot"));

        @SuppressWarnings("rawtypes")
        TableColumn nameCBRBot = new TableColumn("nameCBRBot");
        nameCBRBot.setCellValueFactory(new PropertyValueFactory<>("nameCBRBot"));

        @SuppressWarnings("rawtypes")
        TableColumn nameScriptBot = new TableColumn("nameScriptBot");
        nameScriptBot.setCellValueFactory(new PropertyValueFactory<>("nameScriptBot"));

        @SuppressWarnings("rawtypes")
        TableColumn planCBRBot = new TableColumn("planCBRBot");
        planCBRBot.setCellValueFactory(new PropertyValueFactory<>("planCBRBot"));

        @SuppressWarnings("rawtypes")
        TableColumn weaponMagAmmuCBRBot = new TableColumn("weaponMagAmmuCBRBot");
        weaponMagAmmuCBRBot.setCellValueFactory(new PropertyValueFactory<>("weaponMagAmmuCBRBot"));

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

        // TODO: (Skalierbarkeit)
        // Neue Spalten erstellen (s.o.)
        // Neue Spalten hinzuf�gen (s.u.)

        statisticsTable.getColumns().addAll(frame, coordinatesCBRBot, coordinatesScriptBot, healthCBRBot,
                healthScriptBot, weaponCBRBot, weaponScriptBot, statisticsCBRBot, statisticScriptBot, nameCBRBot,
                nameScriptBot, planCBRBot, weaponMagAmmuCBRBot, weaponMagAmmuScriptBot, healthPosition, weaponPosition,
                ammuPosition, roundCounter);
    }

    @SuppressWarnings("unchecked")
    private List<Integer> fillTableVisabStatistics(String content, int campCountCBR, int collectItemCountCBR,
            int moveToEnemyCountCBR, int reloadCountCBR, int seekCountCBR, int shootCountCBR, int switchWeaponCountCBR,
            int useCoverCountCBR, int campCountScript, int collectItemCountScript, int moveToEnemyCountScript,
            int reloadCountScript, int seekCountScript, int shootCountScript, int switchWeaponCountScript) {

        // Lists for Table
        List<String> coordinatesCBRBotList = new ArrayList<String>();
        List<String> coordinatesScriptBotList = new ArrayList<String>();

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

        // Convert
        List<List<String>> rawData = VISABUtil.convertStringToList(content);

        int frameCount = 0;

        List<Integer> counters = new ArrayList<Integer>();

        for (int i = 0; i < rawData.size(); i++) {
            List<String> rawDataRow = rawData.get(i);

            for (int j = 0; j < rawDataRow.size(); j += 2) {

                // TODO: (Skalierbarkeit)
                // Analoge Schleifeneintr�ge erstellen (s.u.)
                // Dabei muss der entsprechende Attributsname abgeglichen werden.

                // Coordinates
                if (rawDataRow.get(j).contains("coordinatesCBRBot")) {
                    String coordinatesCBRBot = rawDataRow.get(j + 1);
                    coordinatesCBRBotList.add(coordinatesCBRBot);
                    frameCount++;
                }

                if (rawDataRow.get(j).contains("coordinatesScriptBot")) {
                    String coordinatesScriptBot = rawDataRow.get(j + 1);
                    coordinatesScriptBotList.add(coordinatesScriptBot);
                }

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
                }

                if (rawDataRow.get(j).contains("nameCBRBot")) {
                    String nameCBRBot = rawDataRow.get(j + 1);
                    nameCBRBotList.add(nameCBRBot);
                }

                // Executed Plans of CBR-Bot & Script-Bot
                if (rawDataRow.get(j).contains("planCBRBot")) {
                    String planCBRBot = rawDataRow.get(j + 1);
                    planCBRBotList.add(planCBRBot);

                    if (planCBRBot.contains("Camp")) {
                        campCountCBR++;
                    }
                    if (planCBRBot.contains("CollectItem")) {
                        collectItemCountCBR++;
                    }
                    if (planCBRBot.contains("MoveToEnemy")) {
                        moveToEnemyCountCBR++;
                    }
                    if (planCBRBot.contains("Reload")) {
                        reloadCountCBR++;
                    }
                    if (planCBRBot.contains("Seek")) {
                        seekCountCBR++;
                    }
                    if (planCBRBot.contains("Shoot")) {
                        shootCountCBR++;
                    }
                    if (planCBRBot.contains("SwitchWeapon")) {
                        switchWeaponCountCBR++;
                    }
                    if (planCBRBot.contains("UseCover")) {
                        useCoverCountCBR++;
                    }
                }

                if (rawDataRow.get(j).contains("planScriptBot")) {
                    String planScriptBot = rawDataRow.get(j + 1);
                    planScriptBotList.add(planScriptBot);

                    if (planScriptBot.contains("Camp")) {
                        campCountScript++;
                    }
                    if (planScriptBot.contains("CollectItem")) {
                        collectItemCountScript++;
                    }
                    if (planScriptBot.contains("MoveToEnemy")) {
                        moveToEnemyCountScript++;
                    }
                    if (planScriptBot.contains("Reload")) {
                        reloadCountScript++;
                    }
                    if (planScriptBot.contains("Seek")) {
                        seekCountScript++;
                    }
                    if (planScriptBot.contains("Shoot")) {
                        shootCountScript++;
                    }
                    if (planScriptBot.contains("SwitchWeapon")) {
                        switchWeaponCountScript++;
                    }
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

            }

        }

        // insert counters to list for chart
        counters.add(campCountCBR);
        counters.add(collectItemCountCBR);
        counters.add(moveToEnemyCountCBR);
        counters.add(reloadCountCBR);
        counters.add(seekCountCBR);
        counters.add(shootCountCBR);
        counters.add(switchWeaponCountCBR);
        counters.add(useCoverCountCBR);

        counters.add(campCountScript);
        counters.add(collectItemCountScript);
        counters.add(moveToEnemyCountScript);
        counters.add(reloadCountScript);
        counters.add(seekCountScript);
        counters.add(shootCountScript);
        counters.add(switchWeaponCountScript);

        // set table entrys
        for (int k = 0; k < frameCount; k++) {
            TableEntryStatisticsVisab tableEntryStatisticsVisab = new TableEntryStatisticsVisab();

            tableEntryStatisticsVisab.setFrame(k);

            tableEntryStatisticsVisab.setCoordinatesCBRBot(coordinatesCBRBotList.get(k));
            tableEntryStatisticsVisab.setCoordinatesScriptBot(coordinatesScriptBotList.get(k));

            tableEntryStatisticsVisab.setHealthCBRBot(healthCBRBotList.get(k));
            tableEntryStatisticsVisab.setHealthScriptBot(healthScriptBotList.get(k));

            tableEntryStatisticsVisab.setWeaponCBRBot(weaponCBRBotList.get(k));
            tableEntryStatisticsVisab.setWeaponScriptBot(weaponScriptBotList.get(k));

            tableEntryStatisticsVisab.setStatisticsCBRBot(statisticsCBRBotList.get(k));
            tableEntryStatisticsVisab.setStatisticsScriptBot(statisticsScriptBotList.get(k));

            tableEntryStatisticsVisab.setNameCBRBot(nameCBRBotList.get(k));
            tableEntryStatisticsVisab.setNameScriptBot(nameScriptBotList.get(k));

            tableEntryStatisticsVisab.setPlanCBRBot(planCBRBotList.get(k));

            tableEntryStatisticsVisab.setWeaponMagAmmuCBRBot(weaponMagAmmuCBRBotList.get(k));
            tableEntryStatisticsVisab.setWeaponMagAmmuScriptBot(weaponMagAmmuScriptBotList.get(k));

            tableEntryStatisticsVisab.setHealthPosition(healthPositionList.get(k));
            tableEntryStatisticsVisab.setWeaponPosition(weaponPositionList.get(k));
            tableEntryStatisticsVisab.setAmmuPosition(ammuPositionList.get(k));

            tableEntryStatisticsVisab.setRoundCounter(roundCounterList.get(k));

            statisticsTable.getItems().add(tableEntryStatisticsVisab);
        }

        return counters;

    }

    @SuppressWarnings("unchecked")
    private void createPlanChartCBRBot(List<Integer> calculatedCounters) {

        planChartCBRBot.getData().clear();

        xAxisPlanChartCBRBot.setCategories(FXCollections.<String>observableArrayList(Arrays.asList("Camp",
                "CollectItem", "MoveToEnemy", "Reload", "Seek", "Shoot", "SwitchWeapon", "UseCover")));
        xAxisPlanChartCBRBot.setLabel("Plan");

        yAxisPlanChartCBRBot.setLabel("Number of Executions");

        planChartCBRBot.setTitle("Plan Chart CBR-Bot");

        int sum = VISABUtil.sumIntegers(calculatedCounters.get(0), calculatedCounters.get(1), calculatedCounters.get(2),
                calculatedCounters.get(3), calculatedCounters.get(4), calculatedCounters.get(5),
                calculatedCounters.get(6), calculatedCounters.get(7));

        XYChart.Series<String, Number> data = new XYChart.Series<>();
        data.setName("Total Number of Executions " + sum);
        data.getData().add(new XYChart.Data<>("Camp", calculatedCounters.get(0)));
        data.getData().add(new XYChart.Data<>("CollectItem", calculatedCounters.get(1)));
        data.getData().add(new XYChart.Data<>("MoveToEnemy", calculatedCounters.get(2)));
        data.getData().add(new XYChart.Data<>("Reload", calculatedCounters.get(3)));
        data.getData().add(new XYChart.Data<>("Seek", calculatedCounters.get(4)));
        data.getData().add(new XYChart.Data<>("Shoot", calculatedCounters.get(5)));
        data.getData().add(new XYChart.Data<>("SwitchWeapon", calculatedCounters.get(6)));
        data.getData().add(new XYChart.Data<>("UseCover", calculatedCounters.get(7)));

        planChartCBRBot.getData().addAll(data);

    }

    @SuppressWarnings("unchecked")
    private void createPlanChartScriptBot(List<Integer> calculatedCounters) {

        planChartScriptBot.getData().clear();

        xAxisPlanChartScriptBot.setCategories(FXCollections.<String>observableArrayList(
                Arrays.asList("Camp", "CollectItem", "MoveToEnemy", "Reload", "Seek", "Shoot", "SwitchWeapon")));
        xAxisPlanChartScriptBot.setLabel("Plan");

        yAxisPlanChartScriptBot.setLabel("Number of Executions");

        planChartScriptBot.setTitle("Plan Chart Script Bot");

        int sum = VISABUtil.sumIntegers(calculatedCounters.get(8), calculatedCounters.get(9),
                calculatedCounters.get(10), calculatedCounters.get(11), calculatedCounters.get(12),
                calculatedCounters.get(13), calculatedCounters.get(14));

        XYChart.Series<String, Number> data = new XYChart.Series<>();
        data.setName("Total Number of Executions " + sum);
        data.getData().add(new XYChart.Data<>("Camp", calculatedCounters.get(8)));
        data.getData().add(new XYChart.Data<>("CollectItem", calculatedCounters.get(9)));
        data.getData().add(new XYChart.Data<>("MoveToEnemy", calculatedCounters.get(10)));
        data.getData().add(new XYChart.Data<>("Reload", calculatedCounters.get(11)));
        data.getData().add(new XYChart.Data<>("Seek", calculatedCounters.get(12)));
        data.getData().add(new XYChart.Data<>("Shoot", calculatedCounters.get(13)));
        data.getData().add(new XYChart.Data<>("SwitchWeapon", calculatedCounters.get(14)));

        planChartScriptBot.getData().addAll(data);

    }

}
