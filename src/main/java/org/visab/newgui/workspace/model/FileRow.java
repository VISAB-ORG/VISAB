package org.visab.newgui.workspace.model;

import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FileRow {

    private ObservableList<FileRow> files = FXCollections.observableArrayList();
    private String absolutePath;
    private boolean isDirectory;
    private LocalDateTime lastChanged;
    private String name;
    private long size;

    public FileRow(String name, LocalDateTime lastModified, long size, String absolutePath, boolean isDirectory) {
        this.lastChanged = lastModified;
        this.name = name;
        this.isDirectory = isDirectory;
        this.size = size;
        this.absolutePath = absolutePath;
    }

    public ObservableList<FileRow> getFiles() {
        return files;
    }

    public String getAbsolutePath() {
        return this.absolutePath;
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
