package org.visab.processing;

import java.sql.Timestamp;

public interface IVISABFile {

    Timestamp getCreationDate();

    String getFileFormatVersion();

    public String getGame();

    String getFileName();
}
