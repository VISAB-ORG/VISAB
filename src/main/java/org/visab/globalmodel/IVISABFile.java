package org.visab.globalmodel;

import java.time.LocalDateTime;

/**
 * The IVISABFile interface that all VISAB POJOs have to implement. Usual
 * IVISABFiles contain meta information, a list of statistics and optionally
 * images.
 */
public interface IVISABFile {

    LocalDateTime getCreationDate();

    String getFileFormatVersion();

    String getGame();
}
