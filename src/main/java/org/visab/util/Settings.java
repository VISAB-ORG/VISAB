package org.visab.util;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO for holding all settings required in the project. TODO: Should be
 * persisted in file and loaded at start of VISAB.
 *
 * @author moritz
 *
 */
public final class Settings {

    public static final int API_PORT = 2673;
    public static final String JSON_MIME_TYPE = "application/json";
    public static final String MEDIA_CONTENT_TYPE = JSON_MIME_TYPE;
    public static final String WEB_API_BASE_ADDRESS = "localhost:" + API_PORT;

    // Always initialize the data path respective to the root dir path of execution
    public static final String DATA_PATH = VISABUtil.getRunningJarRootDirPath() + "/database/";
    public static final String CSS_PATH = "/application.css";
    public static final String VISAB_DOC_PATH = "/pdf/visab_documentation.pdf";
    public static final String IMAGE_PATH = "/img/";


    /**
     * The time in seconds until a session is automatically timed out if no statistics were received.
     */
    public static final int SESSION_TIMEOUT = 2;
}
