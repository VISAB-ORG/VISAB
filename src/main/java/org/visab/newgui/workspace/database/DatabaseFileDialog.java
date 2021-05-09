package org.visab.newgui.workspace.database;

import java.util.List;

import org.visab.newgui.workspace.FileDialog;

import javafx.stage.FileChooser.ExtensionFilter;

public class DatabaseFileDialog extends FileDialog {

    public DatabaseFileDialog() {
        super("Add VISAB file", List.of(new ExtensionFilter("VISAB files", "*.visab2")));
    }

}
