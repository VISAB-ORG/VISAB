package org.visab.gui.visualize.settlers.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.gui.visualize.settlers.viewmodel.SettlersStatisticsViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
        resourceChart.getStylesheets().addAll("template_style.css");
      
        // var darkModeOn = Workspace.getInstance().getConfigManager().isDarkModeOn();

        sliderLabel.textProperty().bind(viewModel.sliderTextProperty());
        
        roundSlider.maxProperty().bindBidirectional(viewModel.sliderMaxProperty());
        roundSlider.setMin(1);
        roundSlider.setBlockIncrement(10);
        roundSlider.setMajorTickUnit(10);
        roundSlider.setMinorTickCount(0);
        roundSlider.setSnapToTicks(true);
        roundSlider.setOnMouseReleased( event -> {
            viewModel.sliderValueProperty().setValue((int)roundSlider.getValue());
            viewModel.updateStackedBarChartCommand().execute();
        });
        
    }

}
