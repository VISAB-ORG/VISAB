package org.visab.generalmodelchangeme;

import java.time.LocalDateTime;

public interface IVISABFile {

    LocalDateTime getCreationDate();

    String getFileFormatVersion();

    public String getGame();
}
