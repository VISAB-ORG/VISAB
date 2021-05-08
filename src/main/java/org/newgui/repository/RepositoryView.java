package org.newgui.repository;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import org.visab.util.Settings;
import org.newgui.repository.model.FileRow;

public class RepositoryView implements FxmlView<RepositoryViewModel>, Initializable {

    @FXML
    TreeView<String> fileView;

    @FXML
    TreeTableView<FileRow> fileView2;

    @InjectViewModel
    RepositoryViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: Change to get from settings
        var repositoryPath = Paths.get(Settings.DATA_PATH);

        var rootNode = new FileTreeItem(repositoryPath);
        fileView.setRoot(rootNode);

        var root = new FileTreeItem2(repositoryPath);
        fileView2.setRoot(root);

        // try {
        // for (var file : Files.newDirectoryStream(repositoryPath))
        // rootNode.getChildren().add(new FileTreeItem(file));
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
    }

}
