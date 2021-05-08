package org.visab.processing;

import java.time.LocalDateTime;

public interface IVISABFile {

    LocalDateTime getCreationDate();

    String getFileFormatVersion();

    public String getGame();
}
