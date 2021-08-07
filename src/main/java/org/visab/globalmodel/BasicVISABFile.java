package org.visab.globalmodel;

import java.time.LocalDateTime;

/**
 * The BasicVISABFile which serves as an instantiable base implementation for
 * the IVISABFile interface.
 */
public class BasicVISABFile implements IVISABFile {

    protected LocalDateTime creationDate = LocalDateTime.now();
    protected String formatVersion;
    protected String game;

    /**
     * Needed for deserialization.
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
