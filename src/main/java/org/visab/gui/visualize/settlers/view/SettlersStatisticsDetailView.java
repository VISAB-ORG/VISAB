package org.visab.gui.visualize.settlers.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.gui.visualize.settlers.viewmodel.SettlersStatisticsViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart.Series;
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

        // resourceChart.dataProperty().get().addListener(new ListChangeListener<StackedBarChart.Series<String, Number>>(){
        //     @Override
        //     public void onChanged(Change<? extends Series<String, Number>> c) {
        //         resourceChart.setStyle("template_style.css");
        //     }
        // });


        sliderLabel.textProperty().bind(viewModel.sliderTextProperty());
        
        roundSlider.maxProperty().bindBidirectional(viewModel.sliderMaxProperty());
        roundSlider.setOnMouseReleased( event -> {
            viewModel.sliderValueProperty().setValue((int)roundSlider.getValue());
            viewModel.updateStackedBarChartCommand().execute();
        });
        
    }

}
