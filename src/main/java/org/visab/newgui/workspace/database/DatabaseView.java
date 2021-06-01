package org.visab.newgui.workspace.database;

import java.net.URL;
import java.util.ResourceBundle;

import org.visab.newgui.AppMain;
import org.visab.newgui.control.ExplorerFile;
import org.visab.newgui.control.FileExplorer;
import org.visab.newgui.control.RecursiveTreeItem;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class DatabaseView implements FxmlView<DatabaseViewModel>, Initializable {
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
    }

    @FXML
    public void addFilesAction() {
        viewModel.addFileCommand().execute();
    }

    /**
     * Fully refreshes the file explorer.
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

    @InjectViewModel
    protected DatabaseViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshFileExplorer();

        fileExplorer.addFileAddedHandler(f -> viewModel.addFile(f));
        fileExplorer.addRemoveFileHandler(ef -> viewModel.deleteFile(ef));

        viewModel.selectedExplorerFileProperty().bind(fileExplorer.getSelectionModel().selectedItemProperty());

        // After the primaryStage.show() was called from AppMain.
        // Has to be called here, because the elements we want to reference, are only
        // loaded upon the stage being shown.
        AppMain.getPrimaryStage().setOnShowing(e -> {
            viewModel.getDialogHelper().setParentWindow(fileExplorer.getScene().getWindow());
        });
    }

}
