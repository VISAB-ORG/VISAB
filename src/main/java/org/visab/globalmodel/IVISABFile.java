package org.visab.globalmodel;

import java.time.LocalDateTime;

/**
 * The IVISABFile interface that all VISAB file POJOs have to implement.
 */
public interface IVISABFile {

    LocalDateTime getCreationDate();

    String getFileFormatVersion();

    String getGame();
}
