package org.visab.newgui.workspace.database;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

import org.visab.newgui.workspace.ExplorerViewBase;
import org.visab.util.VISABUtil;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class DatabaseView extends ExplorerViewBase<DatabaseViewModel> {

    @Override
    protected void afterInitialize(URL location, ResourceBundle resources) {
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
