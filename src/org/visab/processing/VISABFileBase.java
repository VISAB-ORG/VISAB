package org.visab.processing;

import java.sql.Timestamp;
import java.util.Date;

public abstract class VISABFileBase {

    private Timestamp createdOn = new Timestamp(new Date().getTime());

    private String formatVersion;

    private String game;

    public VISABFileBase(String game, String formatVersion) {
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
