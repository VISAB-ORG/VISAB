package org.visab.newgui.workspace;

import java.util.ResourceBundle;

import org.visab.newgui.AppMain;
import org.visab.newgui.controls.ExplorerFile;
import org.visab.newgui.controls.FileExplorer;
import org.visab.newgui.controls.RecursiveTreeItem;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.net.URL;

// TODO: Add error message when adding file fails
public abstract class ExplorerViewBase<TViewModel extends ExplorerViewModelBase>
        implements FxmlView<TViewModel>, Initializable {

    /**
     * The explorer view
     */
    @FXML
    FileExplorer fileExplorer;

    /**
     * Button for fully refreshing the explorer view
     */
    @FXML
    Button refreshButton;

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

    @InjectViewModel
    protected TViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileExplorer.addFileAddedHandler(f -> viewModel.addFile(f));
        fileExplorer.addRemoveFileHandler(ef -> viewModel.deleteFile(ef));
        // Refresh view completely
        refreshButton.setOnAction(e -> refreshExplorerView());
        refreshExplorerView();

        viewModel.selectedExplorerFileProperty().bind(fileExplorer.getSelectionModel().selectedItemProperty());

        // After the primaryStage.show() was called from AppMain.
        // Has to be called here, because the elements we want to reference, are only
        // loaded upon the stage being shown.
        AppMain.getPrimaryStage().setOnShowing(e -> {
            viewModel.getDialogHelper().setParentWindow(fileExplorer.getScene().getWindow());
        });

        afterInitialize(location, resources);
    }

    /**
     * Fully refreshes the explorer view. TODO: Should be a onaction in fxml
     */
    protected void refreshExplorerView() {
        fileExplorer.setRoot(null);

        var root = viewModel.getFreshBaseFile();
        var rootItem = new RecursiveTreeItem<ExplorerFile>(root, x -> x.getFiles());
        fileExplorer.setRoot(rootItem);

        var expandUntil = 15;
        fileExplorer.expandChildren(expandUntil);
    }

    /**
     * Called directly after initialize() is done
     * 
     * @param location  Forwarded from initialize
     * @param resources Forwarded from initialize
     */
    protected abstract void afterInitialize(URL location, ResourceBundle resources);
}
