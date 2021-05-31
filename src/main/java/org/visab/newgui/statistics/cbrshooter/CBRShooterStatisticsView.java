package org.visab.newgui.statistics.cbrshooter;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import org.visab.newgui.controls.CustomLabelPieChart;
import org.visab.newgui.statistics.cbrshooter.model.CBRShooterStatisticsRow;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

public class CBRShooterStatisticsView implements FxmlView<CBRShooterStatisticsViewModel>, Initializable {

    @FXML
    TableView<CBRShooterStatisticsRow> overviewTable;

    @FXML
    CustomLabelPieChart planUsageCBR;

    @FXML
    CustomLabelPieChart planUsageScript;

    @InjectViewModel
    CBRShooterStatisticsViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        planUsageCBR.setData(viewModel.getPlanUsageCBR());
        planUsageScript.setData(viewModel.getPlanUsageScript());
        // TODO: remove this
        overviewTable.setItems(viewModel.getOverviewStatistics());

        // Set the label format for pie charts
        var df = new DecimalFormat("#.##");
        planUsageCBR.setLabelFormat(d -> d.getName() + " " + df.format(d.getPieValue()) + "s");
        planUsageScript.setLabelFormat(d -> d.getName() + " " + df.format(d.getPieValue()) + "s");
    }

}
