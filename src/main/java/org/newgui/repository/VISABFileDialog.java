package org.newgui.repository;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class VISABFileDialog {

    private FileChooser fileChooser;

    public VISABFileDialog() {
        this.fileChooser = new FileChooser();

        // TODO: Allow json I guess?
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("VISAB files", "*.visab2"));
        fileChooser.setTitle("Add VISAB files");
    }

    public List<File> getFiles(Path initialDir, Stage parentStage) {
        fileChooser.setInitialDirectory(initialDir.toFile());

        var files = fileChooser.showOpenMultipleDialog(parentStage);

        return files == null ? new ArrayList<File>() : files;
    }

}
