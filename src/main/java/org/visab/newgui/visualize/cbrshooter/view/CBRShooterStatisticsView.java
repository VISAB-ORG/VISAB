package org.visab.newgui.visualize.cbrshooter.view;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

import org.visab.newgui.control.CustomLabelPieChart;
import org.visab.newgui.visualize.cbrshooter.model.ComparisonRowBase;
import org.visab.newgui.visualize.cbrshooter.viewmodel.CBRShooterStatisticsViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class CBRShooterStatisticsView implements FxmlView<CBRShooterStatisticsViewModel>, Initializable {

    @FXML
    TableView<ComparisonRowBase<?>> comparisonTable;

    @FXML
    CustomLabelPieChart planUsageCBR;

    @FXML
    CustomLabelPieChart planUsageScript;

    @FXML
    LineChart<Double, Integer> playerKills;

    @FXML
    Label snapshotsPerSecond;

    @InjectViewModel
    CBRShooterStatisticsViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        snapshotsPerSecond.textProperty().bind(viewModel.snapshotPerSecondProperty().asString());

        planUsageCBR.setData(viewModel.getPlanUsageCBR());
        planUsageScript.setData(viewModel.getPlanUsageScript());

        comparisonTable.setItems(viewModel.getComparisonStatistics());
        comparisonTable.getColumns().addAll(createColumns());

        // Set the label format for pie charts
        var df = new DecimalFormat("#.##");
        planUsageCBR.setLabelFormat(d -> d.getName() + " " + df.format(d.getPieValue()) + "s");
        planUsageScript.setLabelFormat(d -> d.getName() + " " + df.format(d.getPieValue()) + "s");

        playerKills.setData(viewModel.getPlayerKillsSeries());
    }

    private List<TableColumn<ComparisonRowBase<?>, ?>> createColumns() {
        var playerNames = viewModel.getPlayerNames();

        for (int i = 0; i < playerNames.size() ; i++) {
            var name = playerNames.get(i);
            var column = new TableColumn<ComparisonRowBase<?>, ObservableValue<?>>(name);
            column.setCellValueFactory(createListValueFactory(row -> row.getPlayerValues(), i));
        }
    }

    private <T> Callback<CellDataFeatures<ComparisonRowBase<T>, T>, ObservableValue<T>> createListValueFactory(
            Function<ComparisonRowBase<T>, List<T>> valueFunc, int index) {
        if (index < 0) {
            return cd -> null;
        } else {
            return cd -> {
                var values = valueFunc.apply(cd.getValue());
                return values == null || values.size() <= index ? null : new SimpleObjectProperty<>(values.get(index));
            };
        }
    }

}
