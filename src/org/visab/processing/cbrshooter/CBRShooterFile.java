package org.visab.processing.cbrshooter;

import java.sql.Timestamp;

import org.visab.processing.VISABFileBase;

public class CBRShooterFile extends VISABFileBase {

    public CBRShooterFile(Timestamp creationDate) {
	super("CBRShooter", "2.0", creationDate);
    }

}
