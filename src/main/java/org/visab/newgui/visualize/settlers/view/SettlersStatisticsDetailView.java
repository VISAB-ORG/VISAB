package org.visab.newgui.visualize.settlers.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.visualize.settlers.viewmodel.SettlersStatisticsViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Slider;

public class SettlersStatisticsDetailView implements FxmlView<SettlersStatisticsViewModel>, Initializable {

    @FXML
    StackedBarChart<String, Number> resourceChart;

    @FXML
    Slider roundSlider;

    @InjectViewModel
    SettlersStatisticsViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceChart.setData(viewModel.getPlayerDetailedStatisticsSeries());
        resourceChart.getYAxis().labelProperty().bind(viewModel.yLabelDetailProperty());

        roundSlider.setOnMouseReleased( event -> {
            viewModel.sliderValueProperty().setValue((int)roundSlider.getValue());
            viewModel.updateStackedBarChartCommand().execute();
        });
        
    }

}