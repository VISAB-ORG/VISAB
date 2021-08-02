package org.visab.newgui.visualize.settlers.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.visualize.settlers.viewmodel.SettlersStatisticsViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.Initializable;

public class SettlersStatisticsDetailView implements FxmlView<SettlersStatisticsViewModel>, Initializable {
    
    @InjectViewModel
    SettlersStatisticsViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
    }

}
