package org.visab.workspace;

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

    public static final String WORKSPACE_PATH = VISABUtil.combinePath(System.getProperty("user.dir"), "workspace");

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