package org.visab.processing;

import java.sql.Timestamp;
import java.util.Date;

public abstract class VISABFileBase implements IVISABFile {

    protected Timestamp creationDate = new Timestamp(new Date().getTime());
    protected String formatVersion;
    protected String game;
    protected String fileName;

    public VISABFileBase(String game, String fileFormatVersion, String fileName) {
        this.formatVersion = fileFormatVersion;
        this.game = game;
        this.fileName = fileName;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public String getFileFormatVersion() {
        return formatVersion;
    }

    public String getGame() {
        return game;
    }

    public String getFileName() {
        return fileName;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setFileFormatVersion(String version) {
        this.formatVersion = version;
    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    /**
     * This has to exist for deserializing existing files
     */
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

}
