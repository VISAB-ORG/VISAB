package org.visab.newgui.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;

// TODO: Include the style sheets here
public class DialogHelper {

    private Logger logger = LogManager.getLogger(DialogHelper.class);

    private Window parentWindow;

    public void setParentWindow(Window parentWindow) {
        this.parentWindow = parentWindow;
    }

    /**
     * Shows a confirmation dialog with an OK and CANCEL button.
     * 
     * @param contentText The message for the user
     * @param title       The title of the dialog
     * @return True of Ok pressed
     */
    public boolean showConfirmationDialog(String contentText, String title) {
        var dialog = new Alert(AlertType.CONFIRMATION);
        dialog.setTitle(title);
        dialog.setContentText(contentText);
        dialog.setHeaderText(null);

        var result = dialog.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Shows a small info dialog.
     * 
     * @param message The message to display
     */
    public void showInfo(String message) {
        showMessageDialog(AlertType.INFORMATION, message, "Information");
    }

    /**
     * Shows a small warning dialog.
     * 
     * @param message The message to display
     */
    public void showWarning(String message) {
        showMessageDialog(AlertType.WARNING, message, "Warning");
    }

    /**
     * Shows a small error dialog.
     * 
     * @param message The message to display
     */
    public void showError(String message) {
        showMessageDialog(AlertType.WARNING, message, "Error");
    }

    /**
     * Shows a small dialog of the given type.
     * 
     * @param type        The type of the dialog.
     * @param contentText The contentText
     * @param title       The title of the dialog
     */
    private void showMessageDialog(AlertType type, String contentText, String title) {
        var dialog = new Alert(type);
        dialog.setTitle(title);
        dialog.setContentText(contentText);
        dialog.setHeaderText(null);

        dialog.showAndWait();
    }

    /**
     * Shows a dialog with a single Text input field.
     * 
     * @param contentText  The contentText describing the text field
     * @param defaultValue The default value of the text fieldk
     * @param title        The title of the dialog
     * @return The text field value if OK pressed, "" else
     */
    public String showInputDialog(String contentText, String defaultValue, String title) {
        // TODO: https://code.makery.ch/blog/javafx-dialogs-official/
        var dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setContentText(contentText);
        dialog.setHeaderText(null);

        var result = dialog.showAndWait();

        return result.isPresent() ? result.get() : "";
    }

    /**
     * Shows a file dialog.
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
