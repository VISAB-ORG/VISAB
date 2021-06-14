package org.visab.newgui.visualize.starter.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.globalmodel.starter.DefaultFile;
import org.visab.newgui.AppMain;
import org.visab.newgui.DynamicViewLoader;
import org.visab.newgui.UiHelper;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class DefaultStatisticsView implements FxmlView<DefaultStatisticsViewModel>, Initializable {

    @FXML
    ListView<TreeItem<String>> statisticsListView;

    @FXML
    TreeView<String> jsonView;

    @InjectViewModel
    DefaultStatisticsViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        statisticsListView.setItems(viewModel.getStatistics());
        statisticsListView.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<TreeItem<String>>() {

                    @Override
                    public void changed(ObservableValue<? extends TreeItem<String>> observable,
                            TreeItem<String> oldValue, TreeItem<String> newValue) {
                        jsonView.setRoot(newValue);
                    }
                });
    }

    public static void main(String[] args) {
        new Thread(() -> Application.launch(AppMain.class)).start();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            //TODO: handle exception
        }
        
        UiHelper.inovkeOnUiThread(() -> {
            var tuple = FluentViewLoader.fxmlView(DefaultStatisticsView.class).load();
        
            String inputJson = "{\"name\":\"Jake\",\"salary\":3000,\"phones\":"
            + "[{\"phoneType\":\"cell\",\"phoneNumber\":\"111-111-111\"},"
            + "{\"phoneType\":\"work\",\"phoneNumber\":\"222-222-222\"}],"
            +"\"taskIds\":[11,22,33],"
            + "\"address\":{\"street\":\"101 Blue Dr\",\"city\":\"White Smoke\"}}";
    
            var defaultFile = new DefaultFile("test");
            defaultFile.getStatistics().add(inputJson);
            defaultFile.getStatistics().add(inputJson);
            defaultFile.getStatistics().add(inputJson);
    
            var viewmodel = tuple.getViewModel();
            viewmodel.initialize(defaultFile);
    
            var stage = new Stage();
            stage.setTitle("title");
            stage.setScene(new Scene(tuple.getView()));
            stage.show();
        });
    }

}
