package org.visab.newgui.workspace.model;

import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FileRow {

    private ObservableList<FileRow> files = FXCollections.observableArrayList();
    private String absolutePath;
    private boolean isDirectory;
    private LocalDateTime lastModified;
    private String name;
    private long size;
    private FileRow parentDir;

    public FileRow(String name, LocalDateTime lastModified, long size, String absolutePath, boolean isDirectory,
            FileRow parentDir) {
        this.lastModified = lastModified;
        this.name = name;
        this.isDirectory = isDirectory;
        this.size = size;
        this.absolutePath = absolutePath;
        this.parentDir = parentDir;
    }

    public FileRow getParentDir() {
        return this.parentDir;
    }

    public ObservableList<FileRow> getFiles() {
        return files;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbsolutePath() {
        return this.absolutePath;
    }

    public LocalDateTime getLastModified() {
        return this.lastModified;
    }

    public String getName() {
        return this.name;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setLastModifies(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isDirectory() {
        return isDirectory;
    }
}
