package org.visab.workspace;

import org.visab.util.VISABUtil;
import org.visab.workspace.config.ConfigManager;

public class Workspace {

    public static final String WORKSPACE_PATH = VISABUtil.combinePath(VISABUtil.getRunningJarRootDirPath(),
            "workspace");

    private ConfigManager configManager;

    private DatabaseManager databaseManager;

    private LogManager logManager;

    private static Workspace instance;

    private Workspace() {
        configManager = new ConfigManager();
        databaseManager = new DatabaseManager();
        logManager = new LogManager();
    }

    public static Workspace getInstance() {
        if (instance == null)
            instance = new Workspace();

        return instance;
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
