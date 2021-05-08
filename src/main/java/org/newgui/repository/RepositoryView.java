package org.newgui.repository;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import org.visab.util.Settings;
import org.newgui.repository.model.FileRow;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RepositoryView implements FxmlView<RepositoryViewModel>, Initializable {

    @FXML
    TreeTableView<FileRow> fileView;

    @FXML
    TreeTableColumn<FileRow, String> nameColumn;

    @InjectViewModel
    RepositoryViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Non MVVM
        initializeFileView();

        // MVVM
        // viewModel.selectedFileRowProperty().bind(fileView.getSelectionModel().selectedItemProperty());
    }

    private void initializeFileView() {
        // TODO: Change to get from settings
        var repositoryPath = Paths.get(Settings.DATA_PATH);

        var rootNode = new FileTreeItem(repositoryPath);
        fileView.setRoot(rootNode);
        rootNode.setExpanded(true);

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
