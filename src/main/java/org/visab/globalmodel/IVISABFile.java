package org.visab.globalmodel;

import java.time.LocalDateTime;

public interface IVISABFile {

    LocalDateTime getCreationDate();

    String getFileFormatVersion();

    public String getGame();
}
