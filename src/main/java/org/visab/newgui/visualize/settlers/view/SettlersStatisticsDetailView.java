package org.visab.newgui.visualize.settlers.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.visualize.settlers.viewmodel.SettlersStatisticsViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class SettlersStatisticsDetailView implements FxmlView<SettlersStatisticsViewModel>, Initializable {

    @FXML
    StackedBarChart<String, Number> resourceChart;

    @FXML
    Slider roundSlider;

    @FXML
    Label sliderLabel;

    @InjectViewModel
    SettlersStatisticsViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceChart.setData(viewModel.getPlayerDetailedStatisticsSeries());
        resourceChart.getYAxis().labelProperty().bind(viewModel.yLabelDetailProperty());

        sliderLabel.textProperty().bind(viewModel.sliderTextProperty());
        
        roundSlider.setOnMouseReleased( event -> {
            viewModel.sliderValueProperty().setValue((int)roundSlider.getValue());
            viewModel.updateStackedBarChartCommand().execute();
        });
        
    }

}
