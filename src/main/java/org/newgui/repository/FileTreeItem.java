package org.newgui.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.event.Event;
import javafx.scene.control.TreeItem;

public class FileTreeItem extends TreeItem<String> {

    private boolean isDirectory;

    private String fullPath;

    public boolean isDirectory() {
        return this.isDirectory;
    }

    public String getFullPath() {
        return this.fullPath;
    }

    public FileTreeItem(Path file) {
        super(file.toString());
        this.fullPath = file.toString();

        if (Files.isDirectory(file)) {
            isDirectory = true;

            // Recursively add FileTreeItem as children.
            addChildren(file);
            // TODO: Set graphic
        } else {
            // Set graphic
        }

        setValue(file);

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

    private void setValue(Path file) {
        setValue(file.getFileName().toString());
    }
}
