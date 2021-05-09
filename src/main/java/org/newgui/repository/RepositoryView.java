package org.newgui.repository;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import org.visab.util.Settings;
import org.newgui.repository.model.FileRow;
import javafx.scene.Node;
import org.visab.util.VISABUtil;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RepositoryView implements FxmlView<RepositoryViewModel>, Initializable {

    @FXML
    TreeTableView<FileRow> fileView;

    @FXML
    TreeTableColumn<FileRow, String> nameColumn;

    @FXML
    VBox dropBox;

    @FXML
    Button refreshButton;

    @FXML
    Button addButton;

    @FXML
    Label fileChanges;

    @InjectViewModel
    RepositoryViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Non MVVM
        var root = viewModel.rootFileRowProperty();
        var rootItem = new RecursiveTreeItem<FileRow>(root.getValue(), x -> x.getFiles());
        fileView.setRoot(rootItem);

        // initializeFileView();
        initializeDragAndDrop();

        // refreshButton.setOnAction(e -> refreshFileView());
        addButton.setOnAction(e -> fileDialog(e));

        // MVVM
        // viewModel.selectedFileRowProperty().bind(fileView.getSelectionModel().selectedItemProperty());
        // TODO: Somehow doesnt work
        fileChanges.textProperty().bind(viewModel.fileChangesProperty().asString());
    }

    private void fileDialog(ActionEvent e) {
        var initialDir = Path.of(VISABUtil.getRunningJarRootDirPath());
        var parentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        var files = new VISABFileDialog().getFiles(initialDir, parentStage);

        for (var file : files)
            viewModel.addFile(file);
    }

    private void refreshFileView() {
        // TODO: Change to get from settings
        var repositoryPath = Paths.get(Settings.DATA_PATH);

        fileView.setRoot(null);

        var newRoot = new FileTreeItem(repositoryPath);
        fileView.setRoot(newRoot);
        newRoot.setExpanded(true);

        // If there arent that many visab files, expand some more nodes
        var expandUntil = 25;
        for (var child : newRoot.getChildren()) {
            if (expandUntil - child.getChildren().size() >= 0) {
                child.setExpanded(true);
                expandUntil -= child.getChildren().size();
            }
        }

        viewModel.filesRefreshed();
    }

    private void initializeDragAndDrop() {
        dropBox.setOnDragOver(e -> {
            if (e.getGestureSource() != dropBox && e.getDragboard().hasFiles())
                e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
        });

        dropBox.setOnDragDropped(e -> {
            var db = e.getDragboard();
            var success = false;
            if (db.hasFiles()) {
                for (var file : db.getFiles()) {
                    // TODO: Allow json I guess
                    if (file.getName().endsWith(".visab2"))
                        viewModel.addFile(file);
                }
                success = true;
            }
            /*
             * let the source know whether the string was successfully transferred and used
             */
            e.setDropCompleted(success);
            e.consume();
        });
    }

    private void initializeFileView() {
        refreshFileView();

        var folderClosedImage = new Image("/repository/folder_closed.png", 16, 16, false, false);
        var folderOpenImage = new Image("/repository/folder_open.png", 16, 16, false, false);
        var fileImage = new Image("/repository/file.png", 16, 16, false, false);
        var visabFileImage = new Image("/repository/visab_file.png", 16, 16, false, false);

        nameColumn.setCellFactory(x -> new TreeTableCell<>() {

            final ImageView folderClosed = new ImageView(folderClosedImage);
            final ImageView folderOpen = new ImageView(folderOpenImage);
            final ImageView file = new ImageView(fileImage);
            final ImageView visabFile = new ImageView(visabFileImage);

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);

                // Set images
                var fileRow = getTreeTableRow().getItem();
                if (!empty && fileRow != null) {
                    if (fileRow.getName().endsWith(".visab2"))
                        setGraphic(visabFile);
                    else if (fileRow.isDirectory() && fileRow.isExpanded()) {
                        setGraphic(folderOpen);
                        getTreeTableView().refresh();
                    } else if (fileRow.isDirectory() && !fileRow.isExpanded()) {
                        setGraphic(folderClosed);
                        getTreeTableView().refresh();
                    } else
                        setGraphic(file);
                } else
                    setGraphic(null);
            }
        });
    }

}
