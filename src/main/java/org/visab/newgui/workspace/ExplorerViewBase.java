package org.visab.newgui.workspace;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.visab.newgui.AppMain;
import org.visab.newgui.workspace.model.ExplorerFile;
import org.visab.workspace.BasicRepository;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.TransferMode;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

// TODO: Add error message when adding file fails
public abstract class ExplorerViewBase<TViewModel extends ExplorerViewModelBase>
        implements FxmlView<TViewModel>, Initializable {

    /**
     * Graphics for FileView TOOD: Get theres from somewhere else I Suppose
     */
    private static final Image fileImage = new Image("/repository/file.png", 16, 16, false, false);

    private static final Image folderClosedImage = new Image("/repository/folder_closed.png", 16, 16, false, false);

    private static final Image folderOpenImage = new Image("/repository/folder_open.png", 16, 16, false, false);

    private static final Image visabFileImage = new Image("/repository/visab_file.png", 16, 16, false, false);

    /**
     * The explorer view
     */
    @FXML
    TreeTableView<ExplorerFile> explorerView;

    /**
     * The file name column of the explorer view
     */
    @FXML
    TreeTableColumn<ExplorerFile, String> nameColumn;

    /**
     * The creation date column of the explorer view
     */
    @FXML
    TreeTableColumn<ExplorerFile, LocalDateTime> creationDateColumn;

    /**
     * The file size column of the explorer view
     */
    @FXML
    TreeTableColumn<ExplorerFile, Long> sizeColumn;

    /**
     * Button for fully refreshing the explorer view
     */
    @FXML
    Button refreshButton;

    @FXML
    public void deleteFileAction() {
        viewModel.deleteFileCommand().execute();
    }

    @FXML
    public void showInExplorerAction() {
        viewModel.showInExplorerCommand().execute();
    }

    @FXML
    public void renameFileAction() {
        viewModel.renameFileCommand().execute();
    }

    @FXML
    public void addFilesAction() {
        viewModel.addFileCommand().execute();
    }

    @InjectViewModel
    protected TViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeExplorerPresentation();
        refreshExplorerView();

        // Refresh view completely
        refreshButton.setOnAction(e -> refreshExplorerView());

        // Drag and drop
        explorerView.setOnDragOver(e -> handleDragOver(e));
        explorerView.setOnDragDropped(e -> handleFilesDropped(e));

        // CTRL + C & CTRL + V & Delete
        initializeKeyCombinations();

        // Selected tree item
        viewModel.selectedExplorerFileProperty().bind(explorerView.getSelectionModel().selectedItemProperty());

        // After the primaryStage.show() was called from AppMain.
        // Has to be called here, because the elements we want to reference, are only
        // loaded upon the stage being shown.
        AppMain.getPrimaryStage().setOnShowing(e -> {
            viewModel.getDialogHelper().setParentWindow(explorerView.getScene().getWindow());
            initializeKeyCombinations();
        });

        afterInitialize(location, resources);
    }

    /**
     * Fully refreshes the explorer view.
     */
    protected void refreshExplorerView() {
        explorerView.setRoot(null);

        var root = viewModel.getFreshBaseFile();
        var rootItem = new RecursiveTreeItem<ExplorerFile>(root, x -> x.getFiles());
        explorerView.setRoot(rootItem);

        // If there arent that many files, expand some more nodes
        var expandUntil = 15;
        for (var child : rootItem.getChildren()) {
            if (expandUntil - child.getChildren().size() >= 0) {
                child.setExpanded(true);
                expandUntil -= child.getChildren().size();
            }
        }

    }

    /**
     * Adds the KeyCombations. For now CTRL + V and DELETE are supported.
     */
    private void initializeKeyCombinations() {
        var keyCombinations = new HashMap<KeyCodeCombination, Runnable>();

        // CTRL + V pasting
        keyCombinations.put(new KeyCodeCombination(KeyCode.V, KeyCombination.SHORTCUT_DOWN), () -> {
            // TODO: Do we require focussed?
            if (explorerView.isFocused()) {
                var clipboard = Clipboard.getSystemClipboard();
                var hasFiles = clipboard.getFiles() != null;

                if (hasFiles) {
                    for (var file : clipboard.getFiles())
                        viewModel.addFile(file);
                }
            }
        });

        // CTRL + C copying
        keyCombinations.put(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN), () -> {
            // TODO: Do we require focussed?
            if (explorerView.isFocused()) {
                var selectedFile = viewModel.getSelectedFile();
                if (selectedFile != null) {
                    var file = new BasicRepository("").loadFile(selectedFile.getAbsolutePath());
                    if (file != null) {
                        var content = new HashMap<DataFormat, Object>();
                        content.put(DataFormat.FILES, List.of(file));
                        Clipboard.getSystemClipboard().setContent(content);
                    }
                }
            }
        });

        // DELETE removing selected file
        keyCombinations.put(new KeyCodeCombination(KeyCode.DELETE), () ->

        {
            if (explorerView.isFocused())
                viewModel.deleteFileCommand().execute();
        });

        AppMain.getPrimaryStage().sceneProperty().addListener(x -> {
            AppMain.getPrimaryStage().getScene().getAccelerators().putAll(keyCombinations);
        });
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
     * Handler for adding files via drag and drop.
     * 
     * @param e The DragEvent
     */
    private void handleFilesDropped(DragEvent e) {
        var db = e.getDragboard();
        var hasFiles = db.hasFiles();

        if (hasFiles) {
            for (var file : db.getFiles()) {
                // Call add file on the viewmodel
                viewModel.addFile(file);
            }
        }

        e.setDropCompleted(hasFiles);
        e.consume();
    }

    /**
     * Sets the graphics for the ExplorerView.
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

                if (empty || fileRow == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                setText(fileRow.isDirectory() ? item + "/" : item);

                // Set graphics
                if (fileRow.getName().endsWith(".visab2")) {
                    setGraphic(visabFile);
                } else if (fileRow.isDirectory() && row.getTreeItem().isExpanded()) {
                    setGraphic(folderOpen);
                    getTreeTableView().refresh();
                } else if (fileRow.isDirectory() && !row.getTreeItem().isExpanded()) {
                    setGraphic(folderClosed);
                    getTreeTableView().refresh();
                } else {
                    setGraphic(file);
                }
            }
        });

        // Set gregorian time
        creationDateColumn.setCellFactory(x -> new TreeTableCell<>() {

            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);

                var explorerFile = getTreeTableRow().getItem();

                if (empty || explorerFile == null) {
                    setText(null);
                    return;
                }

                var formattedTime = explorerFile.getCreationDate().format(formatter);
                setText(formattedTime);
            }
        });

        // Set nice size formatting
        sizeColumn.setCellFactory(x -> new TreeTableCell<>() {

            final NumberFormat germanNumberFormat = NumberFormat.getInstance(Locale.GERMAN);

            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                    return;
                }

                var inKb = item / 1000;
                setText(germanNumberFormat.format(inKb) + " KB");
            }
        });
    }

    /**
     * Called directly after initialize() is done
     * 
     * @param location  Forwarded from initialize
     * @param resources Forwarded from initialize
     */
    protected abstract void afterInitialize(URL location, ResourceBundle resources);
}
