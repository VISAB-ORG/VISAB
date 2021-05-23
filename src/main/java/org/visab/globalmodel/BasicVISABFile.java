package org.visab.globalmodel;

import java.time.LocalDateTime;

/**
 * The basic VISABFile that holds all meta information required by IVISABFile.
 */
public class BasicVISABFile implements IVISABFile {

    protected LocalDateTime creationDate = LocalDateTime.now();
    protected String formatVersion;
    protected String game;

    /**
     * Used for deserialization
     */
    public BasicVISABFile() {
    }

    public BasicVISABFile(String game, String fileFormatVersion) {
        this.formatVersion = fileFormatVersion;
        this.game = game;
    }

    @Override
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public String getFileFormatVersion() {
        return formatVersion;
    }

    @Override
    public String getGame() {
        return game;
    }

    /**
     * This has to exist for deserializing existing files
     */
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setFileFormatVersion(String version) {
        this.formatVersion = version;
    }

    public void setGame(String game) {
        this.game = game;
    }

}
