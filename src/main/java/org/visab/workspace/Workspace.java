package org.visab.workspace;

import org.visab.util.UserObject;
import org.visab.util.VISABUtil;
import org.visab.workspace.config.ConfigManager;

/**
 * The VISAB workspace, from which config, database and log management can be
 * done. It is the central point for modifying settings, viewmanagement and
 * deleting/adding/renaming VISAB files.
 */
public class Workspace {

    /**
     * Singelton instance
     */
    private static Workspace instance;

    public static final String WORKSPACE_PATH = VISABUtil.combinePath(VISABUtil.getRunningJarRootDirPath(),
            "workspace");

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

    private ConfigManager configManager = new ConfigManager();

    private DatabaseManager databaseManager = new DatabaseManager();

    private LogManager logManager = new LogManager();

    /**
     * For singelton
     */
    private Workspace() {
    }
    
    public UserObject getSettings() {
        return this.configManager.getSettings();
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public LogManager getLogManager() {
        return this.logManager;
    }

}

/**
 * Gets the static Workspace instance
 * 
 * @return The static Workspace instance
 * 
 *         public static Workspace getInstance() { if (instance == null)
 *         instance = new Workspace();
 * 
 *         return instance; }
 */
