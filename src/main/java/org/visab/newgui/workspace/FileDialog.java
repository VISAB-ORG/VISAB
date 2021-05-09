package org.visab.newgui.workspace;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public abstract class FileDialog {

    private FileChooser fileChooser;

    public FileDialog(String title, List<ExtensionFilter> extensionFilters) {
        this.fileChooser = new FileChooser();

        if (extensionFilters != null)
            fileChooser.getExtensionFilters().addAll(extensionFilters);

        fileChooser.setTitle(title);
    }

    public List<File> getFiles(Path initialDir, Stage parentStage) {
        fileChooser.setInitialDirectory(initialDir.toFile());

        var files = fileChooser.showOpenMultipleDialog(parentStage);

        return files == null ? new ArrayList<File>() : files;
    }

}
