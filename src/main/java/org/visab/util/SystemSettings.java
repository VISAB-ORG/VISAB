package org.visab.util;

/**
 * POJO for holding all settings in the project that can not be configured by the user. 
 *
 * @author tim
 */
public final class SystemSettings {

    public static final String JSON_MIME_TYPE = "application/json";
    public static final String MEDIA_CONTENT_TYPE = JSON_MIME_TYPE;
    public static final String DATA_PATH = VISABUtil.getRunningJarRootDirPath() + "/database/";
    public static final String CSS_PATH = "/application.css";
    public static final String VISAB_DOC_PATH = "/pdf/visab_documentation.pdf";
    public static final String IMAGE_PATH = "/img/";

}
