package org.visab.workspace;

public class Workspace {

    private ConfigManager configManager;

    private DatabaseManager databaseManager;

    private LogManager logManager;

    private static Workspace instance;

    private Workspace() {
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
