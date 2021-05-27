package org.visab.newgui.statistics.cbrshooter;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.statistics.cbrshooter.model.CBRShooterStatisticsRow;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableView;

public class CBRShooterStatisticsView implements FxmlView<CBRShooterStatisticsViewModel>, Initializable {

    @FXML
    TableView<CBRShooterStatisticsRow> overviewTable;

    @FXML
    PieChart planUsageCBR;

    @FXML
    PieChart planUsageScript;

    @InjectViewModel
    CBRShooterStatisticsViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        planUsageCBR.setData(viewModel.getPlanUsageCBR());
        planUsageScript.setData(viewModel.getPlanUsageScript());
        // TODO: remove this
        overviewTable.setItems(viewModel.getOverviewStatistics());
    }

}
