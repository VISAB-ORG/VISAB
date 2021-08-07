package org.visab.workspace;

import org.visab.util.VISABUtil;

/**
 * The VISAB workspace, from which config and database management can be done.
 * It is the central point for modifying settings and deleting, adding, and
 * renaming VISAB files.
 * 
 * For consistency reasons, the only Manager instances should be the ones in the
 * Workspace singelton instance.
 */
public class Workspace {

    protected static final String WORKSPACE_PATH = VISABUtil.combinePath(System.getProperty("user.dir"), "workspace");

    private static Workspace instance;

    private ConfigManager configManager = new ConfigManager();

    private DatabaseManager databaseManager = new DatabaseManager();

    /**
     * For singelton
     */
    private Workspace() {
    }

    /**
     * Gets the singelton instance
     * 
     * @return The singelton instance
     */
    public static Workspace getInstance() {
        if (instance == null)
            instance = new Workspace();

        return instance;
    }

    /**
     * The ConfigManager instance.
     */
    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    /**
     * The DatabaseManager instance.
     */
    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

}