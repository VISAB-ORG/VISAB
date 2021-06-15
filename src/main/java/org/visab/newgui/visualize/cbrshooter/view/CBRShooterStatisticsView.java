package org.visab.newgui.visualize.cbrshooter.view;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import org.visab.newgui.control.CustomLabelPieChart;
import org.visab.newgui.visualize.cbrshooter.model.CBRShooterStatisticsRow;
import org.visab.newgui.visualize.cbrshooter.viewmodel.CBRShooterStatisticsViewModel;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class CBRShooterStatisticsView implements FxmlView<CBRShooterStatisticsViewModel>, Initializable {

    @FXML
    TableView<CBRShooterStatisticsRow> overviewTable;

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
        planUsageCBR.setData(viewModel.getPlanUsageCBR());
        planUsageScript.setData(viewModel.getPlanUsageScript());
        // TODO: remove this
        overviewTable.setItems(viewModel.getOverviewStatistics());

        snapshotsPerSecond.textProperty().bind(viewModel.snapshotPerSecondProperty().asString());

        // Set the label format for pie charts
        var df = new DecimalFormat("#.##");
        planUsageCBR.setLabelFormat(d -> d.getName() + " " + df.format(d.getPieValue()) + "s");
        planUsageScript.setLabelFormat(d -> d.getName() + " " + df.format(d.getPieValue()) + "s");

        playerKills.setData(viewModel.getPlayerKillsSeries());

        // TODO: remove this
        overviewTable.setItems(viewModel.getOverviewStatistics());

    }

}
