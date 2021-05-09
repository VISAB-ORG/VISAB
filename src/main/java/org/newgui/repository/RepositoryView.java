package org.newgui.repository;

import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import org.newgui.repository.model.FileRow;
import org.visab.util.VISABUtil;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

public class RepositoryView implements FxmlView<RepositoryViewModel>, Initializable {

    /**
     * Graphics for FileView
     */
    private static Image fileImage = new Image("/repository/file.png", 16, 16, false, false);

    private static Image folderClosedImage = new Image("/repository/folder_closed.png", 16, 16, false, false);

    private static Image folderOpenImage = new Image("/repository/folder_open.png", 16, 16, false, false);

    private static Image visabFileImage = new Image("/repository/visab_file.png", 16, 16, false, false);

    @FXML
    Button addButton;

    @FXML
    TreeTableView<FileRow> fileView;

    @FXML
    TreeTableColumn<FileRow, String> nameColumn;

    @FXML
    Button refreshButton;

    @FXML
    Button deleteButton;

    @InjectViewModel
    RepositoryViewModel viewModel;

    private Command deleteFileCommand;

    @FXML
    public void deleteFileAction() {
        deleteFileCommand.execute();
    }

    private void addFileDialog(ActionEvent e) {
        var initialDir = Path.of(VISABUtil.getRunningJarRootDirPath());
        var parentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        var files = new VISABFileDialog().getFiles(initialDir, parentStage);

        for (var file : files)
            viewModel.addFile(file);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Non MVVM
        initializeFileView();
        initializeDragAndDrop();

        refreshButton.setOnAction(e -> refreshFileView());
        addButton.setOnAction(e -> addFileDialog(e));

        refreshFileView();

        // MVVM
        viewModel.selectedFileRowProperty().bind(fileView.getSelectionModel().selectedItemProperty());

        // TODO: I still hate this. Look for different solution at some point
        deleteFileCommand = viewModel.deleteFileCommand();
        deleteButton.disableProperty().bind(deleteFileCommand.notExecutableProperty());
    }

    /**
     * Intializes drag and drop for the file view
     */
    private void initializeDragAndDrop() {
        fileView.setOnDragOver(e -> {
            if (e.getGestureSource() != fileView && e.getDragboard().hasFiles())
                e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
        });

        fileView.setOnDragDropped(e -> {
            var db = e.getDragboard();
            var success = db.hasFiles();

            if (db.hasFiles()) {
                for (var file : db.getFiles()) {
                    if (file.getName().endsWith(".visab2") || file.getName().endsWith(".json"))
                        viewModel.addFile(file);
                }
            }

            // let the source know whether the string was successfully transferred and used
            e.setDropCompleted(success);
            e.consume();
        });
    }

    /**
     * Sets the graphics for the FileView
     */
    private void initializeFileView() {
        nameColumn.setCellFactory(x -> new TreeTableCell<>() {

            final ImageView file = new ImageView(fileImage);
            final ImageView folderClosed = new ImageView(folderClosedImage);
            final ImageView folderOpen = new ImageView(folderOpenImage);
            final ImageView visabFile = new ImageView(visabFileImage);

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                var row = getTreeTableRow();
                var fileRow = row.getItem();

                // Set text
                if (!empty && fileRow != null)
                    setText(fileRow.isDirectory() ? item + "/" : item);
                else
                    setText(null);

                // Set graphics
                if (!empty && fileRow != null) {
                    if (fileRow.getName().endsWith(".visab2"))
                        setGraphic(visabFile);
                    else if (fileRow.isDirectory() && row.getTreeItem().isExpanded()) {
                        setGraphic(folderOpen);
                        getTreeTableView().refresh();
                    } else if (fileRow.isDirectory() && !row.getTreeItem().isExpanded()) {
                        setGraphic(folderClosed);
                        getTreeTableView().refresh();
                    } else
                        setGraphic(file);
                } else
                    setGraphic(null);
            }
        });
    }

    /**
     * Reloads the root from the viewModel. Might be needed if repository path is
     * changed in settings.
     */
    private void refreshFileView() {
        fileView.setRoot(null);

        var root = viewModel.getRootFileRow();
        var rootItem = new RecursiveTreeItem<FileRow>(root, x -> x.getFiles());
        fileView.setRoot(rootItem);

        // If there arent that many visab files, expand some more nodes
        var expandUntil = 25;
        for (var child : rootItem.getChildren()) {
            if (expandUntil - child.getChildren().size() >= 0) {
                child.setExpanded(true);
                expandUntil -= child.getChildren().size();
            }
        }
    }

}
