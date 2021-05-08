package org.newgui.repository.model;

import java.util.Date;

public class FileRow {
    private Date lastChanged;
    private String name;
    private long size;

    public FileRow(String name, Date lastModified, long size) {
        this.lastChanged = lastModified;
        this.name = name;
        this.size = size;
    }

    public Date getLastModified() {
        return this.lastChanged;
    }

    public String getName() {
        return this.name;
    }

    public long getSize() {
        return this.size;
    }

}
