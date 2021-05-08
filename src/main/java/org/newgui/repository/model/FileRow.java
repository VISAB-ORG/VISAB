package org.newgui.repository.model;

import java.time.LocalDateTime;

public class FileRow {
    private LocalDateTime lastChanged;
    private String name;
    private long size;

    public FileRow(String name, LocalDateTime lastModified, long size) {
        this.lastChanged = lastModified;
        this.name = name;
        this.size = size;
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

}
