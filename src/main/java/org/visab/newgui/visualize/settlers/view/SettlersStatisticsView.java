package org.visab.newgui.visualize.settlers.view;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.visab.newgui.control.CustomLabelPieChart;
import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.settlers.viewmodel.SettlersStatisticsViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.beans.value.ObservableValue;

public class SettlersStatisticsView implements FxmlView<SettlersStatisticsViewModel>, Initializable {

    @FXML
    TableView<ComparisonRowBase<?>> comparisonStatistics;

    @InjectViewModel
    SettlersStatisticsViewModel viewModel;

    @FXML
    HBox planUsageHBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comparisonStatistics.setItems(viewModel.getComparisonStatistics());

        viewModel.selectedRowProperty().bind(comparisonStatistics.getSelectionModel().selectedItemProperty());

        addComparisonColumns();
        addPlanPieCharts();
    }

    private void addPlanPieCharts() {
        for (String name : viewModel.getPlayerNames()) {
            var pieChart = new CustomLabelPieChart();
            pieChart.setTitle(name + " Plan Usage");
            pieChart.setLabelFormat(d -> d.getName() + " " + (int) d.getPieValue() + " uses");
            pieChart.setData(viewModel.getPlanUsages().get(name));
            planUsageHBox.getChildren().add(pieChart);

            // TODO: Set legend visible somehow
        }
    }

    private void addComparisonColumns() {
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

        comparisonStatistics.getColumns().addAll(columns);
    }

}
