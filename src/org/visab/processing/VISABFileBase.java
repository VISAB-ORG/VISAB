package org.visab.processing;

import java.sql.Timestamp;

public abstract class VISABFileBase {

    private Timestamp createdOn;

    private String formatVersion;

    private String game;

    public VISABFileBase(String game, String formatVersion, Timestamp creationDate) {
	this.createdOn = creationDate;
	this.formatVersion = formatVersion;
	this.game = game;
    }

    public Timestamp getCreatedOn() {
	return createdOn;
    }

    public String getFormatVersion() {
	return formatVersion;
    }

    public String getGame() {
	return game;
    }

}
