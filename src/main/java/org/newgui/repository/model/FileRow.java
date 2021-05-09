package org.newgui.repository.model;

import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FileRow {

    private ObservableList<FileRow> files = FXCollections.observableArrayList();
    private String fullPath;
    private boolean isDirectory;
    private LocalDateTime lastChanged;
    private String name;
    private long size;

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

    public String getFullPath() {
        return this.fullPath;
    }

    public LocalDateTime getLastModified() {
        return this.lastChanged;
    }

    public String getName() {
        return this.name;
    }

    public long getSize() {
        return this.size;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

}
