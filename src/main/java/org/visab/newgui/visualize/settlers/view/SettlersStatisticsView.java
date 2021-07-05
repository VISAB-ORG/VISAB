package org.visab.newgui.visualize.settlers.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.visualize.ComparisonRowBase;
import org.visab.newgui.visualize.settlers.viewmodel.SettlersStatisticsViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

public class SettlersStatisticsView implements FxmlView<SettlersStatisticsViewModel>, Initializable {

    @FXML
    TableView<ComparisonRowBase<?>> comparisonStatistics;

    @InjectViewModel
    SettlersStatisticsViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        comparisonStatistics.setItems(viewModel.getComparisonStatistics());
    }

}
