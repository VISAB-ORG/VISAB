package org.visab.globalmodel;

import java.time.LocalDateTime;

/**
 * The IVISABFile that all VISAB POJOs have to implement. Usual IVISABFiles
 * contain meta information, a list of statistics and optionally images.
 */
public interface IVISABFile {

    LocalDateTime getCreationDate();

    String getFileFormatVersion();

    public String getGame();
}
