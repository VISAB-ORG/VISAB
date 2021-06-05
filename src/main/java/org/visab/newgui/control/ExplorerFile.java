package org.visab.newgui.control;

import java.time.LocalDateTime;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ExplorerFile {

    private ObservableList<ExplorerFile> files = FXCollections.observableArrayList();
    private StringProperty absolutePathProperty = new SimpleStringProperty();
    private StringProperty nameProperty = new SimpleStringProperty();
    private LongProperty sizeProperty = new SimpleLongProperty();
    private ObjectProperty<ExplorerFile> parentDirProperty = new SimpleObjectProperty<>();
    
    private boolean isDirectory;
    private LocalDateTime creationDate;

    public ExplorerFile(String name, LocalDateTime creationDate, long size, String absolutePath, boolean isDirectory,
            ExplorerFile parentDir) {
        this.nameProperty.set(name);
        this.sizeProperty.set(size);
        this.absolutePathProperty.set(absolutePath);
        this.parentDirProperty.set(parentDir);

        this.isDirectory = isDirectory;
        this.creationDate = creationDate;
    }

    public ExplorerFile getParentDir() {
        return this.parentDirProperty.get();
    }

    public ObservableList<ExplorerFile> getFiles() {
        return files;
    }

    public String getAbsolutePath() {
        return this.absolutePathProperty.get();
    }

    public String getName() {
        return this.nameProperty.get();
    }

    public long getSize() {
        return this.sizeProperty.get();
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public void setSize(long size) {
        this.sizeProperty.set(size);
    }

    public void setName(String name) {
        this.nameProperty.set(name);
    }

    public void setAbsolutePath(String path) {
        this.absolutePathProperty.set(path);
    }

}
