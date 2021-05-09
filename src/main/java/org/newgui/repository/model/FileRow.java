package org.newgui.repository.model;

import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FileRow {

    private LocalDateTime lastChanged;
    private String name;
    private long size;
    private String fullPath;
    private boolean isDirectory;
    private boolean isExpanded;
    private ObservableList<FileRow> files = FXCollections.observableArrayList();

    public FileRow(String name, LocalDateTime lastModified, long size, String fullPath, boolean isDirectory) {
        this.lastChanged = lastModified;
        this.name = name;
        this.isDirectory = isDirectory;
        this.size = size;
        this.fullPath = fullPath;
    }

    public ObservableList<FileRow> getFiles() {
        return files;
    }

    public boolean isExpanded() {
        return this.isExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public LocalDateTime getLastModified() {
        return this.lastChanged;
    }

    public String getFullPath() {
        return this.fullPath;
    }

    public String getName() {
        return this.name;
    }

    public long getSize() {
        return this.size;
    }

}
