package org.visab.newgui.visualize.settlers.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.settlers.viewmodel.SettlersStatisticsViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;

public class SettlersStatisticsDetailView implements FxmlView<SettlersStatisticsViewModel>, Initializable {

    @FXML
    TableView<ComparisonRowBase<?>> detailStatistics;

    @FXML
    LineChart<Integer, Number> detailedPlayerStatistics;

    @InjectViewModel
    SettlersStatisticsViewModel viewModel;

    @FXML
    private void handleChartButtonAction() {
        viewModel.detailedStatisticsChartCommand().execute();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        detailStatistics.setItems(viewModel.getDetailedStatistics());

        viewModel.selectedRowProperty().bind(detailStatistics.getSelectionModel().selectedItemProperty());
        detailedPlayerStatistics.setData(viewModel.getPlayerDetailedStatisticsSeries());
        detailedPlayerStatistics.getYAxis().labelProperty().bind(viewModel.yLabelProperty());
        
        addComparisonColumns();

        detailedPlayerStatistics.dataProperty().get().addListener(new ListChangeListener<LineChart.Series<Integer, Number>>(){
            
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

                detailedPlayerStatistics.setStyle(style);
             }
        });
    }

    @SuppressWarnings("unchecked")
    private void addComparisonColumns() {
        var columns = new ArrayList<TableColumn<ComparisonRowBase<?>, ?>>();

        var playerNames = viewModel.getPlayerNames();
        for (int i = 0; i < playerNames.size(); i++) {
            var name = playerNames.get(i);
            var type = viewModel.getPlayerInformation().get(name);
            var column = new TableColumn<ComparisonRowBase<?>, Object>(name + " (" + type + ")");

            // Create cell value factory
            column.setCellValueFactory(
                    cellData -> (ObservableValue<Object>) cellData.getValue().getPlayerValues().get(name));
            columns.add(column);
        }

        detailStatistics.getColumns().addAll(columns);
    }

}
