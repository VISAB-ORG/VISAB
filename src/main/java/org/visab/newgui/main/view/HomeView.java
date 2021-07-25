package org.visab.newgui.main.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.AppMain;
import org.visab.newgui.control.ExplorerFile;
import org.visab.newgui.control.FileExplorer;
import org.visab.newgui.control.RecursiveTreeItem;
import org.visab.newgui.main.viewmodel.HomeViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.utils.notifications.NotificationObserver;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class HomeView implements FxmlView<HomeViewModel>, Initializable {

    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu homeMenu;
    @FXML
    private MenuItem homeMenuItem;
    @FXML
    private Menu apiMenu;
    @FXML
    private MenuItem apiMenuItem;
    @FXML
    private Menu settingsMenu;
    @FXML
    private MenuItem settingsMenuItem;
    @FXML
    private Menu aboutMenu;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private Menu helpMenu;
    @FXML
    private MenuItem helpMenuItem;
    @FXML
    private Button uploadButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button visualizeButton;
    @FXML
    private Button refreshButton;

    // Deprecated VISAB 1.0 GUI code @TODO: delete this later on
    // ----- Command class variables -----
    @FXML
    private Menu oldGui;

    @FXML
    private MenuItem statisticsViewer;

    @FXML
    private MenuItem pathViewer;

    @FXML
    private MenuItem help;

    @FXML
    private MenuItem about;

    @FXML
    public void openMain() {
        viewModel.openMain().execute();
    }

    @FXML
    public void openStatisticsViewer() {
        viewModel.openStatisticsViewer().execute();
    }

    @FXML
    public void openPathViewer() {
        viewModel.openPathViewer().execute();
    }

    @FXML
    public void openHelp() {
        viewModel.openHelp().execute();
    }

    @FXML
    public void openAbout() {
        viewModel.openAbout().execute();
    }
    // ----- End of deprecated VISAB 1.0 GUI code -----

    @InjectViewModel
    private HomeViewModel viewModel;

    @FXML
    public void openApi() {
        viewModel.openApi().execute();
    }

    @FXML
    public void openSettings() {
        viewModel.openSettings().execute();
    }
    
    @FXML
    public void openNewAbout() {
        viewModel.openNewAbout().execute();
    }
    
    @FXML
    public void openNewHelp() {
        viewModel.openNewHelp().execute();
    }

    /**
     * The explorer view
     */
    @FXML
    FileExplorer fileExplorer;

    @FXML
    public void deleteFileAction() {
        viewModel.deleteSelectedFileCommand().execute();
    }

    @FXML
    public void showInExplorerAction() {
        viewModel.showInExplorerCommand().execute();
    }

    @FXML
    public void renameFileAction() {
        viewModel.renameFileCommand().execute();
        fileExplorer.refresh();
    }

    @FXML
    public void addFilesAction() {
        viewModel.addFileCommand().execute();
    }

    @FXML
    public void visualizeAction() {
        viewModel.visualizeCommand().execute();
    }

    /**
     * Fully refreshes the file explorer, by reloading the os file structure.
     */
    @FXML
    public void refreshFileExplorer() {
        fileExplorer.setRoot(null);

        var root = viewModel.getFreshBaseFile();
        var rootItem = new RecursiveTreeItem<ExplorerFile>(root, x -> x.getFiles());
        fileExplorer.setBaseFile(rootItem);

        var expandUntil = 15;
        fileExplorer.expandChildren(expandUntil);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel.subscribe("FILE_ADDED", (e, o) -> refreshFileExplorer());

        refreshFileExplorer();

        fileExplorer.addFileAddedHandler(f -> viewModel.addFile(f));
        fileExplorer.addRemoveFileHandler(ef -> viewModel.deleteFile(ef));

        viewModel.selectedExplorerFileProperty().bind(fileExplorer.getSelectionModel().selectedItemProperty());
    }

}
