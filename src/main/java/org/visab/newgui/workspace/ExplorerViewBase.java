package org.visab.newgui.workspace;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import org.visab.newgui.workspace.model.FileRow;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.TransferMode;

import org.visab.util.VISABUtil;

// TODO: Add error message when adding file fails
public abstract class ExplorerViewBase<TViewModel extends ExplorerViewModelBase>
        implements FxmlView<TViewModel>, Initializable {

    /**
     * Graphics for FileView
     */
    private static final Image fileImage = new Image("/repository/file.png", 16, 16, false, false);

    private static final Image folderClosedImage = new Image("/repository/folder_closed.png", 16, 16, false, false);

    private static final Image folderOpenImage = new Image("/repository/folder_open.png", 16, 16, false, false);

    private static final Image visabFileImage = new Image("/repository/visab_file.png", 16, 16, false, false);

    /**
     * The explorer view
     */
    @FXML
    TreeTableView<FileRow> explorerView;

    /**
     * The file name column of the explorer view
     */
    @FXML
    TreeTableColumn<FileRow, String> nameColumn;

    /**
     * The last modified column of the explorer view
     */
    @FXML
    TreeTableColumn<FileRow, LocalDateTime> modifiedColumn;

    /**
     * The button to remove the currently selected item
     */
    @FXML
    Button removeButton;

    /**
     * The button to add files
     */
    @FXML
    Button addButton;

    /**
     * Button for showing the currently selected file in explorer
     */
    @FXML
    Button showInExplorerButton;

    /**
     * Button for fully refreshing the explorer view
     */
    @FXML
    Button refreshButton;

    /**
     * Button for renaming the currently selected file
     */
    @FXML
    Button renameButton;

    /**
     * The action to be executed when the removeButton is pressed
     */
    @FXML
    public void removeAction() {
        removeCommand.execute();
    }

    /**
     * The ViewModel
     */
    @InjectViewModel
    protected TViewModel viewModel;

    private Command removeCommand;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeExplorerPresentation();
        refreshExplorerView();

        addButton.setOnAction(e -> addFileDialog(e));
        showInExplorerButton.setOnAction(e -> showInExplorer(e));
        refreshButton.setOnAction(e -> refreshExplorerView());

        // TODO: Show some dialog I guess
        renameButton.setOnAction(e -> viewModel.renameSelectedFile(UUID.randomUUID().toString()));

        explorerView.setOnDragOver(e -> handleDragOver(e));
        explorerView.setOnDragDropped(e -> handleFilesDropped(e));

        initializeKeyCombinations();

        // MVVM
        viewModel.selectedFileRowProperty().bind(explorerView.getSelectionModel().selectedItemProperty());

        // TODO: I still hate this. Look for different solution at some point
        removeCommand = viewModel.deleteFileCommand();
        removeButton.disableProperty().bind(removeCommand.notExecutableProperty());

        afterInitialize(location, resources);
    }

    /**
     * Adds the KeyCombations. For now CTRL + V and DELETE are supported. TODO: Add
     * these
     */
    private void initializeKeyCombinations() {
        var keyCombinations = new HashMap<KeyCodeCombination, Runnable>();

        // CTRL + V pasting
        keyCombinations.put(new KeyCodeCombination(KeyCode.V, KeyCombination.SHORTCUT_DOWN), () -> {
            // TODO: Do we require focussed?
            if (explorerView.isFocused() && viewModel.getSelectedFileRow() != null) {
                var clipboard = Clipboard.getSystemClipboard();
                var hasFiles = clipboard.getFiles() != null;

                if (hasFiles) {
                    for (var file : clipboard.getFiles())
                        addFile(file);
                }
            }
        });

        // DELETE removing selected file
        keyCombinations.put(new KeyCodeCombination(KeyCode.DELETE), () -> {
            if (viewModel.getSelectedFileRow() != null)
                removeAction();
        });
    }

    private void showInExplorer(ActionEvent e) {
        try {
            switch (VISABUtil.getOS()) {
            case WINDOWS:
                var command = "explorer.exe " + viewModel.getBaseDirPath();
                if (viewModel.getSelectedFileRow() != null)
                    command = "explorer.exe /select," + viewModel.getSelectedFileRow().getAbsolutePath();

                Runtime.getRuntime().exec(command);
                break;
            default:
                System.out.println("Opening explorer not supported for non windows OS.");
                break;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Handler for file being dragged over
     * 
     * @param e The DragEvent
     */
    private void handleDragOver(DragEvent e) {
        if (e.getGestureSource() != explorerView && e.getDragboard().hasFiles())
            e.acceptTransferModes(TransferMode.MOVE);
        e.consume();
    }

    /**
     * Fully refreshes the explorer view
     */
    protected void refreshExplorerView() {
        explorerView.setRoot(null);

        var root = viewModel.getFreshBaseFileRow();
        var rootItem = new RecursiveTreeItem<FileRow>(root, x -> x.getFiles());
        explorerView.setRoot(rootItem);

        // If there arent that many files, expand some more nodes
        var expandUntil = 25;
        for (var child : rootItem.getChildren()) {
            if (expandUntil - child.getChildren().size() >= 0) {
                child.setExpanded(true);
                expandUntil -= child.getChildren().size();
            }
        }

    }

    /**
     * Sets the graphics for the ExplorerView
     */
    private void initializeExplorerPresentation() {
        nameColumn.setCellFactory(x -> new TreeTableCell<>() {

            final ImageView file = new ImageView(fileImage);
            final ImageView folderClosed = new ImageView(folderClosedImage);
            final ImageView folderOpen = new ImageView(folderOpenImage);
            final ImageView visabFile = new ImageView(visabFileImage);

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                var row = getTreeTableRow();
                var fileRow = row.getItem();

                // Set text
                if (!empty && fileRow != null)
                    setText(fileRow.isDirectory() ? item + "/" : item);
                else
                    setText(null);

                // Set graphics
                if (!empty && fileRow != null) {
                    if (fileRow.getName().endsWith(".visab2"))
                        setGraphic(visabFile);
                    else if (fileRow.isDirectory() && row.getTreeItem().isExpanded()) {
                        setGraphic(folderOpen);
                        getTreeTableView().refresh();
                    } else if (fileRow.isDirectory() && !row.getTreeItem().isExpanded()) {
                        setGraphic(folderClosed);
                        getTreeTableView().refresh();
                    } else
                        setGraphic(file);
                } else
                    setGraphic(null);
            }
        });

        // Set gregorian time
        modifiedColumn.setCellFactory(x -> new TreeTableCell<>() {

            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);

                var fileRow = getTreeTableRow().getItem();
                if (!empty && fileRow != null) {
                    var formattedTime = fileRow.getLastModified().format(formatter);
                    setText(formattedTime);
                }
            }
        });
    }

    /**
     * Handler for adding files via drag and drop
     * 
     * @param e The DragEvent
     */
    private void handleFilesDropped(DragEvent e) {
        var db = e.getDragboard();
        var hasFiles = db.hasFiles();

        if (hasFiles) {
            for (var file : db.getFiles())
                addFile(file);
        }

        e.setDropCompleted(hasFiles);
        e.consume();
    }

    /**
     * Recursively adds a file to the viewModel. If the file is a directory, adds
     * all the directories files instead.
     * 
     * @param file The file to add
     */
    protected void addFile(File file) {
        if (file.isDirectory() && addOnlyFiles()) {
            for (var _file : file.listFiles())
                addFile(_file);
        } else if (file.isDirectory() && !addOnlyFiles())
            addFile(file);
        else if (hasAllowedExtension(file))
            viewModel.addFile(file);
    }

    /**
     * Checks if a file has an allowed file extension
     * 
     * @param file The file to check
     * @return True if allowed, false else
     */
    private boolean hasAllowedExtension(File file) {
        for (var extension : getAllowedExtensions()) {
            if (file.getName().contains(extension))
                return true;
        }

        return false;
    }

    /**
     * Dialog for adding files
     * 
     * @param e The ActionEvent
     */
    protected abstract void addFileDialog(ActionEvent e);

    /**
     * The allowed file extensions for drag and drop
     * 
     * @return A list of the allowed extensions
     */
    protected abstract List<String> getAllowedExtensions();

    /**
     * Called directly after initialize is done
     * 
     * @param location  Forwarded from initialize
     * @param resources Forwarded from initialize
     */
    protected abstract void afterInitialize(URL location, ResourceBundle resources);

    /**
     * Identicates, whether folders added via drag and drop and ctrl+v should pass
     * its contents instead of the folder itself to the viewModels addFile method
     * 
     * @return True if contents should be passed instead of the folder, false if
     *         folders can be passed
     */
    protected abstract boolean addOnlyFiles();
}
