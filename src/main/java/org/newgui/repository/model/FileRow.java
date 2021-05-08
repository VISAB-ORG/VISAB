package org.newgui.repository.model;

import java.time.LocalDateTime;

public class FileRow {
    private LocalDateTime lastChanged;
    private String name;
    private long size;
    private String fullPath;

    public FileRow(String name, LocalDateTime lastModified, long size, String fullPath) {
        this.lastChanged = lastModified;
        this.name = name;
        this.size = size;
        this.fullPath = fullPath;
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
