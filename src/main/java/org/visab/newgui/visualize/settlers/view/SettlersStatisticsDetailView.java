package org.visab.newgui.visualize.settlers.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.visab.newgui.visualize.settlers.viewmodel.SettlersStatisticsViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class SettlersStatisticsDetailView implements FxmlView<SettlersStatisticsViewModel>, Initializable {

    @FXML
    StackedBarChart<String, Number> resourceChart;

    @InjectViewModel
    SettlersStatisticsViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceChart.setData(viewModel.getPlayerDetailedStatisticsSeries());
        resourceChart.getYAxis().labelProperty().bind(viewModel.yLabelDetailProperty());

/*         Series<String, Number> resource = new Series<>();
        resource.setName("Wood");
        resource.getData().add(new XYChart.Data<>("1 - Player 1", 500));
        resource.getData().add(new XYChart.Data<>("1 - Player 2", 500));
        resource.getData().add(new XYChart.Data<>("2 - Player 1", 500));
        resource.getData().add(new XYChart.Data<>("2 - Player 2", 500));

        Series<String, Number> resource2 = new Series<>();
        resource2.setName("Stone");
        resource2.getData().add(new XYChart.Data<>("1 - Player 1", 500));
        resource2.getData().add(new XYChart.Data<>("1 - Player 2", 500));
        resource2.getData().add(new XYChart.Data<>("2 - Player 1", 500));
        resource2.getData().add(new XYChart.Data<>("2 - Player 2", 500));

        resourceChart.getData().add(resource);
        resourceChart.getData().add(resource2);
        resourceChart.getData().add(resource);
        resourceChart.getData().add(resource2); */
        System.out.println(viewModel);
    }

}
