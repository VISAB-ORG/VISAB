package org.visab.repository;

import java.time.LocalDateTime;

public interface IVISABFile {

    LocalDateTime getCreationDate();

    String getFileFormatVersion();

    public String getGame();
}
