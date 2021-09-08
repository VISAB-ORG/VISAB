package org.visab.gui.main.view;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import org.visab.gui.ResourceHelper;
import org.visab.gui.control.ExplorerFile;
import org.visab.gui.control.FileExplorer;
import org.visab.gui.control.RecursiveTreeItem;
import org.visab.gui.main.viewmodel.HomeViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private ToggleButton darkModeOn;
    @FXML
    private Menu aboutMenu;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private Menu helpMenu;
    @FXML
    private MenuItem helpMenuItem;
    @FXML
    private MenuItem onlineDocuItem;
    @FXML
    private Button uploadButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button visualizeButton;
    @FXML
    private Button refreshButton;
    @FXML
    private ImageView visabLogo;

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

    @FXML
    public void openOnlineDoc() {
        try {
            Desktop.getDesktop().browse(new URI("https://visab-org.github.io/index.html"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        darkModeOn.selectedProperty().bindBidirectional(viewModel.darkModeOnProperty());
        if (darkModeOn.isSelected()) {
            darkModeOn.setText("on");
        } else {
            darkModeOn.setText("off");
        }
        darkModeOn.selectedProperty().addListener(e -> {
            if (darkModeOn.isSelected()) {
                // Put dark mode CSS
                darkModeOn.setText("on");
                viewModel.changeColorScheme(true);
                visabLogo.setImage(new Image(ResourceHelper.IMAGE_PATH + "/VISAB_Logo_white.png"));
            } else {
                // Put Light Mode CSS
                darkModeOn.setText("off");
                viewModel.changeColorScheme(false);
                visabLogo.setImage(new Image(ResourceHelper.IMAGE_PATH + "/visabLogo.png"));
            }
        });

        if (darkModeOn.isSelected()) {
            visabLogo.setImage(new Image(ResourceHelper.IMAGE_PATH + "/VISAB_Logo_white.png"));
        } else {
            visabLogo.setImage(new Image(ResourceHelper.IMAGE_PATH + "/visabLogo.png"));
        }

        refreshFileExplorer();

        fileExplorer.addFileAddedHandler(f -> viewModel.addFile(f));
        fileExplorer.addRemoveFileHandler(ef -> viewModel.deleteFile(ef));

        viewModel.selectedExplorerFileProperty().bind(fileExplorer.getSelectionModel().selectedItemProperty());
    }

}
