package org.visab.processing;

import java.sql.Timestamp;
import java.util.Date;

public abstract class VISABFileBase {

    private Timestamp creationDate = new Timestamp(new Date().getTime());
    private String formatVersion;
    private String game;

    public VISABFileBase(String game, String fileFormatVersion) {
        this.formatVersion = fileFormatVersion;
        this.game = game;
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

}
