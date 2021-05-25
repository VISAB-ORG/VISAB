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
    PieChart planUsage;

    @InjectViewModel
    CBRShooterStatisticsViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        planUsage.setData(viewModel.getPlanUsageCBR());

        overviewTable.setItems(viewModel.getOverviewStatistics());

    }

}
