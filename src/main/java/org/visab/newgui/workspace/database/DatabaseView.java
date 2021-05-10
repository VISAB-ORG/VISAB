package org.visab.newgui.workspace.database;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

import org.visab.newgui.workspace.ExplorerViewBase;
import org.visab.newgui.workspace.ExplorerViewModelBase;

import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Button;

import org.visab.util.VISABUtil;

public class DatabaseView extends ExplorerViewBase<DatabaseViewModel> {

    @FXML
    Button addButton;

    @Override
    protected List<String> getAllowedExtensions() {
        // TODO: Only allow visab2 for now
        return List.of(".visab2");
    }

    @Override
    protected void afterInitialize(URL location, ResourceBundle resources) {
        addButton.setOnAction(e -> addFileDialog(e));
    }

    @Override
    protected void addFileDialog(ActionEvent e) {
        var initialDir = Path.of(VISABUtil.getRunningJarRootDirPath());
        var parentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        var files = new DatabaseFileDialog().getFiles(initialDir, parentStage);

        for (var file : files)
            addFile(file);
    }
}
