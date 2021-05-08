package org.newgui.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;

import org.newgui.repository.model.FileRow;

import javafx.scene.control.TreeItem;

public class FileTreeItem extends TreeItem<FileRow> {

    private FileRow fileRow;

    public FileRow getFileRow() {
        return this.fileRow;
    }

    public FileTreeItem(Path file) {
        // Create the FileRow
        var asFile = file.toFile();

        var name = asFile.getName();
        var lastModified = Instant.ofEpochMilli(asFile.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        var size = FileSizeHelper.size(file) / 1000L; // In kb
        var fullPath = file.toAbsolutePath().toString();
        var isDirectory = Files.isDirectory(file);

        var fileRow = new FileRow(name, lastModified, size, fullPath, isDirectory);
        this.fileRow = fileRow;

        if (isDirectory)
            // Recursively add FileTreeItem as children.
            addChildren(file);

        this.setValue(fileRow);

        this.addEventHandler(FileTreeItem.branchExpandedEvent(), e -> {
            var treeItem = (FileTreeItem) (Object) e.getSource();
            treeItem.getFileRow().setExpanded(true);
        });

        this.addEventHandler(FileTreeItem.branchCollapsedEvent(), e -> {
            var treeItem = (FileTreeItem) (Object) e.getSource();
            treeItem.getFileRow().setExpanded(false);
        });
    }

    private void addChildren(Path file) {
        try {
            for (var _file : Files.newDirectoryStream(file))
                this.getChildren().add(new FileTreeItem(_file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
