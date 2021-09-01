package org.visab.gui.visualize.starter.view;

import java.net.URL;
import java.util.ResourceBundle;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class DefaultStatisticsView implements FxmlView<DefaultStatisticsViewModel>, Initializable {

    @FXML
    ListView<TreeItem<String>> statisticsListView;

    @FXML
    TreeView<String> jsonView;

    @InjectViewModel
    DefaultStatisticsViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statisticsListView.setItems(viewModel.getStatistics());
        viewModel.selectedStatisticsProperty().bind(statisticsListView.getSelectionModel().selectedItemProperty());
        viewModel.selectedStatisticsProperty().addListener(new ChangeListener<TreeItem<String>>() {

            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue,
                    TreeItem<String> newValue) {
                jsonView.setRoot(newValue);
            }
        });
    }

}
