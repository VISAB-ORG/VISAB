package org.visab.workspace;

import org.visab.util.VISABUtil;
import org.visab.workspace.config.ConfigManager;

/**
 * The VISAB workspace, from which config, database and log management can be
 * done. It is the central point for modifying settings, viewmanagement and
 * deleting/adding/renaming VISAB files.
 */
public class Workspace {

    public static final String WORKSPACE_PATH = VISABUtil.combinePath(VISABUtil.getRunningJarRootDirPath(),
            "workspace");

    private ConfigManager configManager;

    private DatabaseManager databaseManager;

    private LogManager logManager;

    /**
     * Singelton instance
     */
    public static final Workspace instance = new Workspace();

    /**
     * For singelton
     */
    private Workspace() {
        configManager = new ConfigManager();
        databaseManager = new DatabaseManager();
        logManager = new LogManager();
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
