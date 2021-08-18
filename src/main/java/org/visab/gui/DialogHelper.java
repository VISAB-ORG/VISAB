package org.visab.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.Scope;
import de.saxsys.mvvmfx.ViewModel;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Helper class for showing dialogs and views from the viewmodel without
 * violating the MVVM pattern. Every class inheriting ViewModelBase has an
 * instance of the DialogHelper.
 */
public class DialogHelper {

    /**
     * Shows a confirmation dialog with an OK and CANCEL button.
     * 
     * @param contentText The message for the user
     * @param title       The title of the dialog
     * @return True of OK was pressed
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
    public static void showMessageDialog(AlertType type, String contentText, String title) {
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
        var dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setContentText(contentText);
        dialog.setHeaderText(null);

        var result = dialog.showAndWait();

        return result.isPresent() ? result.get() : "";
    }

    /**
     * Shows a file selection dialog.
     * 
     * @param directoryPath     The path from at which to launch the file dialog
     * @param allowedExtensions The allowed extensions
     * @param title             The title of the dialog
     * @return A list of the selected files
     */
    public List<File> showFileDialog(String directoryPath, Map<String, String> allowedExtensions, String title) {
        var files = new ArrayList<File>();

        var fileChooser = new FileChooser();

        for (var extension : allowedExtensions.entrySet()) {
            var filter = new ExtensionFilter(extension.getKey(), extension.getValue());
            fileChooser.getExtensionFilters().add(filter);
        }

        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(directoryPath));

        var result = fileChooser.showOpenMultipleDialog(new Stage());

        if (result != null)
            files.addAll(result);

        return files;
    }

    /**
     * Creates a new stage and shows the view given in the configuration.
     * Additionally, a viewmodel aswell as scopes may be provided. If the view or
     * its viewmodel use the @InjectViewModel or @InjectScope annotation of MvvmFx,
     * the passed instances will be injected.
     * 
     * @param configuration The configuration based on which the view will be loaded
     * @param viewModel     The viewmodel to provide
     * @param scopes        The scopes to provide
     */
    public void showView(ShowViewConfiguration configuration, ViewModel viewModel, Scope... scopes) {
        var viewStep = FluentViewLoader.fxmlView(configuration.getViewClass());

        var stage = new Stage();
        stage.setTitle(configuration.getStageTitle());

        if (configuration.getWidth() != 0)
            stage.setMinWidth(configuration.getWidth());

        if (configuration.getHeight() != 0)
            stage.setMinHeight(configuration.getHeight());

        if (viewModel != null)
            viewStep.viewModel(viewModel);

        var additionalScope = new GeneralScope();
        additionalScope.setStage(stage);

        var scopesArr = Arrays.copyOf(scopes, scopes.length + 1);
        scopesArr[scopes.length] = additionalScope;
        viewStep.providedScopes(scopesArr);

        var viewTuple = viewStep.load();
        var view = viewTuple.getView();

        stage.setScene(new Scene(view));

        if (configuration.shouldBlock())
            stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
    }

    /**
     * Creates a new stage and shows the view given in the configuration.
     * Additionally, scopes may be provided. If the views viewmodel use
     * the @InjectScope annotation of MvvmFx, the passed instances will be injected.
     * 
     * @param configuration The configuration based on which the view will be loaded
     * @param scopes        The scopes to provide
     */
    public void showView(ShowViewConfiguration configuration, Scope... scopes) {
        showView(configuration, null, scopes);
    }

}
