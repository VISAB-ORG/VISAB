package org.visab.newgui.control;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import java.time.format.DateTimeFormatter;

import java.text.NumberFormat;
import java.util.Locale;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;

/**
 * The FileExplorer control that represents a file view.
 */
public class FileExplorer extends TreeTableView<ExplorerFile> {

    private static final Image fileImage = new Image("/img/file.png", 16, 16, false, false);

    private static final Image folderClosedImage = new Image("/img/folder_closed.png", 16, 16, false, false);

    private static final Image folderOpenImage = new Image("/img/folder_open.png", 16, 16, false, false);

    private static final Image visabFileImage = new Image("/img/visab_file.png", 16, 16, false, false);

    TreeTableColumn<ExplorerFile, String> nameColumn = new TreeTableColumn<>();

    TreeTableColumn<ExplorerFile, LocalDateTime> creationDateColumn = new TreeTableColumn<>();

    TreeTableColumn<ExplorerFile, Long> sizeColumn = new TreeTableColumn<>();

    public FileExplorer() {
        super();
        nameColumn.setCellValueFactory(new TreeItemPropertyValueFactory<ExplorerFile, String>("name"));
        creationDateColumn
                .setCellValueFactory(new TreeItemPropertyValueFactory<ExplorerFile, LocalDateTime>("creationDate"));
        sizeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<ExplorerFile, Long>("size"));

        // full width (pref): 423px
        nameColumn.setText("Filename");
        nameColumn.setMinWidth(245);
        creationDateColumn.setText("Creation date");
        creationDateColumn.setMinWidth(118);
        sizeColumn.setText("Size");
        sizeColumn.setMinWidth(45);

        initColumnPresentation();

        getColumns().add(nameColumn);
        getColumns().add(creationDateColumn);
        getColumns().add(sizeColumn);

        // Drag and drop
        setOnDragOver(e -> handleDragOver(e));
        setOnDragDropped(e -> handleFilesDropped(e));

        setOnKeyPressed(e -> {
            pressedKeys.add(e.getCode());
            handleKeyPressed(e);
        });
        setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
    }

    private List<KeyCode> pressedKeys = new ArrayList<>();

    public void setBaseFile(RecursiveTreeItem<ExplorerFile> baseFile) {
        super.setRoot(baseFile);
    }

    public void expandChildren(int expandUntil) {
        if (getRoot() == null)
            return;

        for (var child : this.getRoot().getChildren()) {
            if (expandUntil - child.getChildren().size() >= 0) {
                child.setExpanded(true);
                expandUntil -= child.getChildren().size();
            }
        }
    }

    private void handleKeyPressed(KeyEvent e) {
        // Handle pasting
        if (pressedKeys.size() == 2 && pressedKeys.contains(KeyCode.V) && pressedKeys.contains(KeyCode.CONTROL)) {
            if (this.isFocused()) {
                var clipboard = Clipboard.getSystemClipboard();
                var hasFiles = clipboard.getFiles() != null;

                if (hasFiles) {
                    for (var file : clipboard.getFiles()) {
                        for (var handler : fileAddedHandlers)
                            handler.apply(file);
                    }
                }
            }
            return;
        }

        if (pressedKeys.size() == 2 && pressedKeys.contains(KeyCode.C) && pressedKeys.contains(KeyCode.CONTROL)) {
            if (this.isFocused()) {
                var selectedFile = getSelectionModel().selectedItemProperty().get();
                if (selectedFile != null) {
                    var file = new File(selectedFile.getValue().getAbsolutePath());
                    if (file != null) {
                        var content = new HashMap<DataFormat, Object>();
                        content.put(DataFormat.FILES, List.of(file));
                        Clipboard.getSystemClipboard().setContent(content);
                    }
                }
            }
            return;
        }

        if (pressedKeys.size() == 1 && pressedKeys.contains(KeyCode.DELETE)) {
            var selectedFile = getSelectionModel().selectedItemProperty().get();
            if (selectedFile != null) {
                for (var handler : removeSelectedHandlers)
                    handler.apply(selectedFile.getValue());
            }
            return;
        }
    }

    private List<Function<File, Boolean>> fileAddedHandlers = new ArrayList<>();

    public void addFileAddedHandler(Function<File, Boolean> handler) {
        fileAddedHandlers.add(handler);
    }

    private List<Function<ExplorerFile, Boolean>> removeSelectedHandlers = new ArrayList<>();

    public void addRemoveFileHandler(Function<ExplorerFile, Boolean> handler) {
        removeSelectedHandlers.add(handler);
    }

    /**
     * Handler for file being dragged over.
     * 
     * @param e The DragEvent
     */
    private void handleDragOver(DragEvent e) {
        if (e.getGestureSource() != this && e.getDragboard().hasFiles())
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
                for (var handler : fileAddedHandlers)
                    handler.apply(file);
            }
        }

        e.setDropCompleted(hasFiles);
        e.consume();
    }

    /**
     * Sets the graphics for the ExplorerView.
     */
    private void initColumnPresentation() {
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

}
