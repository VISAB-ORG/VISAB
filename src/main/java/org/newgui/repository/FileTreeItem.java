package org.newgui.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import org.newgui.repository.model.FileRow;

import javafx.event.Event;
import javafx.scene.control.TreeItem;

public class FileTreeItem extends TreeItem<FileRow> {

    private boolean isDirectory;

    private String fullPath;

    public boolean isDirectory() {
        return this.isDirectory;
    }

    public String getFullPath() {
        return this.fullPath;
    }

    public FileTreeItem(Path file) {
        var asFile = file.toFile();
        var fileRow = new FileRow(asFile.getName(), new Date(asFile.lastModified()), FileSizeHelper.size(file) / 1000L);
        this.fullPath = file.toString();

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
