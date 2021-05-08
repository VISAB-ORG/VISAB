package org.visab.processing;

import java.time.LocalDateTime;

public class VISABFileBase implements IVISABFile {

    protected LocalDateTime creationDate = LocalDateTime.now();
    protected String formatVersion;
    protected String game;

    /**
     * Used for deserialization
     */
    public VISABFileBase() {
    }

    public VISABFileBase(String game, String fileFormatVersion) {
        this.formatVersion = fileFormatVersion;
        this.game = game;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getFileFormatVersion() {
        return formatVersion;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setFileFormatVersion(String version) {
        this.formatVersion = version;
    }

    /**
     * This has to exist for deserializing existing files
     */
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

}
