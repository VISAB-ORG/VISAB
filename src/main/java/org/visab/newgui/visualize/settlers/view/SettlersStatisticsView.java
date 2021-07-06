package org.visab.newgui.visualize.settlers.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.settlers.viewmodel.SettlersStatisticsViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.value.ObservableValue;

public class SettlersStatisticsView implements FxmlView<SettlersStatisticsViewModel>, Initializable {

    @FXML
    TableView<ComparisonRowBase<?>> comparisonStatistics;

    @InjectViewModel
    SettlersStatisticsViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        comparisonStatistics.setItems(viewModel.getComparisonStatistics());

        viewModel.selectedRowProperty().bind(comparisonStatistics.getSelectionModel().selectedItemProperty());

        var columns = createComparisonColumns();
        comparisonStatistics.getColumns().addAll(columns);
    }

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
