package org.visab.newgui.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;

public class DialogHelper {

    public enum DialogResult {

    }

    private Logger logger = LogManager.getLogger(DialogHelper.class);

    private Window parentWindow;

    public void setParentWindow(Window parentWindow) {
        this.parentWindow = parentWindow;
    }

    public String showInputDialog(String contextText, String title, String defaultValue) {
        // TODO: https://code.makery.ch/blog/javafx-dialogs-official/
        var dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setContentText(contextText);

        var result = dialog.showAndWait();

        return result.isPresent() ? result.get() : "";
    }

    /**
     * Shows a file dialog on top of the parent window.
     * 
     * @param directoryPath     The path from at which to launch the file dialog
     * @param allowedExtensions The allowed extensions
     * @param title             The title of the dialog
     * @return A list of the selected files.
     */
    public List<File> showFileDialog(String directoryPath, Map<String, String> allowedExtensions, String title) {
        var files = new ArrayList<File>();
        if (parentWindow == null) {
            var errorMessage = "Tried to open Dialog without having set parent window!\n"
                    + "To use the DialogHelper, you first have to set the parent window from the View by calling "
                    + "viewModel.getDialogHelper().setParentWindow()";
            logger.error(errorMessage);
        } else {
            var fileChooser = new FileChooser();

            for (var extension : allowedExtensions.entrySet()) {
                var filter = new ExtensionFilter(extension.getKey(), extension.getValue());
                fileChooser.getExtensionFilters().add(filter);
            }

            fileChooser.setTitle(title);
            fileChooser.setInitialDirectory(new File(directoryPath));

            var result = fileChooser.showOpenMultipleDialog(parentWindow);

            if (result != null)
                files.addAll(result);
        }

        return files;
    }

}
