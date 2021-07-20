package org.visab.newgui.visualize.cbrshooter.view;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.visab.newgui.control.CustomLabelPieChart;
import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.cbrshooter.viewmodel.CBRShooterStatisticsViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CBRShooterStatisticsView implements FxmlView<CBRShooterStatisticsViewModel>, Initializable {

    @FXML
    TableView<ComparisonRowBase<?>> comparisonTable;

    @FXML
    CustomLabelPieChart planUsageOne;

    @FXML
    CustomLabelPieChart planUsageTwo;

    @FXML
    LineChart<Integer, Number> playerStats;

    @FXML
    Label snapshotsPerSecond;

    @InjectViewModel
    CBRShooterStatisticsViewModel viewModel;

    @FXML
    private void handleChartButtonAction() {
        viewModel.playerStatsChartCommand().execute();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize pie charts
        // TODO: Do like in settlers
        if (viewModel.getPlayerNames().size() == 2) {
            var name = viewModel.getPlayerNames().get(0);
            planUsageOne.setData(viewModel.getPlanUsage(name));
            planUsageOne.setTitle(name + " Plan Usage");

            name = viewModel.getPlayerNames().get(1);
            planUsageTwo.setData(viewModel.getPlanUsage(name));
            planUsageTwo.setTitle(name + " Plan Usage");

            // Set the label format for pie charts
            var df = new DecimalFormat("#.##");
            planUsageOne.setLabelFormat(d -> d.getName() + " " + df.format(d.getPieValue()) + "s");
            planUsageTwo.setLabelFormat(d -> d.getName() + " " + df.format(d.getPieValue()) + "s");
        }

        comparisonTable.setItems(viewModel.getComparisonStatistics());

        var columns = createComparisonColumns();
        comparisonTable.getColumns().addAll(columns);

        // playerStats chart
        viewModel.selectedStatisticsProperty().bind(comparisonTable.getSelectionModel().selectedItemProperty());
        playerStats.setData(viewModel.getPlayerStatsSeries());
        playerStats.getYAxis().labelProperty().bind(viewModel.yLabelProperty());

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
    }

    @SuppressWarnings("unchecked")
    private List<TableColumn<ComparisonRowBase<?>, ?>> createComparisonColumns() {
        var columns = new ArrayList<TableColumn<ComparisonRowBase<?>, ?>>();

        var playerNames = viewModel.getPlayerNames();
        for (int i = 0; i < playerNames.size(); i++) {
            var name = playerNames.get(i);
            var column = new TableColumn<ComparisonRowBase<?>, Object>(name);

            // Create cell value factory
            column.setCellValueFactory(
                    cellData -> (ObservableValue<Object>) cellData.getValue().getPlayerValues().get(name));
            columns.add(column);
        }

        return columns;
    }

}
