package org.newgui.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;

import org.newgui.repository.model.FileRow;

import javafx.event.Event;
import javafx.scene.control.TreeItem;

public class FileTreeItem extends TreeItem<FileRow> {

    private boolean isDirectory;

    private FileRow fileRow;

    public boolean isDirectory() {
        return this.isDirectory;
    }

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

        var fileRow = new FileRow(name, lastModified, size, fullPath);
        this.fileRow = fileRow;

        if (Files.isDirectory(file)) {
            isDirectory = true;

            // Recursively add FileTreeItem as children.
            addChildren(file);
            // TODO: Set graphic
        } else {
            // Set graphic
        }

        this.setValue(fileRow);
        this.addEventHandler(FileTreeItem.branchExpandedEvent(), e -> expandedHandler(e));
        this.addEventHandler(FileTreeItem.branchCollapsedEvent(), e -> collapsedHandler(e));
    }

    private void addChildren(Path file) {
        try {
            for (var _file : Files.newDirectoryStream(file))
                this.getChildren().add(new FileTreeItem(_file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void collapsedHandler(Event e) {
        var source = (FileTreeItem) e.getSource();
        if (source.isDirectory() && !source.isExpanded()) {
            // imageview iv=(imageview)source.getgraphic();
            // iv.setimage(foldercollapseimage);
        }
    }

    private void expandedHandler(Event e) {
        var source = (FileTreeItem) e.getSource();
        if (source.isDirectory() && source.isExpanded()) {
            // TODO: Add image
        }
        // TODO: Maybe add children dynamically if repository gets to big.
    }
}
