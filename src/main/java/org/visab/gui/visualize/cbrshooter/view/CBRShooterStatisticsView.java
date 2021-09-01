package org.visab.gui.visualize.cbrshooter.view;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.visab.gui.control.CustomLabelPieChart;
import org.visab.gui.visualize.ComparisonRowBase;
import org.visab.gui.visualize.cbrshooter.viewmodel.CBRShooterStatisticsViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public class CBRShooterStatisticsView implements FxmlView<CBRShooterStatisticsViewModel>, Initializable {

    @FXML
    TableView<ComparisonRowBase<?>> comparisonTable;

    @FXML
    HBox planUsageHBox;

    @FXML
    LineChart<Integer, Number> playerStats;

    @FXML
    Label snapshotsPerSecond;

    @FXML
    CheckBox isLiveViewActive;

    @InjectViewModel
    CBRShooterStatisticsViewModel viewModel;

    @FXML
    private void handleChartButtonAction() {
        viewModel.playerStatsChartCommand().execute();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize pie charts
        addPlanPieCharts();

        comparisonTable.setItems(viewModel.getComparisonStatistics());
        var columns = createComparisonColumns();
        comparisonTable.getColumns().addAll(columns);

        // playerStats chart
        viewModel.selectedStatisticsProperty().bind(comparisonTable.getSelectionModel().selectedItemProperty());
        playerStats.setData(viewModel.getPlayerStatsSeries());
        playerStats.getYAxis().labelProperty().bind(viewModel.graphYLabelProperty());

        playerStats.dataProperty().get().addListener(new ListChangeListener<LineChart.Series<Integer, Number>>() {
            @Override
            public void onChanged(Change<? extends Series<Integer, Number>> c) {
                var colors = new ArrayList<String>(viewModel.getPlayerColors().values());

                String style = "";
                for (int i = 0; i < colors.size(); i++) {
                    style += "CHART_COLOR_" + (i + 1) + ": " + colors.get(i) + ";";
                    // When deleting old series and adding new ones sometimes a new line color is
                    // chosen. For two players the new ones would be 3 and 4, for 3 players 4, 5, 6.
                    style += "CHART_COLOR_" + (i + 1 + colors.size()) + ": " + colors.get(i) + ";";
                }

                playerStats.setStyle(style);
            }
        });

        isLiveViewActive.disableProperty().set(true);
        isLiveViewActive.selectedProperty().bind(viewModel.liveViewActiveProperty());
        isLiveViewActive.setVisible(viewModel.liveViewActiveProperty().get());
    }

    @SuppressWarnings("unchecked")
    private List<TableColumn<ComparisonRowBase<?>, ?>> createComparisonColumns() {
        var columns = new ArrayList<TableColumn<ComparisonRowBase<?>, ?>>();

        var playerNames = viewModel.getPlayerNames();
        for (int i = 0; i < playerNames.size(); i++) {
            var name = playerNames.get(i);
            var type = viewModel.getPlayerInformation().get(name);
            var column = new TableColumn<ComparisonRowBase<?>, Object>(name + " (" + type + ")");

            // Create cell value factory
            column.setCellValueFactory(
                    cellData -> (ObservableValue<Object>) cellData.getValue().getPlayerProperties().get(name));
            columns.add(column);
        }

        return columns;
    }

    private void addPlanPieCharts() {
        for (String name : viewModel.getPlayerNames()) {
            var pieChart = new CustomLabelPieChart();
            pieChart.setTitle(name + " Plan Usage");
            var df = new DecimalFormat("#.##");
            pieChart.setLabelFormat(d -> d.getName() + " " + df.format(d.getPieValue()) + "s");
            pieChart.setData(viewModel.getPlanUsage(name));
            planUsageHBox.getChildren().add(pieChart);
        }
    }

}
